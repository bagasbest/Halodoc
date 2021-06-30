package com.halodoc.halodoc.ui.buatjanji;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.halodoc.halodoc.R;
import com.halodoc.halodoc.databinding.ActivityBuatJanjiDetailBinding;
import net.glxn.qrgen.android.QRCode;

public class BuatJanjiDetailActivity extends AppCompatActivity {

    private ActivityBuatJanjiDetailBinding binding;
    public static final String EXTRA_HOSPITAL = "hospital" ;
    private String name;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBuatJanjiDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // KLIK KEMBALI KE HALAMAN SEBELUMNYA
        binding.backButton.setOnClickListener(view -> {
            onBackPressed();
        });

        BuatJanjiModel model = getIntent().getParcelableExtra(EXTRA_HOSPITAL);

        Glide.with(this)
                .load(model.getDp())
                .into(binding.imageView8);

        binding.username.setText(setUsername(model.getUserUid()));
        binding.title.setText(model.getName());
        binding.type.setText(model.getType());
        binding.bookingDate.setText("Dilaksanakan pada: " + model.getBookingDate());
        binding.price.setText("Biaya: Rp." + model.getPrice());
        binding.service.setText("Layanan: " + model.getService());
        binding.notes.setText("Catatan: " + model.getNotes());

        // KLIK SELESAI
        binding.finishBtn.setOnClickListener(view -> {
            new AlertDialog.Builder(this)
                    .setTitle("Menyelesaikan Buat Janji RS")
                    .setMessage("Apakah kamu yakin ingin menyelesaikan Buat Janji RS ?")
                    .setIcon(R.drawable.ic_baseline_warning_amber_24)
                    .setCancelable(false)
                    .setPositiveButton("YA", (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                        binding.progressBar.setVisibility(View.VISIBLE);
                        finishTransaction(model.getUid(), model.getName());
                    })
                    .setNegativeButton("TIDAK", null)
                    .show();

        });

        if(model.getStatus().equals("Selesai")) {
            binding.finishBtn.setVisibility(View.INVISIBLE);
        }

    }

    private void finishTransaction(String uid, String name) {
        FirebaseFirestore
                .getInstance()
                .collection("hospitalPromisePayment")
                .document(uid)
                .update("status", "Selesai")
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        binding.finishBtn.setVisibility(View.INVISIBLE);
                        binding.progressBar.setVisibility(View.GONE);
                        Toast.makeText(BuatJanjiDetailActivity.this, "Anda menyelesaikan Buat Janji pada " + name, Toast.LENGTH_SHORT).show();
                    } else {
                        binding.progressBar.setVisibility(View.GONE);
                        Toast.makeText(BuatJanjiDetailActivity.this, "Anda gagal menyelesaikan Buat Janji pada " + name, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private String setUsername(String userUid) {
        FirebaseFirestore
                .getInstance()
                .collection("users")
                .document(userUid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                   name = "" + documentSnapshot.get("name");

                    // SET QR GENERATOR
                    try {
                        Bitmap bitmap = QRCode.from(name).bitmap();
                        binding.image.setImageBitmap(bitmap);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                });
        return name;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}