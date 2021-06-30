package com.halodoc.halodoc.ui.consultation.chat;

import android.util.Log;


import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class ChatDatabase {

    public static void sendChat(String message, String format, String uid, String myUid, String name, String myName, String dp, String myDp) {

        Map<String, Object> lastUpdateMessageToSender = new HashMap<>();
        lastUpdateMessageToSender.put("dp", dp);
        lastUpdateMessageToSender.put("message", message);
        lastUpdateMessageToSender.put("time", format);
        lastUpdateMessageToSender.put("name", name);
        lastUpdateMessageToSender.put("uid", uid);

        Map<String, Object> lastUpdateMessageToReceiver = new HashMap<>();
        lastUpdateMessageToSender.put("dp", myDp);
        lastUpdateMessageToSender.put("message", message);
        lastUpdateMessageToSender.put("time", format);
        lastUpdateMessageToSender.put("name", myName);
        lastUpdateMessageToSender.put("uid", myUid);

        Map<String, Object> logChat   = new HashMap<>();
        logChat.put("message", message);
        logChat.put("time", format);
        logChat.put("uid", myUid);
        logChat.put("isText", false);


        // UPDATE PESAN TERAKHIR (SISI PENGIRIM)
        FirebaseFirestore
                .getInstance()
                .collection("chat")
                .document(myUid)
                .collection("listUser")
                .document(myUid+uid)
                .set(lastUpdateMessageToSender)
                .addOnCompleteListener(task -> {
                        if(task.isSuccessful()) {
                            Log.d("SENDER SIDE", "sukses");
                        } else {
                            Log.d("SENDER SIDE", task.toString());
                        }
                });

        // UPDATE PESAN TERAKHIR (SISI PENERIMA)
        FirebaseFirestore
                .getInstance()
                .collection("chat")
                .document(uid)
                .collection("listUser")
                .document(uid+myUid)
                .set(lastUpdateMessageToReceiver)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        Log.d("RECEIVER SIDE", "sukses");
                    } else {
                        Log.d("RECEIVER SIDE", task.toString());
                    }
                });

        // UPDATE LOG CHAT TERAKHIR (SISI PENGIRIM)
        FirebaseFirestore
                .getInstance()
                .collection("chat")
                .document(myUid)
                .collection("listUser")
                .document(myUid+uid)
                .collection("logChat")
                .document(String.valueOf(System.currentTimeMillis()))
                .set(logChat)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        Log.d("SENDER MSG", "success");
                    }else {
                        Log.d("SENDER MSG", task.toString());
                    }
                });

        // UPDATE LOG CHAT TERAKHIR (SISI PENERIMA)
        FirebaseFirestore
                .getInstance()
                .collection("chat")
                .document(uid)
                .collection("listUser")
                .document(uid+myUid)
                .collection("logChat")
                .document(String.valueOf(System.currentTimeMillis()))
                .set(logChat)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        Log.d("RECEIVER MSG", "success");
                    }else {
                        Log.d("RECEIVER MSG", task.toString());
                    }
                });

    }
}
