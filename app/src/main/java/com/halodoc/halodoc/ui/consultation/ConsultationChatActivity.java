package com.halodoc.halodoc.ui.consultation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.halodoc.halodoc.R;
import com.halodoc.halodoc.databinding.ActivityConsultationChatBinding;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ConsultationChatActivity extends AppCompatActivity {

    public static final String EXTRA_DOCTOR_DP = "dp";
    public static final String EXTRA_DOCTOR_NAME = "name";
    public static final String EXTRA_DOCTOR_UID = "uid";
    public static final String EXTRA_CUSTOMER_NAME = "name2";
    public static final String EXTRA_CUSTOMER_UID = "uid2";
    public static final String EXTRA_CUSTOMER_DP = "dp2";
    private ActivityConsultationChatBinding binding;
    private ConsultationChatAdapter adapter;
    private ConsultationChatViewModel viewModel;

    private String customerUid;
    private String doctorUid;
    private String currentUid;
    private String doctorName;
    private String customerName;
    private String doctorDp;
    private String customerDp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConsultationChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        customerUid = getIntent().getStringExtra(EXTRA_CUSTOMER_UID);
        doctorUid = getIntent().getStringExtra(EXTRA_DOCTOR_UID);
        doctorName = getIntent().getStringExtra(EXTRA_DOCTOR_NAME);
        customerName = getIntent().getStringExtra(EXTRA_CUSTOMER_NAME);
        doctorDp = getIntent().getStringExtra(EXTRA_DOCTOR_DP);
        customerDp = getIntent().getStringExtra(EXTRA_CUSTOMER_DP);

        binding.textView23.setText(doctorName);

        // LOAD CHAT HISTORY
        initRecyclerView();
        initViewModel();


        sendMessage(customerName, doctorName, customerUid, doctorUid, customerDp, doctorDp);
    }

    private void sendMessage(String customerName, String doctorName, String customerUid, String doctorUid, String customerDp, String doctorDp) {
        binding.send.setOnClickListener(view -> {
            String message = binding.messageEt.getText().toString().trim();
            if(message.isEmpty()){
                Toast.makeText(ConsultationChatActivity.this, "Pesan tidak boleh kosong", Toast.LENGTH_SHORT).show();
                return;
            }

            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat getDate = new SimpleDateFormat("dd MMM yyyy, HH:mm:ss");
            String format = getDate.format(new Date());

            ChatDatabase.sendChat(message, format, customerName, doctorName, customerUid, doctorUid, customerDp, doctorDp);
            binding.messageEt.getText().clear();

            // LOAD CHAT HISTORY
            initRecyclerView();
            initViewModel();

        });
    }

    private void initRecyclerView() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        if (currentUid.equals(customerUid)) {
            adapter = new ConsultationChatAdapter(customerUid);
        } else {
            adapter = new ConsultationChatAdapter(doctorUid);
        }
        binding.chatRv.setAdapter(adapter);
        binding.chatRv.setLayoutManager(linearLayoutManager);

    }

    private void initViewModel() {

        viewModel = new ViewModelProvider(this).get(ConsultationChatViewModel.class);

        if (currentUid.equals(customerUid)) {
            viewModel.setChatList(customerUid, doctorUid);
        } else {
            viewModel.setChatList(doctorUid, customerUid);
        }
        viewModel.getChatList().observe(this, chatList -> {
            if (chatList.size() > 0) {
                //binding.noData.setVisibility(View.GONE);
                adapter.setData(chatList);
            } else {
                // binding.noData.setVisibility(View.VISIBLE);
            }
            //  binding.progressBar.setVisibility(View.GONE);
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}