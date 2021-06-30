package com.halodoc.halodoc.ui.home.konsultasi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import com.google.firebase.auth.FirebaseAuth;
import com.halodoc.halodoc.R;
import com.halodoc.halodoc.databinding.ActivityConsultationWithDoctorBinding;

public class ConsultationWithDoctorActivity extends AppCompatActivity {

    private ActivityConsultationWithDoctorBinding binding;
    private ConsultationWithDoctorAdapter adapter;

    @Override
    protected void onResume() {
        super.onResume();
        // CEK APAKAH PENGGUNA SUDAH LOGIN APA BELUM
        checkIsUserLoginOrNot();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConsultationWithDoctorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        // PILIH SPESIALIS DOKTER UNTUK KONSULTASI
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.specialist, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        binding.specialistEt.setAdapter(adapter);
        binding.specialistEt.setOnItemClickListener((adapterView, view, i, l) -> {
            initRecyclerView();
            initViewModel(binding.specialistEt.getText().toString());
        });

        // KLIK KEMBALI KE HALAMAN SEBELUMNYA
        binding.backButton.setOnClickListener(view -> {
            onBackPressed();
        });
    }

    private void checkIsUserLoginOrNot() {
        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            // INISIASI LIST DOKTER KONSULTASI
            initRecyclerView();
            initViewModel("all");
        } else {
            initRecyclerView();
            initViewModel("public");
        }
    }

    private void initRecyclerView() {
        binding.rvDoctor.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ConsultationWithDoctorAdapter("consult");
        adapter.notifyDataSetChanged();
        binding.rvDoctor.setAdapter(adapter);
    }

    private void initViewModel(String specialist) {
        // tampilkan daftar dokter
        ConsultationWithDoctorViewModel viewModel = new ViewModelProvider(this).get(ConsultationWithDoctorViewModel.class);

        binding.progressBar.setVisibility(View.VISIBLE);
        if (specialist.equals("all")) {
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            viewModel.setAllDoctor(uid);
        } else if(specialist.equals("public")){
            viewModel.setAllDoctor(specialist);
        } else {
            if(FirebaseAuth.getInstance().getCurrentUser() != null) {
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                viewModel.setDoctorBySpecialist(specialist, uid);
            } else {
                viewModel.setDoctorBySpecialist(specialist, "public");
            }
        }

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