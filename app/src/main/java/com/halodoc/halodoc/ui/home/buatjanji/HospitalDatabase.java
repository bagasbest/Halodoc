package com.halodoc.halodoc.ui.home.buatjanji;

import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class HospitalDatabase {

    public static String proofPayment;

    public static void uploadImageToDatabase(Uri data, HospitalPaymentActivity activity, ImageView imageHint) {

        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
        String imageFileName = "paymentProof/user_" + System.currentTimeMillis() + ".png";

        mStorageRef.child(imageFileName).putFile(data)
                .addOnSuccessListener(taskSnapshot ->
                        mStorageRef.child(imageFileName).getDownloadUrl()
                                .addOnSuccessListener(uri -> {
                                    proofPayment = uri.toString();
                                    imageHint.setVisibility(View.GONE);
                                })
                                .addOnFailureListener(e -> {
                                    imageHint.setVisibility(View.VISIBLE);
                                    Toast.makeText(activity, "Gagal mengunggah bukti pembayaran", Toast.LENGTH_SHORT).show();
                                    Log.d("proofPayment: ", e.toString());
                                }))
                .addOnFailureListener(e -> {
                    imageHint.setVisibility(View.VISIBLE);
                    Toast.makeText(activity, "Gagal mengunggah bukti pembayaran", Toast.LENGTH_SHORT).show();
                    Log.d("proofPayment: ", e.toString());
                });
    }
}
