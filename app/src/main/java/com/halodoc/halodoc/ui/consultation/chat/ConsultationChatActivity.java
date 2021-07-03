package com.halodoc.halodoc.ui.consultation.chat;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.halodoc.halodoc.R;
import com.halodoc.halodoc.databinding.ActivityConsultationChatBinding;
import com.halodoc.halodoc.ui.consultation.ConsultationMedRecActivity;
import com.halodoc.halodoc.ui.consultation.ConsultationModel;
import com.halodoc.halodoc.ui.consultation.ConsultationNoteActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ConsultationChatActivity extends AppCompatActivity {

    public static final String EXTRA_CONSULTATION = "consultation";
    private ActivityConsultationChatBinding binding;
    private ConsultationChatAdapter adapter;
    private ConsultationModel model;

    private String currentUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConsultationChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        model = getIntent().getParcelableExtra(EXTRA_CONSULTATION);
        String uid;
        String myUid;
        String name;
        String myName;
        String dp;
        String myDp;
        if(currentUid.equals(model.getUserUid())) {
            binding.textView23.setText(model.getDoctorName());

            uid = model.getDoctorUid();
            myUid = model.getUserUid();
            name = model.getDoctorName();
            myName = model.getUserName();
            dp = model.getDoctorDp();
            myDp = model.getUserDp();

        } else {
            binding.textView23.setText(model.getUserName());

            uid = model.getUserUid();
            myUid = model.getDoctorUid();
            name = model.getUserName();
            myName = model.getDoctorName();
            dp = model.getUserDp();
            myDp = model.getDoctorDp();
        }

        // LOAD CHAT HISTORY
        initRecyclerView();
        initViewModel();

        // KLIK KEMBALI KE HALAMAN SEBELUMNYA
        binding.backButton.setOnClickListener(view -> {
            onBackPressed();
        });

        // KLIK BERKAS
        binding.attach.setOnClickListener(view -> {
            showOptionDialog();
        });

        // CEK APAKAH KONSULTASI SUDAH SELESAI / BELUM
        checkConsultationStatus();

        sendMessage(uid, myUid, name, myName, dp, myDp);
    }

    private void checkConsultationStatus() {
        if(model.getStatus().equals("Selesai")) {
            binding.send.setEnabled(false);
            binding.messageEt.setEnabled(false);
        }
    }

    private void showOptionDialog() {
        String []options = {"Catatan Dokter", "Rekomendasi Obat", "Selesaikan Konsultasi"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pilihan");
        builder.setItems(options, (dialog, which) -> {
            if(which == 0) {

                // CATATAN
                dialog.dismiss();
               Intent intent = new Intent(ConsultationChatActivity.this, ConsultationNoteActivity.class);
               intent.putExtra(ConsultationNoteActivity.EXTRA_NOTE, model);
               startActivity(intent);
            }
            else if (which == 1) {

                // CATATAN & REKOMENDASI OBAT
                dialog.dismiss();
                Intent intent = new Intent(ConsultationChatActivity.this, ConsultationMedRecActivity.class);
                intent.putExtra(ConsultationMedRecActivity.EXTRA_MEDREC, model);
                startActivity(intent);
            }
            else if(which == 2) {

                // SELESAIKAN KONSULTASI
                dialog.dismiss();
                if(model.getDoctorUid().equals(currentUid)) {
                    Toast.makeText(ConsultationChatActivity.this, "Hanya kustomer yang dapat menyelesaikan konsultasi", Toast.LENGTH_SHORT).show();
                } else if(model.getStatus().equals("Selesai")) {
                    Toast.makeText(ConsultationChatActivity.this, "Konsultasi sudah diselesaikan", Toast.LENGTH_SHORT).show();
                } else {
                    showConfirmDialog();
                }
            }
        });
        builder.create().show();
    }

    private void showConfirmDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Konfirmasi Menyelesaikan Konsultasi")
                .setMessage("Apakah anda yakin ingin menyelesaikan konsultasi ?")
                .setIcon(R.drawable.ic_baseline_warning_amber_24)
                .setCancelable(false)
                .setPositiveButton("YA", (dialogInterface, i) -> {
                    dialogInterface.dismiss();

                    // SELESAIKAN KONSULTASI
                    FirebaseFirestore
                            .getInstance()
                            .collection("consultation")
                            .document(model.getConsultationUid())
                            .update("status", "Selesai")
                            .addOnCompleteListener(task -> {
                               if(task.isSuccessful()) {
                                   Toast.makeText(ConsultationChatActivity.this, "Konsultasi Selesai", Toast.LENGTH_SHORT).show();
                                   binding.send.setEnabled(false);
                                   binding.messageEt.setEnabled(false);
                               }
                            });
                })
                .setNegativeButton("Tidak", null)
                .show();
    }

    private void sendMessage(String uid, String myUid, String name, String myName, String dp, String myDp) {
        binding.send.setOnClickListener(view -> {
            String message = binding.messageEt.getText().toString().trim();
            if(message.isEmpty()){
                Toast.makeText(ConsultationChatActivity.this, "Pesan tidak boleh kosong", Toast.LENGTH_SHORT).show();
                return;
            }

            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat getDate = new SimpleDateFormat("dd MMM yyyy, HH:mm:ss");
            String format = getDate.format(new Date());

            ChatDatabase.sendChat(message, format, uid, myUid, name, myName, dp, myDp);
            binding.messageEt.getText().clear();

            // LOAD CHAT HISTORY
            initRecyclerView();
            initViewModel();

        });
    }

    private void initRecyclerView() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        binding.chatRv.setLayoutManager(linearLayoutManager);

        if (currentUid.equals(model.getUserUid())) {
            adapter = new ConsultationChatAdapter(model.getUserUid());
        } else {
            adapter = new ConsultationChatAdapter(model.getDoctorUid());
        }
        adapter.notifyDataSetChanged();
        binding.chatRv.setAdapter(adapter);

    }

    private void initViewModel() {

        ConsultationChatViewModel viewModel = new ViewModelProvider(this).get(ConsultationChatViewModel.class);

        if (currentUid.equals(model.getUserUid())) {
            viewModel.setChatList(model.getUserUid(), model.getDoctorUid());
        } else {
            viewModel.setChatList(model.getDoctorUid(), model.getUserUid());
        }
        viewModel.getChatList().observe(this, chatList -> {
            if (chatList != null) {
                adapter.setData(chatList);
            }
            binding.progressBar.setVisibility(View.GONE);
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}