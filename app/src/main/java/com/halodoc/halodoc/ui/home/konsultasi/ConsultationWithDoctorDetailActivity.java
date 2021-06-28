package com.halodoc.halodoc.ui.home.konsultasi;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.halodoc.halodoc.R;
import com.halodoc.halodoc.databinding.ActivityConsultationWithDoctorDetailBinding;
import com.halodoc.halodoc.ui.consultation.ConsultationPaymentActivity;

public class ConsultationWithDoctorDetailActivity extends AppCompatActivity {

    private ActivityConsultationWithDoctorDetailBinding binding;
    private ConsultationWithDoctorModel model;
    public static final String EXTRA_DOCTOR = "doctor";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConsultationWithDoctorDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // TAMPILKAN DETAIL INFORMASI DOKTER YANG INGIN DIVERIFIKASI
        model = getIntent().getParcelableExtra(EXTRA_DOCTOR);
        binding.name.setText(model.getName());
        binding.sertifikatKeahlian.setText(model.getSertifikatKeahlian());
        binding.pengalaman.setText("Telah bekerja selama: " + model.getExperience()  +" tahun");
        binding.price.setText("Biaya: Rp. " + model.getPrice());
        binding.like.setText(model.getLike() + " Orang telah merekomendasikan " + model.getName());
        binding.description.setText(model.getDescription());
        Glide.with(this)
                .load(model.getDp())
                .into(binding.dp);

        Glide.with(this)
                .load(model.getPractice())
                .into(binding.practice);

        Glide.with(this)
                .load(model.getSpecialist())
                .into(binding.specialist);

        // KLIK KONSULTASI
        clickConsultation();
    }

    private void clickConsultation() {
        binding.paymentBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, ConsultationPaymentActivity.class);
            intent.putExtra(ConsultationPaymentActivity.EXTRA_CONSULTATION, model);
            startActivity(intent);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}