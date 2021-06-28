package com.halodoc.halodoc.ui.home.konsultasi;

import android.app.ProgressDialog;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class AddDoctorDatabase {

    public static String formal;
    public static String practice;
    public static String specialist;

    public static void uploadFormalPicture(AddDoctorActivity activity, Uri data, String option) {
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
        ProgressDialog mProgressDialog = new ProgressDialog(activity);

        mProgressDialog.setMessage("Mohon tunggu hingga proses selesai...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
        String imageFileName = "doctor/" + option +"/data_" + System.currentTimeMillis() + ".png";

        mStorageRef.child(imageFileName).putFile(data)
                .addOnSuccessListener(taskSnapshot ->
                        mStorageRef.child(imageFileName).getDownloadUrl()
                                .addOnSuccessListener(uri -> {
                                    mProgressDialog.dismiss();
                                    switch (option) {
                                        case "formal":
                                            formal = uri.toString();
                                            break;
                                        case "practice":
                                            practice = uri.toString();
                                            break;
                                        case "specialist":
                                            specialist = uri.toString();
                                            break;
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    mProgressDialog.dismiss();
                                    Toast.makeText(activity, "Gagal memperbarui profil", Toast.LENGTH_SHORT).show();
                                    Log.d("userDp: ", e.toString());
                                }))
                .addOnFailureListener(e -> {
                    mProgressDialog.dismiss();
                    Toast.makeText(activity, "Gagal memperbarui profil", Toast.LENGTH_SHORT).show();
                    Log.d("userDp: ", e.toString());
                });

    }

}
