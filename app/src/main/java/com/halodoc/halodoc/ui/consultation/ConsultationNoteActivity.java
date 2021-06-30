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

        // JIKA PENGGUNA SAAT INI DOTER, MAKA IA DAPAT MENULISKAN CATATAN DAN REKOMENDASI OBAT
        if(model.getDoctorUid().equals(uid)) {
            binding.sendNoteBtn.setVisibility(View.VISIBLE);
            binding.note.setEnabled(true);
            binding.medRec.setEnabled(true);

            // AMBIL CATATAN
            getNote();


            // KLIK KIRIM CATATAN & REKOMENDASI OBAT
            binding.sendNoteBtn.setOnClickListener(view -> {
                String note = binding.note.getText().toString();
                String medRecommendation = binding.medRec.getText().toString();

                if(note.isEmpty()) {
                    binding.note.setError("Catatan tidak boleh kosong");
                    return;
                } else if(medRecommendation.isEmpty()) {
                    binding.medRec.setError("Rekomendasi Obat tidak boleh kosong");
                    return;
                }

                // KIRIM CATATAN KE PENGGUNA (KUSTOMER)
                sendNoteToUser(note, medRecommendation);

            });

        } else {

            // JIKA PENGGUNA MERUPAKAN KUSTOMER YANG MENJALANI KONSULTASI, TAMPILKAN CATATAN
            getNote();

        }
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
                        String medRec = "" + documentSnapshot.get("medRec");
                        binding.note.setText(note);
                        binding.medRec.setText(medRec);
                    } else {
                        binding.note.setText("");
                        binding.medRec.setText("");
                    }
                });
    }

    private void sendNoteToUser(String note, String medRecommendation) {

        binding.progressBar.setVisibility(View.VISIBLE);
        Map<String, Object> notes = new HashMap<>();
        notes.put("note", note);
        notes.put("medRec", medRecommendation);

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}