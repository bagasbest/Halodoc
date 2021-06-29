package com.halodoc.halodoc.ui.consultation;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class ConsultationViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<ConsultationModel>> listConsultation = new MutableLiveData<>();
    final ArrayList<ConsultationModel> consultationModelArrayList = new ArrayList<>();

    private static final String TAG = ConsultationViewModel.class.getSimpleName();

    public void setUser(String userUid) {
        consultationModelArrayList.clear();

        try {
            FirebaseFirestore
                    .getInstance()
                    .collection("consultation")
                    .whereEqualTo("userUid", userUid)
                    .whereNotEqualTo("status", "Selesai")
                    .get()
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot document : task.getResult()) {

                                ConsultationModel model = new ConsultationModel();
                                model.setConsultationUid("" + document.get("uid"));
                                model.setDoctorDp("" + document.get("doctorDp"));
                                model.setDoctorName("" + document.get("doctorName"));
                                model.setDoctorUid("" + document.get("doctorUid"));
                                model.setPrice("" + document.get("price"));
                                model.setService("" + document.get("services"));
                                model.setStatus("" + document.get("status"));
                                model.setUserDp("" + document.get("customerDp"));
                                model.setUserName("" + document.get("customerName"));
                                model.setUserUid("" + document.get("customerUid"));

                                consultationModelArrayList.add(model);
                            }
                            listConsultation.postValue(consultationModelArrayList);
                        } else {
                            Log.e(TAG, task.toString());
                        }
                    });
        } catch (Exception error) {
            error.printStackTrace();
        }
    }

    public void setFinishConsultation(String userUid) {
        consultationModelArrayList.clear();

        try {
            FirebaseFirestore
                    .getInstance()
                    .collection("consultation")
                    .whereEqualTo("userUid", userUid)
                    .whereEqualTo("status", "Selesai")
                    .get()
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot document : task.getResult()) {

                                ConsultationModel model = new ConsultationModel();
                                model.setConsultationUid("" + document.get("uid"));
                                model.setDoctorDp("" + document.get("doctorDp"));
                                model.setDoctorName("" + document.get("doctorName"));
                                model.setDoctorUid("" + document.get("doctorUid"));
                                model.setPrice("" + document.get("price"));
                                model.setService("" + document.get("services"));
                                model.setStatus("" + document.get("status"));
                                model.setUserDp("" + document.get("customerDp"));
                                model.setUserName("" + document.get("customerName"));
                                model.setUserUid("" + document.get("customerUid"));

                                consultationModelArrayList.add(model);
                            }
                            listConsultation.postValue(consultationModelArrayList);
                        } else {
                            Log.e(TAG, task.toString());
                        }
                    });
        } catch (Exception error) {
            error.printStackTrace();
        }
    }

    public LiveData<ArrayList<ConsultationModel>> getConsultation() {
        return listConsultation;
    }

}