package com.halodoc.halodoc.ui.buatjanji;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class BuatJanjiViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<BuatJanjiModel>> listBuatJanaji = new MutableLiveData<>();
    final ArrayList<BuatJanjiModel> buatJanjiModelArrayList = new ArrayList<>();

    private static final String TAG = BuatJanjiViewModel.class.getSimpleName();

    public void setDataByUserIdProgress(String userId) {
        buatJanjiModelArrayList.clear();

        try {
            FirebaseFirestore
                    .getInstance()
                    .collection("hospitalPromisePayment")
                    .whereEqualTo("userUid", userId)
                    .whereNotEqualTo("status", "Selesai")
                    .get()
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot document : task.getResult()) {
                                BuatJanjiModel model = new BuatJanjiModel();
                                model.setBookingDate("" + document.get("bookingDate"));
                                model.setHospitalUid("" + document.get("hospitalUid"));
                                model.setNotes("" + document.get("notes"));
                                model.setPaymentProof("" + document.get("paymentProof"));
                                model.setPrice("" + document.get("price"));
                                model.setService("" + document.get("services"));
                                model.setStatus("" + document.get("status"));
                                model.setUid("" + document.get("uid"));
                                model.setUserUid("" + document.get("userUid"));
                                model.setDp("" + document.get("dp"));
                                model.setName("" + document.get("name"));
                                model.setType("" + document.get("type"));
                                model.setLocation("" + document.get("location"));

                                buatJanjiModelArrayList.add(model);
                            }
                            listBuatJanaji.postValue(buatJanjiModelArrayList);
                        } else {
                            Log.e(TAG, task.toString());
                        }
                    });
        } catch (Exception error) {
            error.printStackTrace();
        }
    }

    public void setDataByUserIdDone(String userId) {
        buatJanjiModelArrayList.clear();

        try {
            FirebaseFirestore
                    .getInstance()
                    .collection("hospitalPromisePayment")
                    .whereEqualTo("userUid", userId)
                    .whereEqualTo("status", "Selesai")
                    .get()
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot document : task.getResult()) {
                                BuatJanjiModel model = new BuatJanjiModel();
                                model.setBookingDate("" + document.get("bookingDate"));
                                model.setHospitalUid("" + document.get("hospitalUid"));
                                model.setNotes("" + document.get("notes"));
                                model.setPaymentProof("" + document.get("paymentProof"));
                                model.setPrice("" + document.get("price"));
                                model.setService("" + document.get("services"));
                                model.setStatus("" + document.get("status"));
                                model.setUid("" + document.get("uid"));
                                model.setUserUid("" + document.get("userUid"));
                                model.setDp("" + document.get("dp"));
                                model.setName("" + document.get("name"));
                                model.setType("" + document.get("type"));
                                model.setLocation("" + document.get("location"));

                                buatJanjiModelArrayList.add(model);
                            }
                            listBuatJanaji.postValue(buatJanjiModelArrayList);
                        } else {
                            Log.e(TAG, task.toString());
                        }
                    });
        } catch (Exception error) {
            error.printStackTrace();
        }
    }

    public LiveData<ArrayList<BuatJanjiModel>> getData() {
        return listBuatJanaji;
    }

}