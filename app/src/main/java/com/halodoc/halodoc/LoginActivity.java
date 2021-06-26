package com.halodoc.halodoc;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.halodoc.halodoc.databinding.ActivityLoginBinding;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    @Override
    protected void onStart() {
        super.onStart();
        // AUTO LOGIN KETIKA SUDAH PERNAH LOGIN SEBELUMNYA
        checkIfUserLoginBefore();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // KETIKA KLIK DAFTAR
        binding.registerTv.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));



        // KETIKA KLIK TOMBOL LOGIN, VERIFIKASI EMAIL & PASSWORD DULU, SETELAH ITU BARU LOGIN
        binding.loginBtn.setOnClickListener(view -> {
            String email = binding.emailEt.getText().toString().trim();
            String password = binding.passwordEt.getText().toString().trim();

            if (email.isEmpty()) {
                binding.emailEt.setError("Email tidak boleh kosong");
                return;
            } else if (!email.contains("@") || !email.contains(".")) {
                binding.emailEt.setError("Format email tidak sesuai");
                return;
            } else if (password.isEmpty()) {
                binding.passwordEt.setError("Password tidak boleh kosong");
                return;
            }


            //LOGIN
            binding.progressBar.setVisibility(View.VISIBLE);
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            binding.progressBar.setVisibility(View.GONE);
                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                            finish();
                        } else {
                            binding.progressBar.setVisibility(View.GONE);
                            Log.e("Error Login", Objects.requireNonNull(task.getException()).toString());
                            Toast.makeText(LoginActivity.this, "Ups, terdapat kendala ketika ingin login", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

    }

    private void checkIfUserLoginBefore() {
        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}