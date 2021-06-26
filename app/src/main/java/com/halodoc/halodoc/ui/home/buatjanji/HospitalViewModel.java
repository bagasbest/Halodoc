package com.halodoc.halodoc.ui.home.buatjanji;


import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class HospitalViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<HospitalModel>> listBuatJanaji = new MutableLiveData<>();
    final ArrayList<HospitalModel> hospitalModelArrayList = new ArrayList<>();

    private static final String TAG = HospitalViewModel.class.getSimpleName();

    public void setHospitalBySpecialist(String specialist) {
        hospitalModelArrayList.clear();

        try {
            FirebaseFirestore
                    .getInstance()
                    .collection("hospital")
                    .whereArrayContains("services", specialist)
                    .get()
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot document : task.getResult()) {
                                HospitalModel hospitalModel = new HospitalModel();
                                hospitalModel.setName("" + document.get("title"));
                                hospitalModel.setType("" + document.get("type"));
                                hospitalModel.setLocation("" + document.get("location"));
                                hospitalModel.setDp("" + document.get("dp"));
                                hospitalModel.setUid("" + document.get("uid"));
                                List<String> group = (List<String>) document.get("services");
                                hospitalModel.setServices(group);


                                hospitalModelArrayList.add(hospitalModel);
                            }
                            listBuatJanaji.postValue(hospitalModelArrayList);
                        } else {
                            Log.e(TAG, task.toString());
                        }
                    });
        } catch (Exception error) {
            error.printStackTrace();
        }
    }

    public void setHospitalByAllSpecialist() {
        hospitalModelArrayList.clear();

        try {
            FirebaseFirestore
                    .getInstance()
                    .collection("hospital")
                    .get()
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot document : task.getResult()) {
                                HospitalModel hospitalModel = new HospitalModel();
                                hospitalModel.setName("" + document.get("title"));
                                hospitalModel.setType("" + document.get("type"));
                                hospitalModel.setLocation("" + document.get("location"));
                                hospitalModel.setDp("" + document.get("dp"));
                                hospitalModel.setUid("" + document.get("uid"));
                                List<String> group = (List<String>) document.get("services");
                                hospitalModel.setServices(group);


                                hospitalModelArrayList.add(hospitalModel);
                            }
                            listBuatJanaji.postValue(hospitalModelArrayList);
                        } else {
                            Log.e(TAG, task.toString());
                        }
                    });
        } catch (Exception error) {
            error.printStackTrace();
        }
    }

    public LiveData<ArrayList<HospitalModel>> getHospital() {
        return listBuatJanaji;
    }


}
