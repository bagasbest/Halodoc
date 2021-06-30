package com.halodoc.halodoc.ui.home.konsultasi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.halodoc.halodoc.HomeActivity;
import com.halodoc.halodoc.LoginActivity;
import com.halodoc.halodoc.R;
import com.halodoc.halodoc.databinding.ActivityConsultationWithDoctorDetailBinding;
import com.halodoc.halodoc.ui.consultation.ConsultationPaymentActivity;
import com.halodoc.halodoc.ui.home.buatjanji.HospitalPaymentActivity;

import org.jetbrains.annotations.NotNull;

public class ConsultationWithDoctorDetailActivity extends AppCompatActivity {

    private ActivityConsultationWithDoctorDetailBinding binding;
    private ConsultationWithDoctorModel model;
    public static final String EXTRA_DOCTOR = "doctor";
    private boolean isFavorite = false;
    private int like = 0;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConsultationWithDoctorDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // TAMPILKAN DETAIL INFORMASI DOKTER YANG INGIN DIVERIFIKASI
        model = getIntent().getParcelableExtra(EXTRA_DOCTOR);

        like = Integer.parseInt(model.getLike());

        binding.name.setText(model.getName());
        binding.sertifikatKeahlian.setText(model.getSertifikatKeahlian());
        binding.pengalaman.setText("Telah bekerja selama: " + model.getExperience()  +" tahun");
        binding.price.setText("Biaya: Rp. " + model.getPrice());
        binding.like.setText(model.getLike() + " Orang telah merekomendasikan " + model.getName());
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

        // KLIK KONSULTASI
        clickConsultation();


        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            // CEK APAKAH SAYA MENYUKAI DOKTER INI SAAT INI
            checkIfLikedDoctor(model.getUid());
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();


            if(model.getUid().equals(uid)){
                //UBAH HARGA KONSULTASI (KHUSUS DOKTER TERSEBUT)
                binding.setPrice.setVisibility(View.VISIBLE);
                binding.setPrice.setOnClickListener(view -> {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ConsultationWithDoctorDetailActivity.this);
                    builder.setTitle("Perbarui Tarif Konsultasi");
                    EditText editText = new EditText(this);
                    editText.setText(model.getPrice());
                    editText.setRawInputType(InputType.TYPE_CLASS_NUMBER);
                    builder.setView(editText);
                    builder.setPositiveButton("YA", (dialog, i) -> {
                        if(editText.getText().toString().trim().isEmpty()) {
                            Toast.makeText(ConsultationWithDoctorDetailActivity.this, "Kolom tidak boleh kosong", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        updatePriceConsultation(editText.getText().toString().trim());
                    });
                    builder.setNegativeButton("TIDAK",null);
                    builder.show();
                });

                // SEMBUNYIKAN TOMBOL PEMBAYARAN & REKOMENDASI
                binding.paymentBtn.setVisibility(View.INVISIBLE);
                binding.likeBtn.setVisibility(View.INVISIBLE);
            }

        }

        // KLIK SUKAI
        binding.likeBtn.setOnClickListener(view -> {
            if(FirebaseAuth.getInstance().getCurrentUser() != null) {
                saveData(model.getUid());
            } else {
                binding.notLogin.setVisibility(View.VISIBLE);
                binding.detailConsult.setVisibility(View.GONE);
            }
        });

        // LOGIN BUTTON
        binding.loginBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        // KLIK KEMBALI KE HALAMAN SEBELUMNYA
        binding.backButton.setOnClickListener(view -> {
            onBackPressed();
        });

    }

    @SuppressLint({"CommitPrefEdits", "SetTextI18n"})
    private void saveData(String doctorUid) {
        checkIfLikedDoctor(doctorUid);

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        SharedPreferences pref = this.getPreferences(Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = pref.edit();
        if(isFavorite) {
            editor.putBoolean("status_"+uid+doctorUid, false);
            editor.apply();
            binding.likeBtn.setText("Saya merekomendasikan dokter ini");
            binding.likeBtn.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.background));

            like -= 1;
        } else {
            editor.putBoolean("status_"+uid+doctorUid, true);
            editor.apply();
            binding.likeBtn.setText("Saya tidak merekomendasikan dokter ini");
            binding.likeBtn.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.grey));

            like += 1;
        }
        likedOrNot(String.valueOf(like));
        binding.like.setText(like + " Orang telah merekomendasikan" + model.getName());
    }

    @SuppressLint("SetTextI18n")
    private void checkIfLikedDoctor(String doctorUid) {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        SharedPreferences pref = this.getPreferences(Context.MODE_PRIVATE);
        boolean value = pref.getBoolean("status_" + uid + doctorUid, false);

        Log.e("TAG", "status_" + uid + doctorUid);

        if(value) {
            isFavorite = true;
            binding.likeBtn.setText("Saya tidak merekomendasikan dokter ini");
            binding.likeBtn.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.grey));
        } else {
            isFavorite = false;
            binding.likeBtn.setText("Saya merekomendasikan dokter ini");
            binding.likeBtn.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.background));
        }
    }

    private void likedOrNot(String like) {
        FirebaseFirestore
                .getInstance()
                .collection("doctor")
                .document(model.getUid())
                .update("like", like);
    }


    private void updatePriceConsultation(String price) {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore
                .getInstance()
                .collection("doctor")
                .document(uid)
                .update("price", price)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(ConsultationWithDoctorDetailActivity.this, "Berhasil memperbarui tarif konsultasi", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ConsultationWithDoctorDetailActivity.this, "Gagal memperbarui tarif konsultasi", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void clickConsultation() {
        binding.paymentBtn.setOnClickListener(view -> {
            if(FirebaseAuth.getInstance().getCurrentUser() != null) {

                // HANYA USER BIASA YANG DAPAT MELAKSANAKAN KONSULTASI, DOKTER TIDAK BISA KONSULTASI DENGAN DOKTER
                FirebaseFirestore
                        .getInstance()
                        .collection("doctor")
                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .get()
                        .addOnSuccessListener(documentSnapshot -> {
                           if(documentSnapshot.exists()) {
                               showAlertDialog();
                           } else {
                               Intent intent = new Intent(this, ConsultationPaymentActivity.class);
                               intent.putExtra(ConsultationPaymentActivity.EXTRA_CONSULTATION, model);
                               startActivity(intent);
                           }
                        });



            } else {
                binding.notLogin.setVisibility(View.VISIBLE);
                binding.detailConsult.setVisibility(View.GONE);
            }
        });
    }

    private void showAlertDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Pemberitahuan")
                .setMessage("Anda berstatus sebagai Dokter saat ini.\n\nHanya pengguna yang bukan berstatus dokter yang dapat melakukan konsultasi, silahkan mendaftar akun baru")
                .setIcon(R.drawable.ic_baseline_warning_amber_24)
                .setCancelable(false)
                .setPositiveButton("YA", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                })
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}