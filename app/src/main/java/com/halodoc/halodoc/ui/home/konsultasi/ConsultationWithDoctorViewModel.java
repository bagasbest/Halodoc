package com.halodoc.halodoc.ui.home.konsultasi;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;

public class ConsultationWithDoctorViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<ConsultationWithDoctorModel>> listDoctor = new MutableLiveData<>();
    final ArrayList<ConsultationWithDoctorModel> consultationWithDoctorModelArrayList = new ArrayList<>();

    private static final String TAG = ConsultationWithDoctorViewModel.class.getSimpleName();

    public void setDoctorBySpecialist(String specialist) {
        consultationWithDoctorModelArrayList.clear();

        try {
            FirebaseFirestore
                    .getInstance()
                    .collection("doctor")
                    .whereEqualTo("sertifikatKeahlian", specialist)
                    .get()
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot document : task.getResult()) {
                                ConsultationWithDoctorModel model = new ConsultationWithDoctorModel();

                                model.setName("" + document.get("name"));
                                model.setDescription("" + document.get("description"));
                                model.setSertifikatKeahlian("" + document.get("sertifikatKeahlian"));
                                model.setExperience("" + document.get("experience"));
                                model.setPhone("" + document.get("phone"));
                                model.setDp("" + document.get("dp"));
                                model.setPractice("" + document.get("practice"));
                                model.setSpecialist("" + document.get("specialist"));
                                model.setLike("" + document.get("like"));
                                model.setPrice("" + document.get("price"));
                                model.setUid("" + document.get("uid"));
                                model.setRole("" + document.get("role"));

                                consultationWithDoctorModelArrayList.add(model);
                            }
                            listDoctor.postValue(consultationWithDoctorModelArrayList);
                        } else {
                            Log.e(TAG, task.toString());
                        }
                    });
        } catch (Exception error) {
            error.printStackTrace();
        }
    }

    public void setAllDoctor() {
        consultationWithDoctorModelArrayList.clear();

        try {
            FirebaseFirestore
                    .getInstance()
                    .collection("doctor")
                    .get()
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot document : task.getResult()) {
                                ConsultationWithDoctorModel model = new ConsultationWithDoctorModel();

                                model.setName("" + document.get("name"));
                                model.setDescription("" + document.get("description"));
                                model.setSertifikatKeahlian("" + document.get("sertifikatKeahlian"));
                                model.setExperience("" + document.get("experience"));
                                model.setPhone("" + document.get("phone"));
                                model.setDp("" + document.get("dp"));
                                model.setPractice("" + document.get("practice"));
                                model.setSpecialist("" + document.get("specialist"));
                                model.setLike("" + document.get("like"));
                                model.setPrice("" + document.get("price"));
                                model.setUid("" + document.get("uid"));
                                model.setRole("" + document.get("role"));

                                consultationWithDoctorModelArrayList.add(model);
                            }
                            listDoctor.postValue(consultationWithDoctorModelArrayList);
                        } else {
                            Log.e(TAG, task.toString());
                        }
                    });
        } catch (Exception error) {
            error.printStackTrace();
        }
    }

    public void setUnVerifiedDoctor() {
        consultationWithDoctorModelArrayList.clear();

        try {
            FirebaseFirestore
                    .getInstance()
                    .collection("doctor")
                    .whereEqualTo("role", "waiting")
                    .get()
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot document : task.getResult()) {
                                ConsultationWithDoctorModel model = new ConsultationWithDoctorModel();

                                model.setName("" + document.get("name"));
                                model.setDescription("" + document.get("description"));
                                model.setSertifikatKeahlian("" + document.get("sertifikatKeahlian"));
                                model.setExperience("" + document.get("experience"));
                                model.setPhone("" + document.get("phone"));
                                model.setDp("" + document.get("dp"));
                                model.setPractice("" + document.get("practice"));
                                model.setSpecialist("" + document.get("specialist"));
                                model.setLike("" + document.get("like"));
                                model.setPrice("" + document.get("price"));
                                model.setUid("" + document.get("uid"));
                                model.setRole("" + document.get("role"));

                                consultationWithDoctorModelArrayList.add(model);
                            }
                            listDoctor.postValue(consultationWithDoctorModelArrayList);
                        } else {
                            Log.e(TAG, task.toString());
                        }
                    });
        } catch (Exception error) {
            error.printStackTrace();
        }
    }

    public LiveData<ArrayList<ConsultationWithDoctorModel>> getDoctor() {
        return listDoctor;
    }

}
