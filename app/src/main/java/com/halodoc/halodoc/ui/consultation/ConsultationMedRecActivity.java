package com.halodoc.halodoc.ui.consultation;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.halodoc.halodoc.databinding.ActivityConsultationMedRecBinding;

import java.util.HashMap;
import java.util.Map;

public class ConsultationMedRecActivity extends AppCompatActivity {

    public static final String EXTRA_MEDREC = "medrec";
    private ActivityConsultationMedRecBinding binding;
    private ConsultationModel model;
    private int qty;
    private int day;
    private int type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConsultationMedRecBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        model = getIntent().getParcelableExtra(EXTRA_MEDREC);

        // KEMBALI KE HALAMAN SEBELUMNYA
        binding.backButton.setOnClickListener(view -> onBackPressed());

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // JIKA PENGGUNA SAAT INI DOTER, MAKA IA DAPAT MENULISKAN REKOMENDASI OBAT
        if (model.getDoctorUid().equals(uid)) {
            binding.name.setEnabled(true);
            binding.minQty.setEnabled(true);
            binding.maxQty.setEnabled(true);
            binding.minDay.setEnabled(true);
            binding.maxDay.setEnabled(true);
            binding.minType.setEnabled(true);
            binding.maxtype.setEnabled(true);
            binding.medType.setEnabled(true);
            binding.sebelumMakan.setEnabled(true);
            binding.ketikaMakan.setEnabled(true);
            binding.sesudahMakan.setEnabled(true);
            binding.pagi.setEnabled(true);
            binding.siang.setEnabled(true);
            binding.sore.setEnabled(true);
            binding.notes.setEnabled(true);
            binding.saveBtn.setVisibility(View.VISIBLE);

            // AMBIL DATA REKOMENDASI OBAT
            getMedRec();

            // TAMBAHKAN KUANTITAS OBAT
            binding.maxQty.setOnClickListener(view -> {
                binding.qty.setText(String.valueOf(qty + 1));
                qty++;
            });

            // TAMBAHKAN PENGGUNAAN OBAT PERHARI
            binding.maxDay.setOnClickListener(view -> {
                binding.day.setText(String.valueOf(day + 1));
                day++;
            });

            // TAMBAHKAN JUMLAH PEMAKAIAN OBAT PER SATUAN WAKTU
            binding.maxtype.setOnClickListener(view -> {
                binding.type.setText(String.valueOf(type + 1));
                type++;
            });

            // KURANGKAN KUANTITAS
            binding.minQty.setOnClickListener(view -> {
                if (qty > 0) {
                    binding.qty.setText(String.valueOf(qty - 1));
                    qty--;
                }
            });


            // KURANGKAN PENGGUNAAN PERHARI
            binding.minDay.setOnClickListener(view -> {
                if (day > 0) {
                    binding.day.setText(String.valueOf(day - 1));
                    day--;
                }
            });



            // KURANGKAN JUMLAH PEMAKAIAN OBAT PER SATUAN WAKTU
            binding.minType.setOnClickListener(view -> {
                if (type > 0) {
                    binding.type.setText(String.valueOf(type - 1));
                    type--;
                }
            });


            // SIMPAN REKOMENDASI OBAT
            binding.saveBtn.setOnClickListener(view -> {
                verifiedForm();
            });
        } else {
            // JIKA PENGGUNA MERUPAKAN KUSTOMER YANG MENJALANI KONSULTASI, TAMPILKAN REKOMENDASI OBAT YANG DITULIS DOKTER
            getMedRec();
        }

    }

    @SuppressLint("SetTextI18n")
    private void getMedRec() {
        FirebaseFirestore
                .getInstance()
                .collection("consultation")
                .document(model.getConsultationUid())
                .collection("doctorMedRec")
                .document(model.getDoctorUid())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        qty = Integer.parseInt("" + documentSnapshot.get("qty"));
                        day = Integer.parseInt("" + documentSnapshot.get("day"));
                        type = Integer.parseInt("" + documentSnapshot.get("type"));

                        binding.name.setText("" + documentSnapshot.get("name"));
                        binding.notes.setText("" + documentSnapshot.get("notes"));
                        binding.medType.setText("" + documentSnapshot.get("medType"));
                        binding.qty.setText("" + qty);
                        binding.day.setText("" + day);
                        binding.type.setText("" + type);
                        binding.sebelumMakan.setChecked(documentSnapshot.getBoolean("before"));
                        binding.ketikaMakan.setChecked(documentSnapshot.getBoolean("going"));
                        binding.sesudahMakan.setChecked(documentSnapshot.getBoolean("after"));
                        binding.pagi.setChecked(documentSnapshot.getBoolean("morning"));
                        binding.siang.setChecked(documentSnapshot.getBoolean("afternoon"));
                        binding.sore.setChecked(documentSnapshot.getBoolean("evening"));
                    } else {
                        binding.name.setText("");
                        binding.notes.setText("");
                        binding.medType.setText("");
                        qty = 0;
                        day = 0;
                        type = 0;
                    }
                });
    }


    private void verifiedForm() {
        String qty = binding.qty.getText().toString();
        String day = binding.day.getText().toString();
        String type = binding.type.getText().toString();
        String medType = binding.medType.getText().toString().trim();
        String name = binding.name.getText().toString().trim();
        String notes = binding.notes.getText().toString().trim();
        boolean before = binding.sebelumMakan.isChecked();
        boolean going = binding.ketikaMakan.isChecked();
        boolean after = binding.sesudahMakan.isChecked();
        boolean morning = binding.pagi.isChecked();
        boolean afternoon = binding.siang.isChecked();
        boolean evening = binding.sore.isChecked();

        if (qty.equals("0")) {
            Toast.makeText(ConsultationMedRecActivity.this, "Kuantitas tidak boleh 0", Toast.LENGTH_SHORT).show();
            return;
        } else if (day.equals("0")) {
            Toast.makeText(ConsultationMedRecActivity.this, "Minimal 1 kali perharu", Toast.LENGTH_SHORT).show();
            return;
        } else if (type.equals("0")) {
            Toast.makeText(ConsultationMedRecActivity.this, "Minimal 1 Kapsul / Tetes / Pil", Toast.LENGTH_SHORT).show();
            return;
        } else if (medType.isEmpty()) {
            Toast.makeText(ConsultationMedRecActivity.this, "Silahkan isikan jenis: Kapsul / Tetes / Pil", Toast.LENGTH_SHORT).show();
            return;
        } else if (name.isEmpty()) {
            Toast.makeText(ConsultationMedRecActivity.this, "Nama Obat tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        } else if (notes.isEmpty()) {
            Toast.makeText(ConsultationMedRecActivity.this, "Catatan tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        } else if (!before && !going && !after) {
            Toast.makeText(ConsultationMedRecActivity.this, "Minimal 1 waktu: Sebelum makan, Ketika makan, atau Sesudah makan", Toast.LENGTH_SHORT).show();
            return;
        } else if (!morning && !afternoon && !evening) {
            Toast.makeText(ConsultationMedRecActivity.this, "Minimal 1 waktu: Pagi, Siang, atau Sore", Toast.LENGTH_SHORT).show();
            return;
        }


        binding.progressBar.setVisibility(View.VISIBLE);
        Map<String, Object> medRec = new HashMap<>();
        medRec.put("name", name);
        medRec.put("notes", notes);
        medRec.put("medType", medType);
        medRec.put("qty", qty);
        medRec.put("day", day);
        medRec.put("type", type);
        medRec.put("before", before);
        medRec.put("going", going);
        medRec.put("after", after);
        medRec.put("morning", morning);
        medRec.put("afternoon", afternoon);
        medRec.put("evening", evening);

        // SIMPAN DATA KE DATABASE
        FirebaseFirestore
                .getInstance()
                .collection("consultation")
                .document(model.getConsultationUid())
                .collection("doctorMedRec")
                .document(model.getDoctorUid())
                .set(medRec)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        binding.progressBar.setVisibility(View.GONE);
                        Toast.makeText(ConsultationMedRecActivity.this, "Sukses", Toast.LENGTH_SHORT).show();
                    } else {
                        binding.progressBar.setVisibility(View.GONE);
                        Toast.makeText(ConsultationMedRecActivity.this, "Gagal", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}