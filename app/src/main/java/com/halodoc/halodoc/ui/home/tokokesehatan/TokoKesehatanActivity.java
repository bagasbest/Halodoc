package com.halodoc.halodoc.ui.home.tokokesehatan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import com.halodoc.halodoc.R;
import com.halodoc.halodoc.databinding.ActivityTokoKesehatanBinding;

public class TokoKesehatanActivity extends AppCompatActivity {

    private ActivityTokoKesehatanBinding binding;
    private TokoKesehatanAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTokoKesehatanBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initRecyclerView();
        initViewModel("all");

        //tampilkan kecamatan
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.jenis, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        binding.productType.setAdapter(adapter);
        binding.productType.setOnItemClickListener((adapterView, view, i, l) -> {
            initRecyclerView();
            initViewModel(binding.productType.getText().toString());
        });

        // KEMBALI KE HALAMAN SEBELUMNYA
        binding.backButton.setOnClickListener(view -> onBackPressed());

    }

    private void initRecyclerView() {
        // tampilkan daftar produk dari fitur Toko Kesehatan
        binding.rvMarket.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        adapter = new TokoKesehatanAdapter();
        binding.rvMarket.setAdapter(adapter);
    }

    private void initViewModel(String type) {
        TokoKesehatanViewModel viewModel = new ViewModelProvider(this).get(TokoKesehatanViewModel.class);
        binding.progressBar.setVisibility(View.VISIBLE);
        if(type.equals("all")) {
            viewModel.setAllProduct();
        } else {
            viewModel.setProductByType(type);
        }
        viewModel.getTokoKesehatan().observe(this, productList -> {
            if (productList.size() > 0) {
                binding.noData.setVisibility(View.GONE);
                adapter.setData(productList);
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