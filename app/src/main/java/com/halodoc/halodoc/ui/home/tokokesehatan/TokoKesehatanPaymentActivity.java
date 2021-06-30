package com.halodoc.halodoc.ui.home.tokokesehatan;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.halodoc.halodoc.HomeActivity;
import com.halodoc.halodoc.R;
import com.halodoc.halodoc.databinding.ActivityTokoKesehatanPaymentBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TokoKesehatanPaymentActivity extends AppCompatActivity {

    public static final String EXTRA_PRICE = "price";
    public static final String EXTRA_TYPE = "type";
    public static final String EXTRA_QTY = "qty";
    public static final String EXTRA_DP = "dp";
    public static final String EXTRA_NAME = "name";

    private ActivityTokoKesehatanPaymentBinding binding;
    private String type;
    int rp;
    private static final int REQUEST_FROM_GALLERY_TO_SELF_PHOTO = 1001;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTokoKesehatanPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String price = getIntent().getStringExtra(EXTRA_PRICE);
        type = getIntent().getStringExtra(EXTRA_TYPE);
        String qty = getIntent().getStringExtra(EXTRA_QTY);

        rp = Integer.parseInt(price) * Integer.parseInt(qty);

        binding.price.setText("Rp." + rp);


        // UNGGAH BUKTI PEMBAYARAN
        uploadProofPayment();

    }

    private void uploadProofPayment() {
        binding.roundedImageView.setOnClickListener(view -> {
            if(binding.detailLokasi.getText().toString().trim().isEmpty()) {
                Toast.makeText(TokoKesehatanPaymentActivity.this, "Mohon isikan lokasi anda", Toast.LENGTH_SHORT).show();
                return;
            }
            ImagePicker
                    .with(this)
                    .galleryOnly()
                    .compress(1024)
                    .maxResultSize(1080, 1080)
                    .start(REQUEST_FROM_GALLERY_TO_SELF_PHOTO);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_FROM_GALLERY_TO_SELF_PHOTO) {
                // UNGGAH BUKTI PEMBAYARAN UNTUK KONSULTASI DENGAN DOKTER
                uploadProofPaymentToDatabase(data.getData());
                Glide.with(this)
                        .load(data.getData())
                        .into(binding.roundedImageView);
            }
        }
    }

    private void uploadProofPaymentToDatabase(Uri data) {

        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
        ProgressDialog mProgressDialog = new ProgressDialog(this);

        mProgressDialog.setMessage("Mohon tunggu hingga proses selesai...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
        String imageFileName = "paymentProof/user_" + System.currentTimeMillis() + ".png";


        mStorageRef.child(imageFileName).putFile(data)
                .addOnSuccessListener(taskSnapshot ->
                        mStorageRef.child(imageFileName).getDownloadUrl()
                                .addOnSuccessListener(uri -> {
                                    binding.imageHint.setVisibility(View.GONE);
                                    setProofPaymentToActivity(mProgressDialog, uri.toString());
                                })
                                .addOnFailureListener(e -> {
                                    mProgressDialog.dismiss();
                                    Toast.makeText(TokoKesehatanPaymentActivity.this, "Gagal mengunggah bukti pembayaran", Toast.LENGTH_SHORT).show();
                                    Log.d("productDp", e.toString());
                                    binding.imageHint.setVisibility(View.VISIBLE);
                                }))
                .addOnFailureListener(e -> {
                    mProgressDialog.dismiss();
                    Toast.makeText(TokoKesehatanPaymentActivity.this, "Gagal mengunggah bukti pembayaran", Toast.LENGTH_SHORT).show();
                    Log.d("productDp", e.toString());
                    binding.imageHint.setVisibility(View.VISIBLE);
                });

    }

    private void setProofPaymentToActivity(ProgressDialog mProgressDialog, String proofPayment) {
        String timeInMillis = String.valueOf(System.currentTimeMillis());

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat getDate = new SimpleDateFormat("dd MMMM yyyy");
        String format = getDate.format(new Date());
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // TRANSAKSI
        Map<String, Object> transaction = new HashMap<>();
        transaction.put("uid", timeInMillis);
        transaction.put("name", getIntent().getStringExtra(EXTRA_NAME));
        transaction.put("type", type);
        transaction.put("dp", getIntent().getStringExtra(EXTRA_DP));
        transaction.put("userUid", uid);
        transaction.put("services", "Toko Kesehatan");
        transaction.put("price", String.valueOf(rp));
        transaction.put("bookingDate", format);
        transaction.put("notes", binding.detailLokasi.getText().toString().trim());
        transaction.put("status", "Belum Diverifikasi");
        transaction.put("paymentProof", proofPayment);
        transaction.put("transactionType", "market");

        FirebaseFirestore
                .getInstance()
                .collection("transaction")
                .document(timeInMillis)
                .set(transaction)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // KE CHAT KONSULTASI
                        mProgressDialog.dismiss();
                        showSuccessDialog();
                    } else {
                        mProgressDialog.dismiss();
                        Log.e("Error Transaction", task.toString());
                        Toast.makeText(TokoKesehatanPaymentActivity.this, "Gagal membuat transaksi", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void showSuccessDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Berhasi Mengunggah Bukti Pembayaran")
                .setMessage("Admin akan memverifikasi bukti pembayaran yang kamu kirimkan, terima kasih telah menggunakan Halodoc\n\nBarang kamu akan dikirimkan ke alamat tujuan sesegera mungkin oleh tim halodoc")
                .setIcon(R.drawable.ic_baseline_check_circle_24)
                .setCancelable(false)
                .setPositiveButton("YA", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    Intent intent = new Intent(TokoKesehatanPaymentActivity.this, HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                })
                .show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}