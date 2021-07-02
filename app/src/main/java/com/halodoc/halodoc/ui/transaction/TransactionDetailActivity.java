package com.halodoc.halodoc.ui.transaction;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.halodoc.halodoc.R;
import com.halodoc.halodoc.databinding.ActivityTransactionDetailBinding;


public class TransactionDetailActivity extends AppCompatActivity {

    private ActivityTransactionDetailBinding binding;
    public static final String EXTRA_TRANSACTION = "transaction";
    private String uid;
    private String type;
    private TransactionModel model;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTransactionDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        model = getIntent().getParcelableExtra(EXTRA_TRANSACTION);
        uid = model.getUid();
        type = model.getTransactionType();

        Glide.with(this)
                .load(model.getDp())
                .into(binding.dp);

        binding.name.setText(model.getName());
        binding.services.setText("Pelayanan: " + model.getServices());
        binding.price.setText("Biaya: Rp." + model.getPrice());
        binding.bookingDate.setText("Dilaksanakan pada: " + model.getBookingDate());
        binding.status.setText("Status: " + model.getStatus());
        binding.notes.setText("Catatan: " + model.getNotes());

        // AMBIL BUKTI TRANSFER
        switch (model.getProofPayment()) {
            case "OVO" :
                binding.textView29.setText("Bukti Transfer: Menggunakan OVO");
                break;
            case "DANA" :
                binding.textView29.setText("Bukti Transfer: Menggunakan DANA");
                break;
            case "GO-PAY" :
                binding.textView29.setText("Bukti Transfer: Menggunakan GO-PAY");
                break;
            case "LINK-AJA" :
                binding.textView29.setText("Bukti Transfer: Menggunakan LINK-AJA");
                break;
            default:
                binding.textView29.setText("Bukti Transfer:");
                binding.roundedImageView2.setVisibility(View.VISIBLE);
                Glide.with(this)
                        .load(model.getProofPayment())
                        .into(binding.roundedImageView2);
                break;
        }



        if (model.getStatus().equals("Sudah Diverifikasi")) {
            binding.verifiedButton.setVisibility(View.INVISIBLE);
            binding.rejectButton.setVisibility(View.INVISIBLE);
        }


        // KLIK KEMBALI KE HALAMAN SEBELUMNYA
        binding.backButton.setOnClickListener(view -> {
            onBackPressed();
        });

        // KLIK VERIFIKASI
        binding.verifiedButton.setOnClickListener(view -> {
            showConfirmDialog("Verifikasi");
        });

        // KLIK TOLAK
        binding.rejectButton.setOnClickListener(view -> {
            showConfirmDialog("Menolak");
        });
    }

    private void showConfirmDialog(String option) {
        if (option.equals("Verifikasi")) {
            new AlertDialog.Builder(this)
                    .setTitle("Konfirmasi Verifikasi Transaksi")
                    .setMessage("Apakah kamu yakin ingin memverifikasi pengguna ?")
                    .setIcon(R.drawable.ic_baseline_warning_amber_24)
                    .setCancelable(false)
                    .setPositiveButton("YA", (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                        binding.progressBar.setVisibility(View.VISIBLE);
                        verifiedTransaction();
                    })
                    .setNegativeButton("TIDAK", null)
                    .show();
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("Konfirmasi Menolak Transaksi")
                    .setMessage("Apakah kamu yakin ingin menolak transaksi pengguna ?")
                    .setIcon(R.drawable.ic_baseline_warning_amber_24)
                    .setCancelable(false)
                    .setPositiveButton("YA", (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                        binding.progressBar.setVisibility(View.VISIBLE);
                        rejectTransaction();
                    })
                    .setNegativeButton("TIDAK", null)
                    .show();
        }

    }

    private void rejectTransaction() {
        FirebaseFirestore
                .getInstance()
                .collection("transaction")
                .document(uid)
                .delete()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // VERIFIKASI BUAT JANJI / KONSULTASI
                        deleteService();
                    } else {
                        binding.progressBar.setVisibility(View.GONE);
                        Toast.makeText(TransactionDetailActivity.this, "Gagal menolak transaksi pengguna", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void deleteService() {
        FirebaseFirestore
                .getInstance()
                .collection(type)
                .document(uid)
                .delete()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        binding.progressBar.setVisibility(View.GONE);
                        Toast.makeText(TransactionDetailActivity.this, "Berhasil menolak transaksi pengguna", Toast.LENGTH_SHORT).show();
                        binding.verifiedButton.setVisibility(View.INVISIBLE);
                        binding.rejectButton.setVisibility(View.INVISIBLE);
                    } else {
                        binding.progressBar.setVisibility(View.GONE);
                        Toast.makeText(TransactionDetailActivity.this, "Gagal menolak transaksi pengguna", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void verifiedTransaction() {
        FirebaseFirestore
                .getInstance()
                .collection("transaction")
                .document(uid)
                .update("status", "Sudah Diverifikasi")
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // VERIFIKASI BUAT JANJI / KONSULTASI
                        if (!model.getTransactionType().equals("market")) {
                            verificateServices();
                        } else {
                            binding.progressBar.setVisibility(View.GONE);
                            Toast.makeText(TransactionDetailActivity.this, "Berhasil memverifikasi transaksi pengguna", Toast.LENGTH_SHORT).show();
                            binding.verifiedButton.setVisibility(View.INVISIBLE);
                            binding.rejectButton.setVisibility(View.INVISIBLE);
                        }
                    } else {
                        binding.progressBar.setVisibility(View.GONE);
                        Toast.makeText(TransactionDetailActivity.this, "Gagal memverifikasi transaksi pengguna", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void verificateServices() {
        FirebaseFirestore
                .getInstance()
                .collection(type)
                .document(uid)
                .update("status", "Sudah Diverifikasi")
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        binding.progressBar.setVisibility(View.GONE);
                        Toast.makeText(TransactionDetailActivity.this, "Berhasil memverifikasi transaksi pengguna", Toast.LENGTH_SHORT).show();
                        binding.verifiedButton.setVisibility(View.INVISIBLE);
                        binding.rejectButton.setVisibility(View.INVISIBLE);
                    } else {
                        binding.progressBar.setVisibility(View.GONE);
                        Toast.makeText(TransactionDetailActivity.this, "Gagal memverifikasi transaksi pengguna", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}