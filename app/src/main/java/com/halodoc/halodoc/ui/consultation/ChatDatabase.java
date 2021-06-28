package com.halodoc.halodoc.ui.consultation;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class ChatDatabase {

    public static void sendChat(String message, String format, String customerName, String doctorName, String customerUid, String doctorUid, String customerDp, String doctorDp) {

        Map<String, Object> logChat   = new HashMap<>();
        logChat.put("message", message);
        logChat.put("time", format);
        logChat.put("uid", customerUid);
        logChat.put("isText", false);

        FirebaseFirestore
                .getInstance()
                .collection("chat")
                .document(customerUid)
                .collection("listUser")
                .document(customerUid+doctorUid)
                .collection("logChat")
                .document(String.valueOf(System.currentTimeMillis()))
                .set(logChat)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        Log.d("Sender MSG", "success");
                    }else {
                        Log.d("Sender MSG", "fail");
                    }
                });

    }
}
