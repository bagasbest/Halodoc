package com.halodoc.halodoc.ui.home.konsultasi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.halodoc.halodoc.R;
import com.halodoc.halodoc.databinding.ActivityVerifiedDoctorBinding;

public class VerifiedDoctorActivity extends AppCompatActivity {

    private ActivityVerifiedDoctorBinding binding;
    private ConsultationWithDoctorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVerifiedDoctorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // TAMPILKAN LIST DOKTER YANG INGIN DIVERIFIKASI
        initRecyclerView();
        initViewModel();
    }

    private void initRecyclerView() {
        binding.rvVerified.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ConsultationWithDoctorAdapter("verif");
        adapter.notifyDataSetChanged();
        binding.rvVerified.setAdapter(adapter);
    }

    private void initViewModel() {
        // tampilkan daftar belanjaan di Halaman Order/Payment
        ConsultationWithDoctorViewModel viewModel = new ViewModelProvider(this).get(ConsultationWithDoctorViewModel.class);


        viewModel.setUnVerifiedDoctor();
        viewModel.getDoctor().observe(this, doctor -> {
            if (doctor.size() > 0) {
                binding.noData.setVisibility(View.GONE);
                adapter.setData(doctor);
            } else {
                binding.noData.setVisibility(View.VISIBLE);
            }
            binding.progressBar.setVisibility(View.GONE);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}