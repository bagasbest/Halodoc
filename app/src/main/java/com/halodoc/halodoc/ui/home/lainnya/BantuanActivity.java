package com.halodoc.halodoc.ui.home.lainnya;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.halodoc.halodoc.databinding.ActivityBantuanBinding;

public class BantuanActivity extends AppCompatActivity {

    public static final String EXTRA_TITLE = "bantuan";
    private ActivityBantuanBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBantuanBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.title.setText(getIntent().getStringExtra(EXTRA_TITLE));

        binding.email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BantuanActivity.this, EmailKamiActivity.class));
            }
        });

        binding.telpon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = "081234567890";
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                startActivity(intent);
            }
        });

        binding.chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BantuanActivity.this, DevelopmentActivity.class));
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}