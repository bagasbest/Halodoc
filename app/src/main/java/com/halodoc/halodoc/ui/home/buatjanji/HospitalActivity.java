package com.halodoc.halodoc.ui.home.buatjanji;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.halodoc.halodoc.R;
import com.halodoc.halodoc.databinding.ActivityHospitalBinding;

public class HospitalActivity extends AppCompatActivity {

    private ActivityHospitalBinding binding;
    private FirebaseUser user;
    private HospitalAdapter adapter;

    @Override
    protected void onResume() {
        super.onResume();
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHospitalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // TAMPILKAN SEMUA RUMAH SAKIT YANG TERSEDIA
        initRecyclerView();
        initViewModel("all");

        // PILIH SPESIALIS
        selectSpecialist();

        // KEMBALI KE HALAMAN SEBELUMNYA
        backButton();
    }

    private void backButton() {
        binding.backButton.setOnClickListener(view -> onBackPressed());
    }

    private void selectSpecialist() {
        //tampilkan kecamatan
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.specialist, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        binding.specialist.setAdapter(adapter);
        binding.specialist.setOnItemClickListener((adapterView, view, i, l) -> {
            initRecyclerView();
            initViewModel(binding.specialist.getText().toString());
        });
    }

    private void initRecyclerView() {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new HospitalAdapter();
        adapter.notifyDataSetChanged();
        binding.recyclerView.setAdapter(adapter);
    }

    private void initViewModel(String specialist) {
        // tampilkan daftar belanjaan di Halaman Order/Payment
        HospitalViewModel viewModel = new ViewModelProvider(this).get(HospitalViewModel.class);


        if (specialist.equals("all")) {
            viewModel.setHospitalByAllSpecialist();
        } else {
            viewModel.setHospitalBySpecialist(specialist);
        }

        viewModel.getHospital().observe(this, hospital -> {
            if (hospital.size() > 0) {
                binding.noData.setVisibility(View.GONE);
                adapter.setData(hospital);
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