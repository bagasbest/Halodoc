package com.halodoc.halodoc.ui.transaction;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.halodoc.halodoc.ui.home.buatjanji.HospitalModel;
import com.halodoc.halodoc.ui.home.buatjanji.HospitalViewModel;

import java.util.ArrayList;
import java.util.List;

public class TransactionViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<TransactionModel>> listTransaction = new MutableLiveData<>();
    final ArrayList<TransactionModel> transactionModelArrayList = new ArrayList<>();

    private static final String TAG = TransactionViewModel.class.getSimpleName();

    public void setTransactionByUid(String uid) {
        transactionModelArrayList.clear();

        try {
            FirebaseFirestore
                    .getInstance()
                    .collection("transaction")
                    .whereEqualTo("userUid", uid)
                    .get()
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot document : task.getResult()) {
                                TransactionModel model = new TransactionModel();
                                model.setBookingDate("" + document.get("bookingDate"));
                                model.setDp("" + document.get("dp"));
                                model.setName("" + document.get("name"));
                                model.setNotes("" + document.get("notes"));
                                model.setPrice("" + document.get("price"));
                                model.setProofPayment(""+ document.get("proofPayment"));
                                model.setServices("" + document.get("services"));
                                model.setStatus("" + document.get("status"));
                                model.setTransactionType("" + document.get("transactionType"));
                                model.setUid("" + document.get("uid"));
                                model.setUserUid("" + document.get("userUid"));

                                transactionModelArrayList.add(model);
                            }
                            listTransaction.postValue(transactionModelArrayList);
                        } else {
                            Log.e(TAG, task.toString());
                        }
                    });
        } catch (Exception error) {
            error.printStackTrace();
        }
    }

    public LiveData<ArrayList<TransactionModel>> getTransaction() {
        return listTransaction;
    }

}
