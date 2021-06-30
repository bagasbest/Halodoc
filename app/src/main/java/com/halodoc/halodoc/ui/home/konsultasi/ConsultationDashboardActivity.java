package com.halodoc.halodoc.ui.home.konsultasi;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.halodoc.halodoc.LoginActivity;
import com.halodoc.halodoc.R;
import com.halodoc.halodoc.databinding.ActivityConsultationDashboardBinding;

public class ConsultationDashboardActivity extends AppCompatActivity {

    private ActivityConsultationDashboardBinding binding;
    private FirebaseUser user;

    @Override
    protected void onResume() {
        super.onResume();
        user = FirebaseAuth.getInstance().getCurrentUser();
        checkIsLoginOrNot();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConsultationDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // MENJADI DOKTER KONSULTASI
        binding.becomeDoctor.setOnClickListener(view -> {
            if(user != null) {
                checkIsUserAlreadyRegisteredAsDoctorOrNot();
            }
            else {
                binding.notLogin.setVisibility(View.VISIBLE);
                binding.constraintLayout.setVisibility(View.GONE);
            }
        });

        // KONSULTASI DENGAN DOKTER
        binding.consultWithDoctor.setOnClickListener(view -> {
            startActivity(new Intent(this, ConsultationWithDoctorActivity.class));
        });

        // VERIFIKASI PENDAFTARAN DOKTER
        binding.verified.setOnClickListener(view -> {
            startActivity(new Intent(this, VerifiedDoctorActivity.class));
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

    private void checkIsLoginOrNot() {

        if(user != null) {
            checkIsAdminOrNot();
        } else {
            binding.verified.setVisibility(View.GONE);
        }

    }

    private void checkIsAdminOrNot() {
        FirebaseFirestore
                .getInstance()
                .collection("users")
                .document(user.getUid())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                   if(("" + documentSnapshot.get("role")).equals("admin")) {
                       binding.verified.setVisibility(View.VISIBLE);
                   }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(ConsultationDashboardActivity.this, "Gagal mengambil role", Toast.LENGTH_SHORT).show();
                });
    }

    private void checkIsUserAlreadyRegisteredAsDoctorOrNot() {
        FirebaseFirestore
                .getInstance()
                .collection("doctor")
                .document(user.getUid())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if(documentSnapshot.exists()) {
                        showAlertDialog();
                    } else {
                        startActivity(new Intent(ConsultationDashboardActivity.this, AddDoctorActivity.class));
                    }
                });
    }

    private void showAlertDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Anda sudah terdaftar menjadi Dokter")
                .setMessage("Anda hanya dapat mendaftar menjadi dokter konsultasi sekali saja.\n\nJika anda belum dapat praktik di Halodoc, silahkan menunggu hingga admin memverifikasi berkas pendaftaran anda")
                .setIcon(R.drawable.check)
                .setCancelable(false)
                .setPositiveButton("OKE", (dialogInterface, i) -> {
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