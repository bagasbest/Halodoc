package com.halodoc.halodoc.ui.home.tokokesehatan;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class TokoKesehatanViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<TokoKesehatanModel>> listTokoKesehatan = new MutableLiveData<>();
    final ArrayList<TokoKesehatanModel> tokoKesehatanModelArrayList = new ArrayList<>();

    private static final String TAG = TokoKesehatanViewModel.class.getSimpleName();

    public void setAllProduct() {
        tokoKesehatanModelArrayList.clear();

        try {
            FirebaseFirestore
                    .getInstance()
                    .collection("market")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                TokoKesehatanModel model = new TokoKesehatanModel();

                                model.setName("" + document.get("name"));
                                model.setPrice("" + document.get("price"));
                                model.setDescription("" + document.get("description"));
                                model.setDp("" + document.get("dp"));
                                model.setUid("" + document.get("uid"));
                                model.setType("" + document.get("type"));

                                tokoKesehatanModelArrayList.add(model);
                            }
                            listTokoKesehatan.postValue(tokoKesehatanModelArrayList);
                        } else {
                            Log.e(TAG, task.toString());
                        }
                    });
        } catch (Exception error) {
            error.printStackTrace();
        }
    }


    public void setProductByType(String type) {
        tokoKesehatanModelArrayList.clear();

        try {
            FirebaseFirestore
                    .getInstance()
                    .collection("market")
                    .whereEqualTo("type", type)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                TokoKesehatanModel model = new TokoKesehatanModel();

                                model.setName("" + document.get("name"));
                                model.setPrice("" + document.get("price"));
                                model.setDescription("" + document.get("description"));
                                model.setDp("" + document.get("dp"));
                                model.setUid("" + document.get("uid"));

                                tokoKesehatanModelArrayList.add(model);
                            }
                            listTokoKesehatan.postValue(tokoKesehatanModelArrayList);
                        } else {
                            Log.e(TAG, task.toString());
                        }
                    });
        } catch (Exception error) {
            error.printStackTrace();
        }
    }



    public LiveData<ArrayList<TokoKesehatanModel>> getTokoKesehatan() {
        return listTokoKesehatan;
    }


}
