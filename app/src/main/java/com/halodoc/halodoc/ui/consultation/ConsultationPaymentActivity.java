package com.halodoc.halodoc.ui.consultation;


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
import android.widget.ArrayAdapter;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.halodoc.halodoc.HomeActivity;
import com.halodoc.halodoc.R;
import com.halodoc.halodoc.databinding.ActivityConsultationPaymentBinding;
import com.halodoc.halodoc.ui.home.konsultasi.ConsultationWithDoctorModel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ConsultationPaymentActivity extends AppCompatActivity {

    public static final String EXTRA_CONSULTATION = "consult";
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
    private String notes;
    private String paymentMethod;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConsultationPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();


        // TAMPILKAN DETAIL INFORMASI DOKTER YANG INGIN DIVERIFIKASI
        ConsultationWithDoctorModel model = getIntent().getParcelableExtra(EXTRA_CONSULTATION);
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
            notes = binding.notesEt.getText().toString().trim();

            if(notes.isEmpty()) {
                Toast.makeText(this, "Silahkan tulis catatan konsultasi terlebih dahulu", Toast.LENGTH_SHORT).show();
                return;
            }

            ImagePicker
                    .with(this)
                    .galleryOnly()
                    .compress(1024)
                    .maxResultSize(1080, 1080)
                    .start(REQUEST_FROM_GALLERY_TO_SELF_PHOTO);
        });

        // KLIK SELESAIKAN TRANSAKSI
        binding.finishBtn.setOnClickListener(view -> {
            paymentMethod = binding.paymentMethodEt.getText().toString();
            notes = binding.notesEt.getText().toString().trim();

            if(paymentMethod.isEmpty()) {
                Toast.makeText(this, "Pilih metode pembayaran", Toast.LENGTH_SHORT).show();
                return;
            } else if(notes.isEmpty()) {
                Toast.makeText(this, "Catatan tidak boleh kosong", Toast.LENGTH_SHORT).show();
                return;
            }

            ProgressDialog mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Mohon tunggu hingga proses selesai...");
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();

            setProofPaymentToConsultation(mProgressDialog, paymentMethod);
        });


        getCustomerName();

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
                binding.text100.setVisibility(View.VISIBLE);
                binding.proofPayment.setVisibility(View.VISIBLE);
                binding.imageHint.setVisibility(View.VISIBLE);
                binding.finishBtn.setVisibility(View.GONE);
                binding.textView26.setText("No Rekening: 123 45678 90123");
            } else {
                binding.text100.setVisibility(View.INVISIBLE);
                binding.proofPayment.setVisibility(View.GONE);
                binding.imageHint.setVisibility(View.INVISIBLE);
                binding.finishBtn.setVisibility(View.VISIBLE);
                binding.textView26.setText("No " + paymentMethod +": 0812 3456 7890");
            }
        });
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

        Map<String, Object> consultation = new HashMap<>();
        consultation.put("uid", timeInMillis);
        consultation.put("doctorUid", doctorUid);
        consultation.put("doctorName", name);
        consultation.put("customerUid", uid);
        consultation.put("customerName", customerName);
        consultation.put("services", services);
        consultation.put("notes", notes);
        consultation.put("price", price);
        consultation.put("doctorDp", dp);
        consultation.put("status", "Belum Diverifikasi");
        consultation.put("customerDp", customerDp);


        FirebaseFirestore
                .getInstance()
                .collection("consultation")
                .document(timeInMillis)
                .set(consultation)
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
        transaction.put("userUid", uid);
        transaction.put("services", services);
        transaction.put("price", price);
        transaction.put("bookingDate", format);
        transaction.put("notes", notes);
        transaction.put("status", "Belum Diverifikasi");
        transaction.put("paymentProof", proofPayment);
        transaction.put("transactionType", "consultation");

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
                .setMessage("Admin akan memverifikasi bukti pembayaran yang kamu kirimkan, terima kasih telah menggunakan Halodoc\n\nUntuk memulai konsultasi, silahkan pilih navigasi konsultasi, kamu bisa melakukan konsultasi setelah bukti pembayaranmu terverifikasi")
                .setIcon(R.drawable.ic_baseline_check_circle_24)
                .setCancelable(false)
                .setPositiveButton("YA", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    Intent intent = new Intent(ConsultationPaymentActivity.this, HomeActivity.class);
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