package com.halodoc.halodoc.ui.home.lainnya;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.halodoc.halodoc.R;
import com.halodoc.halodoc.databinding.ActivityDevelopmentBinding;

public class DevelopmentActivity extends AppCompatActivity {

    private ActivityDevelopmentBinding binding;
    public static final String EXTRA_TITLE = "title";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDevelopmentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.title.setText(getIntent().getStringExtra(EXTRA_TITLE));

        // KEMBALI KE HALAMAN SEBELUMNYA
        binding.back.setOnClickListener(view -> onBackPressed());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}