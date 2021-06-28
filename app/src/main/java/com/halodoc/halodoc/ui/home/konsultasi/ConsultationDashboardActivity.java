package com.halodoc.halodoc.ui.home.konsultasi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.halodoc.halodoc.R;
import com.halodoc.halodoc.databinding.ActivityConsultationDashboardBinding;

public class ConsultationDashboardActivity extends AppCompatActivity {

    private ActivityConsultationDashboardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConsultationDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // MENJADI DOKTER KONSULTASI
        binding.becomeDoctor.setOnClickListener(view -> {
            startActivity(new Intent(this, AddDoctorActivity.class));
        });

        // KONSULTASI DENGAN DOKTER
        binding.consultWithDoctor.setOnClickListener(view -> {
            startActivity(new Intent(this, ConsultationWithDoctorActivity.class));
        });

        // VERIFIKASI PENDAFTARAN DOKTER
        binding.verified.setOnClickListener(view -> {
            startActivity(new Intent(this, VerifiedDoctorActivity.class));
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}