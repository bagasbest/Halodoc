package com.halodoc.halodoc.ui.consultation.chat;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class ConsultationChatViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<ConsultationChatModel>> listChat = new MutableLiveData<>();
    final ArrayList<ConsultationChatModel> chatModelArrayList = new ArrayList<>();

    private static final String TAG = ConsultationChatViewModel.class.getSimpleName();

    public void setChatList(String uid1, String uid2) {
        chatModelArrayList.clear();

        try {
            FirebaseFirestore
                    .getInstance()
                    .collection("chat")
                    .document(uid1)
                    .collection("listUser")
                    .document(uid1+uid2)
                    .collection("logChat")
                    .get()
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot document : task.getResult()) {

                                ConsultationChatModel model = new ConsultationChatModel();
                                model.setMessage("" + document.get("message"));
                                model.setTime("" + document.get("time"));
                                model.setUid("" + document.get("uid"));


                                chatModelArrayList.add(model);
                            }
                            listChat.postValue(chatModelArrayList);
                        } else {
                            Log.e(TAG, task.toString());
                        }
                    });
        } catch (Exception error) {
            error.printStackTrace();
        }
    }

    public LiveData<ArrayList<ConsultationChatModel>> getChatList() {
        return listChat;
    }

}
