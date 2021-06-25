package com.halodoc.halodoc;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.halodoc.halodoc.databinding.ActivityHomeBinding;
import com.halodoc.halodoc.ui.buatjanji.BuatJanjiFragment;
import com.halodoc.halodoc.ui.consultation.ConsultationFragment;
import com.halodoc.halodoc.ui.home.HomeFragment;
import com.halodoc.halodoc.ui.profile.ProfileFragment;
import com.halodoc.halodoc.ui.transaction.TransactionFragment;

public class HomeActivity extends AppCompatActivity {

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityHomeBinding binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // untuk mengganti halaman contoh: halaman Buat Janji -> halaman Konsultasi -> halaman Beranda -> halaman Transaksi -> halaman Profil
        navView.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = new BuatJanjiFragment();
            switch (item.getItemId()) {
                case R.id.navigation_buat_janji: {
                    navView.getMenu().findItem(R.id.navigation_buat_janji).setEnabled(false);
                    navView.getMenu().findItem(R.id.navigation_konsultasi).setEnabled(true);
                    navView.getMenu().findItem(R.id.navigation_home).setEnabled(true);
                    navView.getMenu().findItem(R.id.navigation_transaction).setEnabled(true);
                    navView.getMenu().findItem(R.id.navigation_profile).setEnabled(true);
                    selectedFragment = new BuatJanjiFragment();
                    break;
                }
                case R.id.navigation_konsultasi: {
                    navView.getMenu().findItem(R.id.navigation_buat_janji).setEnabled(true);
                    navView.getMenu().findItem(R.id.navigation_konsultasi).setEnabled(false);
                    navView.getMenu().findItem(R.id.navigation_home).setEnabled(true);
                    navView.getMenu().findItem(R.id.navigation_transaction).setEnabled(true);
                    navView.getMenu().findItem(R.id.navigation_profile).setEnabled(true);
                    selectedFragment = new ConsultationFragment();
                    break;
                }
                case R.id.navigation_home: {
                    navView.getMenu().findItem(R.id.navigation_buat_janji).setEnabled(true);
                    navView.getMenu().findItem(R.id.navigation_konsultasi).setEnabled(true);
                    navView.getMenu().findItem(R.id.navigation_home).setEnabled(false);
                    navView.getMenu().findItem(R.id.navigation_transaction).setEnabled(true);
                    navView.getMenu().findItem(R.id.navigation_profile).setEnabled(true);
                    selectedFragment = new HomeFragment();
                    break;
                }
                case R.id.navigation_transaction: {
                    navView.getMenu().findItem(R.id.navigation_buat_janji).setEnabled(true);
                    navView.getMenu().findItem(R.id.navigation_konsultasi).setEnabled(true);
                    navView.getMenu().findItem(R.id.navigation_home).setEnabled(true);
                    navView.getMenu().findItem(R.id.navigation_transaction).setEnabled(false);
                    navView.getMenu().findItem(R.id.navigation_profile).setEnabled(true);
                    selectedFragment = new TransactionFragment();
                    break;
                }
                case R.id.navigation_profile: {
                    navView.getMenu().findItem(R.id.navigation_buat_janji).setEnabled(true);
                    navView.getMenu().findItem(R.id.navigation_konsultasi).setEnabled(true);
                    navView.getMenu().findItem(R.id.navigation_home).setEnabled(true);
                    navView.getMenu().findItem(R.id.navigation_transaction).setEnabled(true);
                    navView.getMenu().findItem(R.id.navigation_profile).setEnabled(false);
                    selectedFragment = new ProfileFragment();
                    break;
                }
                default: {
                    navView.getMenu().findItem(R.id.navigation_buat_janji).setEnabled(false);
                }
            }
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container, selectedFragment);
            transaction.commit();

            return true;
        });
    }
}