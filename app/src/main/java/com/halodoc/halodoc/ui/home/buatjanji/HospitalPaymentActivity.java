package com.halodoc.halodoc.ui.home.buatjanji;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.halodoc.halodoc.HomeActivity;
import com.halodoc.halodoc.LoginActivity;
import com.halodoc.halodoc.R;
import com.halodoc.halodoc.RegisterActivity;
import com.halodoc.halodoc.databinding.ActivityHospitalPaymentBinding;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class HospitalPaymentActivity extends AppCompatActivity {

    private ActivityHospitalPaymentBinding binding;
    public static final String EXTRA_HOSPITAL = "hospital";
    public static final String EXTRA_SERVICES = "services";
    public static final String EXTRA_BOOKING_DATE = "booking";
    public static final String EXTRA_NOTES = "notes";
    public static final String EXTRA_PRICE = "price";

    private static final int REQUEST_FROM_GALLERY_TO_SELF_PHOTO = 1001;


    private String name;
    private String uid;
    private String services;
    private String bookingDate;
    private String notes;
    private String price;
    private String type;
    private String dp;
    private String location;
    private String paymentMethod;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHospitalPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // dapatkan atribut dari rumah sakit yang di klik pengguna
        HospitalModel hospitalModel = getIntent().getParcelableExtra(EXTRA_HOSPITAL);
        name = hospitalModel.getName();
        uid = hospitalModel.getUid();
        type = hospitalModel.getType();
        dp = hospitalModel.getDp();
        location = hospitalModel.getLocation();
        services = getIntent().getStringExtra(EXTRA_SERVICES);
        bookingDate = getIntent().getStringExtra(EXTRA_BOOKING_DATE);
        notes = getIntent().getStringExtra(EXTRA_NOTES);
        price = getIntent().getStringExtra(EXTRA_PRICE);

        binding.name.setText(name);
        binding.type.setText(type);
        binding.price.setText("Biaya: Rp." + price);
        binding.keterangan.setText("Kamu memilih layanan " + services + ", pada tanggal " + bookingDate + ", dengan catatan: " + notes + ".");

        Glide.with(this)
                .load(dp)
                .error(R.drawable.ic_error)
                .into(binding.dp);

        // KEMBALI KE HALAMAN SEBELUMNYA
        clickBack();


        // UNGGAH BUKTI TRANSFER PEMBAYARAN
        uploadPaymentProof();

        // KLIK SELESAIKAN TRANSAKSI
        binding.finishBtn.setOnClickListener(view -> {
            paymentMethod = binding.paymentMethodEt.getText().toString();

            if(paymentMethod.isEmpty()) {
                Toast.makeText(this, "Pilih metode pembayaran", Toast.LENGTH_SHORT).show();
                return;
            }

            binding.progressBar.setVisibility(View.VISIBLE);
            savePaymentToDatabase();
        });

        // METODE PEMBAYARAN
        pickPaymentMethod();

    }

    @SuppressLint("SetTextI18n")
    private void pickPaymentMethod() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.payment, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        binding.paymentMethodEt.setAdapter(adapter);
        binding.paymentMethodEt.setOnItemClickListener((adapterView, view, i, l) -> {
            paymentMethod = binding.paymentMethodEt.getText().toString();
            if(paymentMethod.equals("Transfer Bank")) {
                binding.textView19.setVisibility(View.VISIBLE);
                binding.roundedImageView.setVisibility(View.VISIBLE);
                binding.imageHint.setVisibility(View.VISIBLE);
                binding.finishBtn.setVisibility(View.GONE);
                binding.rekening.setText("No Rekening: 123 45678 90123");
            } else {
                binding.textView19.setVisibility(View.INVISIBLE);
                binding.roundedImageView.setVisibility(View.GONE);
                binding.imageHint.setVisibility(View.INVISIBLE);
                binding.finishBtn.setVisibility(View.VISIBLE);
                binding.rekening.setText("No " + paymentMethod +": 0812 3456 7890");
            }
        });
    }

    private void uploadPaymentProof() {
        binding.roundedImageView.setOnClickListener(view -> {
            ImagePicker
                    .with(this)
                    .galleryOnly()
                    .compress(1024)
                    .maxResultSize(1080, 1080)
                    .start(REQUEST_FROM_GALLERY_TO_SELF_PHOTO);

        });
    }

    private void clickBack() {
        binding.backButton.setOnClickListener(view -> {
            onBackPressed();
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_FROM_GALLERY_TO_SELF_PHOTO) {
                binding.progressBar.setVisibility(View.VISIBLE);
                HospitalDatabase.uploadImageToDatabase(data.getData(), this, binding.imageHint);
                Glide.with(this)
                        .load(data.getData())
                        .placeholder(R.drawable.ic_baseline_broken_image_24)
                        .error(R.drawable.ic_baseline_broken_image_24)
                        .into(binding.roundedImageView);

                // DELAY SELAMA 3 DETIK KEMUDIAN SIMPAN KE DATABASE
                new Handler()
                        .postDelayed(this::savePaymentToDatabase, 5000);
            }
        }
    }

    private void savePaymentToDatabase() {
        if (HospitalDatabase.proofPayment != null || !paymentMethod.isEmpty()) {

            String timeInMillis = String.valueOf(System.currentTimeMillis());
            String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

            // BUAT JANJI
            Map<String, Object> hospitalPromisePayment = new HashMap<>();
            hospitalPromisePayment.put("uid", timeInMillis);
            hospitalPromisePayment.put("hospitalUid", uid);
            hospitalPromisePayment.put("userUid", userUid);
            hospitalPromisePayment.put("services", services);
            hospitalPromisePayment.put("price", price);
            hospitalPromisePayment.put("type", type);
            hospitalPromisePayment.put("dp",dp);
            hospitalPromisePayment.put("name", name);
            hospitalPromisePayment.put("location", location);
            hospitalPromisePayment.put("bookingDate", bookingDate);
            hospitalPromisePayment.put("notes", notes);
            hospitalPromisePayment.put("status", "Belum Diverifikasi");
            if(!paymentMethod.isEmpty()) {
                hospitalPromisePayment.put("paymentProof", paymentMethod);
            } else {
                hospitalPromisePayment.put("paymentProof", HospitalDatabase.proofPayment);
            }

            FirebaseFirestore
                    .getInstance()
                    .collection("hospitalPromisePayment")
                    .document(timeInMillis)
                    .set(hospitalPromisePayment)
                    .addOnCompleteListener(task -> {
                        if (!task.isSuccessful()) {
                            binding.progressBar.setVisibility(View.GONE);
                            Log.e("Error Buat Janji", task.toString());
                            Toast.makeText(HospitalPaymentActivity.this, "Gagal mengunggah bukti pembayaran", Toast.LENGTH_SHORT).show();
                        } else {
                            transaction(timeInMillis, userUid);
                        }
                    });
        }
    }

    private void transaction(String timeInMillis, String userUid) {
        // TRANSAKSI
        Map<String, Object> transaction = new HashMap<>();
        transaction.put("uid", timeInMillis);
        transaction.put("userUid", userUid);
        transaction.put("name", name);
        transaction.put("type", type);
        transaction.put("dp", dp);
        transaction.put("notes", notes);
        transaction.put("services", services);
        transaction.put("price", price);
        transaction.put("bookingDate", bookingDate);
        transaction.put("status", "Belum Diverifikasi");
        transaction.put("transactionType", "hospitalPromisePayment");
        if(!paymentMethod.isEmpty()) {
            transaction.put("paymentProof", paymentMethod);
        } else {
            transaction.put("paymentProof", HospitalDatabase.proofPayment);
        }
        FirebaseFirestore
                .getInstance()
                .collection("transaction")
                .document(timeInMillis)
                .set(transaction)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        binding.progressBar.setVisibility(View.GONE);
                        showSuccessDialog();
                    } else {
                        binding.progressBar.setVisibility(View.GONE);
                        Log.e("Error Transaction", task.toString());
                        Toast.makeText(HospitalPaymentActivity.this, "Gagal membuat transaksi", Toast.LENGTH_SHORT).show();
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
                    Intent intent = new Intent(HospitalPaymentActivity.this, HomeActivity.class);
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