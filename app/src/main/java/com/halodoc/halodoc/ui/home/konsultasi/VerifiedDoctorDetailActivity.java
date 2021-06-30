package com.halodoc.halodoc.ui.home.konsultasi;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.halodoc.halodoc.R;
import com.halodoc.halodoc.databinding.ActivityVerifiedDoctorDetailBinding;


public class VerifiedDoctorDetailActivity extends AppCompatActivity {

    private ActivityVerifiedDoctorDetailBinding binding;
    public static final String EXTRA_DOCTOR = "doctor";
    private String doctorUid;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVerifiedDoctorDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // TAMPILKAN DETAIL INFORMASI DOKTER YANG INGIN DIVERIFIKASI
        ConsultationWithDoctorModel model = getIntent().getParcelableExtra(EXTRA_DOCTOR);
        doctorUid = model.getUid();
        binding.name.setText(model.getName());
        binding.sertifikatKeahlian.setText(model.getSertifikatKeahlian());
        binding.pengalaman.setText("Bekerja Selama : " + model.getExperience() + " tahun");
        binding.phone.setText(model.getPhone());
        binding.description.setText(model.getDescription());
        Glide.with(this)
                .load(model.getDp())
                .into(binding.dp);

        Glide.with(this)
                .load(model.getPractice())
                .into(binding.practice);

        Glide.with(this)
                .load(model.getSpecialist())
                .into(binding.specialist);

        // KLIK VERIFIKASI DOKTER
        clickVerified();

        // KLIK KEMBALI KE HALAMAN SEBELUMNYA
        binding.backButton.setOnClickListener(view -> {
            onBackPressed();
        });

    }

    private void clickVerified() {
        binding.verifBtn.setOnClickListener(view -> FirebaseFirestore
                .getInstance()
                .collection("doctor")
                .document(doctorUid)
                .update("role", "doctor")
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        showSuccessDialog();
                    } else {
                        Toast.makeText(VerifiedDoctorDetailActivity.this, "Gagal memverifikasi dokter", Toast.LENGTH_SHORT).show();
                    }
                }));
    }

    private void showSuccessDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Berhasil Melakukan Verifikasi")
                .setMessage("Dokter ini berhasi diverifikai, ia akan mulai praktik setelah ini")
                .setIcon(R.drawable.check)
                .setPositiveButton("OKE", (dialogInterface, i) -> {
                    onBackPressed();
                })
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}