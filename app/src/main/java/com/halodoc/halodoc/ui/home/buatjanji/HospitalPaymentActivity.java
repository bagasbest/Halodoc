package com.halodoc.halodoc.ui.home.buatjanji;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.halodoc.halodoc.R;
import com.halodoc.halodoc.databinding.ActivityHospitalPaymentBinding;

public class HospitalPaymentActivity extends AppCompatActivity {

    private ActivityHospitalPaymentBinding binding;
    public static final String EXTRA_HOSPITAL = "hospital";
    public static final String EXTRA_SERVICES = "services";
    public static final String EXTRA_BOOKING_DATE = "booking";
    public static final String EXTRA_NOTES = "notes";

    private static final int REQUEST_FROM_GALLERY_TO_SELF_PHOTO = 1001;


    private String name;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHospitalPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // dapatkan atribut dari rumah sakit yang di klik pengguna
        HospitalModel hospitalModel = getIntent().getParcelableExtra(EXTRA_HOSPITAL);
        name = hospitalModel.getName();
        uid = hospitalModel.getUid();

        binding.name.setText(name);
        binding.type.setText(hospitalModel.getType());

        Glide.with(this)
                .load(hospitalModel.getDp())
                .error(R.drawable.ic_error)
                .into(binding.dp);

        // KEMBALI KE HALAMAN SEBELUMNYA
        clickBack();


        // UNGGAH BUKTI TRANSFER PEMBAYARAN
        uploadPaymentProof();

    }

    private void uploadPaymentProof() {
        binding.imageHint.setOnClickListener(view -> {
            ImagePicker
                    .with(this)
                    .galleryOnly()
                    .compress(1024)
                    .maxResultSize(1080, 1080)
                    .start(REQUEST_FROM_GALLERY_TO_SELF_PHOTO);

            binding.progressBar.setVisibility(View.VISIBLE);
        });
    }

    private void clickBack() {
        binding.backButton.setOnClickListener(view -> {
            onBackPressed();
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK) {
           // ProfileDatabase.uploadImageToDatabase(data.getData(), getActivity(), user.getUid());
            Glide.with(this)
                    .load(data.getData())
                    .placeholder(R.drawable.ic_baseline_face_24)
                    .error(R.drawable.ic_baseline_face_24)
                    .into(binding.dp);
            binding.progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}