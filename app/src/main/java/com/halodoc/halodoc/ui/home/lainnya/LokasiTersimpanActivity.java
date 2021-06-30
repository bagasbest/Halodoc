package com.halodoc.halodoc.ui.home.lainnya;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.halodoc.halodoc.databinding.ActivityLokasiTersimpanBinding;


public class LokasiTersimpanActivity extends AppCompatActivity {

    public static final String EXTRA_TITLE = "";
    private ActivityLokasiTersimpanBinding binding;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLokasiTersimpanBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        binding.title.setText(getIntent().getStringExtra(EXTRA_TITLE));

        // AMBIL LOKASI
        getLoc();


        // SIMPAN LOKASI
        setLoc();

    }

    private void setLoc() {
        binding.saveBtn.setOnClickListener(view -> {
            String location = binding.locationEt.getText().toString().trim();

            if(location.isEmpty()) {
                binding.locationEt.setError("Lokasi tidak boleh kosong");
                return;
            }

            // SIMPAN LOKASI KE DATABASE
            FirebaseFirestore
                    .getInstance()
                    .collection("users")
                    .document(uid)
                    .update("location", location)
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()) {
                            Toast.makeText(LokasiTersimpanActivity.this, "Sukses menyimpan lokasi", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LokasiTersimpanActivity.this, "Gagal menyimpan lokasi", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

    @SuppressLint("SetTextI18n")
    private void getLoc() {
        // SIMPAN LOKASI KE DATABASE
        FirebaseFirestore
                .getInstance()
                .collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    binding.locationEt.setText("" + documentSnapshot.get("location"));
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}