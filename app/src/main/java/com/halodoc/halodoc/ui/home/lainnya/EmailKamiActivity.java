package com.halodoc.halodoc.ui.home.lainnya;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.halodoc.halodoc.databinding.ActivityEmailKamiBinding;
import java.util.HashMap;
import java.util.Map;

public class EmailKamiActivity extends AppCompatActivity {

    private ActivityEmailKamiBinding binding;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEmailKamiBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.title.setText("Email Kami");

        binding.saveBtn.setOnClickListener(view -> {
            String keluhan = binding.keluhanEt.getText().toString().trim();

            if(keluhan.isEmpty()) {
                binding.keluhanEt.setError("Keluhan tidak boleh kosong");
                return;
            }

            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

            Map<Object, String> complint = new HashMap<>();
            complint.put("message", keluhan);
            complint.put("uid", uid);

            FirebaseFirestore
                    .getInstance()
                    .collection("complaint")
                    .document(uid)
                    .set(complint)
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()) {
                            Toast.makeText(EmailKamiActivity.this, "Berhasil menyampaikan komplain", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(EmailKamiActivity.this, "Gagal menyampaikan komplain", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}