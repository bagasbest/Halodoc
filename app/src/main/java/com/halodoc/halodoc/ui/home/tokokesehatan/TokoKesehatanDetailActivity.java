package com.halodoc.halodoc.ui.home.tokokesehatan;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.halodoc.halodoc.databinding.ActivityTokoKesehatanDetailBinding;

public class TokoKesehatanDetailActivity extends AppCompatActivity {

    private ActivityTokoKesehatanDetailBinding binding;
    public static final String EXTRA_MARKET = "market";
    private TokoKesehatanModel model;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTokoKesehatanDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        model = getIntent().getParcelableExtra(EXTRA_MARKET);
        Glide.with(this)
                .load(model.getDp())
                .into(binding.dp);

        binding.name.setText(model.getName());
        binding.price.setText("Rp."+model.getPrice());
        binding.description.setText(model.getDescription());


        // KEMBALI KE HALAMAN SEBELUMNYA
        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        // KE HALAMAN SELANJUTNYA
        binding.nextBtn.setOnClickListener(view -> {
            String qty = binding.qtyEt.getText().toString().trim();
            if(qty.isEmpty()) {
                binding.qtyEt.setError("Mohon isi total pembelian");
                return;
            }

            Intent intent = new Intent(TokoKesehatanDetailActivity.this, TokoKesehatanPaymentActivity.class);
            intent.putExtra(TokoKesehatanPaymentActivity.EXTRA_PRICE, model.getPrice());
            intent.putExtra(TokoKesehatanPaymentActivity.EXTRA_TYPE, model.getType());
            intent.putExtra(TokoKesehatanPaymentActivity.EXTRA_QTY, qty);
            intent.putExtra(TokoKesehatanPaymentActivity.EXTRA_DP, model.getDp());
            intent.putExtra(TokoKesehatanPaymentActivity.EXTRA_NAME, model.getName());
            startActivity(intent);
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}