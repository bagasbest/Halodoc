package com.halodoc.halodoc.ui.consultation;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.halodoc.halodoc.databinding.ActivityConsultationNoteBinding;

import java.util.HashMap;
import java.util.Map;

public class ConsultationNoteActivity extends AppCompatActivity {

    public static final String EXTRA_NOTE = "";
    private ActivityConsultationNoteBinding binding;
    private ConsultationModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConsultationNoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        model = getIntent().getParcelableExtra(EXTRA_NOTE);


        // KLIK KEMBALI KE HALAMAN SEBELUMNYA
        binding.backButton.setOnClickListener(view -> onBackPressed());

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // JIKA PENGGUNA SAAT INI DOTER, MAKA IA DAPAT MENULISKAN CATATAN DOKTER
        if(model.getDoctorUid().equals(uid)) {
            binding.sendNoteBtn.setVisibility(View.VISIBLE);
            binding.note.setEnabled(true);

            // AMBIL CATATAN
            getNote();


            // KLIK KIRIM CATATAN & REKOMENDASI OBAT
            binding.sendNoteBtn.setOnClickListener(view -> {
                String note = binding.note.getText().toString();

                if(note.isEmpty()) {
                    binding.note.setError("Catatan tidak boleh kosong");
                    return;
                }

                // KIRIM CATATAN KE PENGGUNA (KUSTOMER)
                sendNoteToUser(note);

            });

        } else {

            // JIKA PENGGUNA MERUPAKAN KUSTOMER YANG MENJALANI KONSULTASI, TAMPILKAN CATATAN
            getNote();

        }
    }

    private void sendNoteToUser(String note) {
        binding.progressBar.setVisibility(View.VISIBLE);
        Map<String, Object> notes = new HashMap<>();
        notes.put("note", note);

        FirebaseFirestore
                .getInstance()
                .collection("consultation")
                .document(model.getConsultationUid())
                .collection("doctorNote")
                .document(model.getDoctorUid())
                .set(notes)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        binding.progressBar.setVisibility(View.GONE);
                        Toast.makeText(ConsultationNoteActivity.this, "Sukses", Toast.LENGTH_SHORT).show();
                    } else {
                        binding.progressBar.setVisibility(View.GONE);
                        Toast.makeText(ConsultationNoteActivity.this, "Gagal", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getNote() {
        FirebaseFirestore
                .getInstance()
                .collection("consultation")
                .document(model.getConsultationUid())
                .collection("doctorNote")
                .document(model.getDoctorUid())
                .get()
                .addOnCompleteListener(task -> {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot.exists()) {
                        String note = "" + documentSnapshot.get("note");
                        binding.note.setText(note);
                    } else {
                        binding.note.setText("");
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}