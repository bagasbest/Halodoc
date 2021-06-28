package com.halodoc.halodoc.ui.consultation;

import androidx.annotation.FontRes;
import androidx.annotation.Nullable;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.halodoc.halodoc.HomeActivity;
import com.halodoc.halodoc.R;
import com.halodoc.halodoc.databinding.ActivityConsultationPaymentBinding;
import com.halodoc.halodoc.ui.home.buatjanji.HospitalPaymentActivity;
import com.halodoc.halodoc.ui.home.konsultasi.ConsultationWithDoctorModel;
import com.halodoc.halodoc.ui.profile.ProfileDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ConsultationPaymentActivity extends AppCompatActivity {

    public static final String EXTRA_CONSULTATION = "consult";
    private ConsultationWithDoctorModel model;
    private ActivityConsultationPaymentBinding binding;
    private final int REQUEST_FROM_GALLERY_TO_SELF_PHOTO = 1001;

    private String name;
    private String type;
    private String dp;
    private String services;
    private String price;
    private String doctorUid;
    private String customerName;
    private String uid;
    private String customerDp;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConsultationPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();


        // TAMPILKAN DETAIL INFORMASI DOKTER YANG INGIN DIVERIFIKASI
        model = getIntent().getParcelableExtra(EXTRA_CONSULTATION);
        name = model.getName();
        type = model.getSertifikatKeahlian();
        dp = model.getDp();
        services = model.getSertifikatKeahlian();
        price = model.getPrice();
        doctorUid = model.getUid();

        binding.name.setText(name);
        binding.sertifikatKeahlian.setText(services);
        binding.pengalaman.setText("Telah bekerja selama: " + model.getExperience() + " tahun");
        binding.price.setText("Rp. " + price);
        binding.description.setText(model.getDescription());
        Glide.with(this)
                .load(dp)
                .into(binding.dp);

        // KLIK UNGGAH BUKTI TRANSFER
        binding.proofPayment.setOnClickListener(view -> {
            ImagePicker
                    .with(this)
                    .galleryOnly()
                    .compress(1024)
                    .maxResultSize(1080, 1080)
                    .start(REQUEST_FROM_GALLERY_TO_SELF_PHOTO);
        });


        getCustomerName();

    }

    private void getCustomerName() {
        FirebaseFirestore
                .getInstance()
                .collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                   customerName = ""  + documentSnapshot.get("name");
                   customerDp = ""  + documentSnapshot.get("userDp");
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
                        .into(binding.proofPayment);
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
                                    setProofPaymentToConsultation(mProgressDialog, uri.toString());
                                })
                                .addOnFailureListener(e -> {
                                    mProgressDialog.dismiss();
                                    Toast.makeText(ConsultationPaymentActivity.this, "Gagal mengunggah bukti pembayaran", Toast.LENGTH_SHORT).show();
                                    Log.d("productDp", e.toString());
                                    binding.imageHint.setVisibility(View.VISIBLE);
                                }))
                .addOnFailureListener(e -> {
                    mProgressDialog.dismiss();
                    Toast.makeText(ConsultationPaymentActivity.this, "Gagal mengunggah bukti pembayaran", Toast.LENGTH_SHORT).show();
                    Log.d("productDp", e.toString());
                    binding.imageHint.setVisibility(View.VISIBLE);
                });
    }

    private void setProofPaymentToConsultation(ProgressDialog mProgressDialog, String proofPayment) {

        String timeInMillis = String.valueOf(System.currentTimeMillis());

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat getDate = new SimpleDateFormat("dd MMMM yyyy");
        String format = getDate.format(new Date());

        Map<String, Object> proofPaymentImage = new HashMap<>();
        proofPaymentImage.put("uid", timeInMillis);
        proofPaymentImage.put("doctorUid", doctorUid);
        proofPaymentImage.put("doctorName", name);
        proofPaymentImage.put("customerUid", uid);
        proofPaymentImage.put("customerName", customerName);
        proofPaymentImage.put("services", services);
        proofPaymentImage.put("price", price);
        proofPaymentImage.put("dp", dp);


        FirebaseFirestore
                .getInstance()
                .collection("consultation")
                .document(timeInMillis)
                .set(proofPaymentImage)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // TRANSAKSI
                        setTransaction(timeInMillis, format, proofPayment, mProgressDialog);

                    } else {
                        Log.e("Error Transaction", task.toString());
                        Toast.makeText(ConsultationPaymentActivity.this, "Gagal membuat transaksi", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setTransaction(String timeInMillis, String format, String proofPayment, ProgressDialog mProgressDialog) {
        // TRANSAKSI
        Map<String, Object> transaction = new HashMap<>();
        transaction.put("uid", timeInMillis);
        transaction.put("name", name);
        transaction.put("type", type);
        transaction.put("dp", dp);
        transaction.put("services", services);
        transaction.put("price", price);
        transaction.put("bookingDate", format);
        transaction.put("status", "Belum Diverifikasi");
        transaction.put("proofPayment", proofPayment);
        transaction.put("transactionType", "Konsultasi");

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
                        Toast.makeText(ConsultationPaymentActivity.this, "Gagal membuat transaksi", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showSuccessDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Berhasi Mengunggah Bukti Pembayaran")
                .setMessage("Admin akan memverifikasi bukti pembayaran yang kamu kirimkan, terima kasih telah menggunakan Halodoc")
                .setIcon(R.drawable.ic_baseline_check_circle_24)
                .setCancelable(false)
                .setPositiveButton("YA", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    Intent intent = new Intent(ConsultationPaymentActivity.this, ConsultationChatActivity.class);
                    intent.putExtra(ConsultationChatActivity.EXTRA_DOCTOR_DP, dp);
                    intent.putExtra(ConsultationChatActivity.EXTRA_DOCTOR_NAME, name);
                    intent.putExtra(ConsultationChatActivity.EXTRA_DOCTOR_UID, doctorUid);
                    intent.putExtra(ConsultationChatActivity.EXTRA_CUSTOMER_NAME, customerName);
                    intent.putExtra(ConsultationChatActivity.EXTRA_CUSTOMER_UID, uid);
                    intent.putExtra(ConsultationChatActivity.EXTRA_CUSTOMER_DP, dp);
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