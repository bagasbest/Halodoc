package com.halodoc.halodoc.ui.home.konsultasi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.halodoc.halodoc.R;
import com.halodoc.halodoc.databinding.ActivityAddDoctorBinding;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class AddDoctorActivity extends AppCompatActivity {

    private ActivityAddDoctorBinding binding;

    private static final int REQUEST_FROM_CAMERA_TO_FORMAL_PICTURE = 1001;
    private static final int REQUEST_FROM_CAMERA_TO_PRACTICE_CERTIFICATE = 1002;
    private static final int REQUEST_FROM_CAMERA_TO_SPECIALIST_CERTIFICATE = 1003;

    private String sertifikatKeahlian;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddDoctorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // KLIK MENDAFTAR & VERIFIKASI FORM
        clickRegisterDoctor();

        // KLIK UNGGAH FOTO FORMAL
        clickUploadFormalPic();

        // KLIK UNGGAH FOTO SERTIFIKAT PRAKTIK
        clickUploadPracticeCertificate();

        // KLIK UNGGAH FOTO SERTIFIKAT PRAKTIK
        clickUploadSpecialistCertificate();

        // KLIK KEMBALI
        clickBackButton();

        // PILIH SERTIFIKAT KEAHLIAN
        //tampilkan kecamatan
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.specialist, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        binding.sertifikatKeahlian.setAdapter(adapter);
        binding.sertifikatKeahlian.setOnItemClickListener((adapterView, view, i, l) -> {
            sertifikatKeahlian = binding.sertifikatKeahlian.getText().toString();
        });

    }

    private void clickUploadSpecialistCertificate() {
        binding.sertifikatSpesialis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(AddDoctorActivity.this)
                        .galleryOnly()
                        .compress(1024)
                        .maxResultSize(1080, 1080)
                        .start(REQUEST_FROM_CAMERA_TO_SPECIALIST_CERTIFICATE);
            }
        });
    }

    private void clickUploadPracticeCertificate() {
        binding.sertifikatKeteranganPraktik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(AddDoctorActivity.this)
                        .galleryOnly()
                        .compress(1024)
                        .maxResultSize(1080, 1080)
                        .start(REQUEST_FROM_CAMERA_TO_PRACTICE_CERTIFICATE);
            }
        });
    }

    private void clickBackButton() {
        binding.backButton.setOnClickListener(view -> onBackPressed());
    }

    private void clickUploadFormalPic() {
        binding.fotoFormal.setOnClickListener(view -> ImagePicker.with(this)
                .galleryOnly()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start(REQUEST_FROM_CAMERA_TO_FORMAL_PICTURE));
    }

    private void clickRegisterDoctor() {

        binding.registerDoctorButton.setOnClickListener(view -> {
            String name = binding.name.getText().toString().trim();
            String desc = binding.description.getText().toString();
            String phone = binding.phone.getText().toString().trim();
            String pengalaman = binding.pengalaman.getText().toString().trim();
            String formal = AddDoctorDatabase.formal;
            String practice = AddDoctorDatabase.practice;
            String specialist = AddDoctorDatabase.specialist;
            sertifikatKeahlian = binding.sertifikatKeahlian.getText().toString();


            if(name.isEmpty()) {
                binding.name.setError("Nama Lengkap & Gelar, tidak boleh kosong");
                return;
            }
            else if(desc.isEmpty()){
                binding.description.setError("Deskripsi, tidak boleh kosong");
                return;
            }
            else if(sertifikatKeahlian == null){
                binding.sertifikatKeahlian.setError("Sertifikat Keahlian, tidak boleh kosong");
                return;
            }
            else if(phone.isEmpty()){
                binding.phone.setError("Nomor Telepon, tidak boleh kosong");
                return;
            }
            else if(formal == null) {
                Toast.makeText(AddDoctorActivity.this, "Silahkan unggah foto formal", Toast.LENGTH_SHORT).show();
                return;
            }
            else if(practice == null) {
                Toast.makeText(AddDoctorActivity.this, "Silahkan unggah sertifikat praktik anda", Toast.LENGTH_SHORT).show();
                return;
            }
            else if(specialist == null) {
                Toast.makeText(AddDoctorActivity.this, "Silahkan unggah sertifikat spesialis anda", Toast.LENGTH_SHORT).show();
                return;
            }




            // SIMPAN DATA DOKTER KE DATABASE
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            binding.progressBar.setVisibility(View.VISIBLE);
            Map<String, Object> doctor = new HashMap<>();
            doctor.put("name", name);
            doctor.put("description", desc);
            doctor.put("sertifikatKeahlian", sertifikatKeahlian);
            doctor.put("experience", pengalaman);
            doctor.put("phone", phone);
            doctor.put("dp", formal);
            doctor.put("like", "0");
            doctor.put("price", "25000");
            doctor.put("practice", practice);
            doctor.put("specialist", specialist);
            doctor.put("uid", uid);
            doctor.put("role", "waiting");


            FirebaseFirestore
                    .getInstance()
                    .collection("doctor")
                    .document(uid)
                    .set(doctor)
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()) {
                            binding.progressBar.setVisibility(View.GONE);
                            showSuccessDialog();
                        } else {
                            binding.progressBar.setVisibility(View.GONE);
                            Toast.makeText(AddDoctorActivity.this, "Gagal registrasi: " + task.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });



    }

    private void showSuccessDialog() {
            new AlertDialog.Builder(this)
                    .setTitle("Berhasil Mendaftar")
                    .setMessage("Silahkan menunggu verifikasi dari admin KitaSehat selama 7 x 24 Jam, data anda aman di database KitaSehat.\n\nAnda dapat mulai praktik langsung setelah data anda di verifikasi")
                    .setIcon(R.drawable.check)
                    .setPositiveButton("OKE", (dialogInterface, i) -> {
                        onBackPressed();
                    })
                    .show();
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_FROM_CAMERA_TO_FORMAL_PICTURE: {
                    AddDoctorDatabase.uploadFormalPicture(this, data.getData(), "formal");
                    binding.fotoFormal.setText("Sudah Diunggah");
                    break;
                }
                case REQUEST_FROM_CAMERA_TO_PRACTICE_CERTIFICATE: {
                    AddDoctorDatabase.uploadFormalPicture(this, data.getData(), "practice");
                    binding.sertifikatKeteranganPraktik.setText("Sudah Diunggah");
                    break;
                }
                case REQUEST_FROM_CAMERA_TO_SPECIALIST_CERTIFICATE: {
                    AddDoctorDatabase.uploadFormalPicture(this, data.getData(), "specialist");
                    binding.sertifikatSpesialis.setText("Sudah Diunggah");
                    break;
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}