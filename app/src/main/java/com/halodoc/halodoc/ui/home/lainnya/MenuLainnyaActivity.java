package com.halodoc.halodoc.ui.home.lainnya;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.halodoc.halodoc.HomeActivity;
import com.halodoc.halodoc.LoginActivity;
import com.halodoc.halodoc.databinding.ActivityMenuLainnyaBinding;

public class MenuLainnyaActivity extends AppCompatActivity {

    private ActivityMenuLainnyaBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMenuLainnyaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // CEK APAKAH PENGGUNA SUDAH LOGIN ATAU BELUM
        checkIsUserLoginOrNot();


        // KLIK BACK BUTTON
        clickBackButton();

        // KLIK LOGOUT BUTTON
        clickLogoutButton();

        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            binding.iv1.setOnClickListener(view -> {
                Intent intent = new Intent(this, DevelopmentActivity.class);
                intent.putExtra(DevelopmentActivity.EXTRA_TITLE, "Paket Sehat");
                startActivity(intent);
            });

            binding.iv2.setOnClickListener(view -> {
                Intent intent = new Intent(this, LokasiTersimpanActivity.class);
                intent.putExtra(LokasiTersimpanActivity.EXTRA_TITLE, "Lokasi Tersimpan");
                startActivity(intent);
            });

            binding.iv3.setOnClickListener(view -> {
                Intent intent = new Intent(this, BantuanActivity.class);
                intent.putExtra(BantuanActivity.EXTRA_TITLE, "Bantuan");
                startActivity(intent);
            });

            binding.iv4.setOnClickListener(view -> {
                Intent intent = new Intent(this, DevelopmentActivity.class);
                intent.putExtra(DevelopmentActivity.EXTRA_TITLE, "Bahasa");
                startActivity(intent);
            });

            binding.iv5.setOnClickListener(view -> {
                Intent intent = new Intent(this, DevelopmentActivity.class);
                intent.putExtra(DevelopmentActivity.EXTRA_TITLE, "Metode Pembayaran");
                startActivity(intent);
            });
        }
    }

    private void checkIsUserLoginOrNot() {
        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            binding.logoutBtn.setVisibility(View.VISIBLE);
        } else {
            binding.logoutBtn.setVisibility(View.GONE);
        }
    }

    private void clickLogoutButton() {
       binding.logoutBtn.setOnClickListener(view -> {
           new AlertDialog.Builder(this)
                   .setTitle("Konfirmasi Logout")
                   .setMessage("Apakah anada yakin ingin keluar aplikasi ?")
                   .setPositiveButton("YA", (dialogInterface, i) -> {
                       // sign out dari firebase autentikasi
                       FirebaseAuth.getInstance().signOut();

                       // go to login activity
                       Intent intent = new Intent(this, LoginActivity.class);
                       intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                       dialogInterface.dismiss();
                       startActivity(intent);
                       finish();

                   })
                   .setNegativeButton("TIDAK", null)
                   .show();
       });
    }

    private void clickBackButton() {
        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuLainnyaActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
    }
}