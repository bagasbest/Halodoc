package com.halodoc.halodoc.ui.profile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.halodoc.halodoc.LoginActivity;
import com.halodoc.halodoc.R;
import com.halodoc.halodoc.databinding.FragmentProfileBinding;
import com.halodoc.halodoc.utils.HomeBackground;
import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.Map;


public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private FirebaseUser user;

    // variabel
    private static final int REQUEST_FROM_GALLERY_TO_SELF_PHOTO = 1001;
    private static final int REQUEST_FROM_CAMERA_TO_SELF_PHOTO = 1002;

    @Override
    public void onStart() {
        super.onStart();
        user = FirebaseAuth.getInstance().getCurrentUser();
        checkIsUserLoginOrNot();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        user = FirebaseAuth.getInstance().getCurrentUser();

        // KLIK LOGIN, JIKA BELUM LOGIN
        loginClick();

        // KLIK PERBARUI PROFIL
        updateProfile();

        // KLIK PERBARUI USER DP
        updateUserDp();
    }

    private void updateUserDp() {
        binding.updateUserDp.setOnClickListener(view -> {

            String[] option = {"Melalui Kamera", "Melalui Galeri"};

            new AlertDialog.Builder(getActivity())
                    .setTitle("Konfirmasi Pengambilan Foto")
                    .setIcon(R.drawable.ic_baseline_photo_camera_24)
                    .setItems(option, (dialogInterface, i) -> {
                        if (i == 0) {
                            // AMBIL FOTO DARI KAMERA
                            pickFromCamera();
                        } else {
                            // AMBIL FOTO DARI GALERI
                            pickFromGallery();
                        }
                    }).show();
        });
    }

    private void pickFromCamera() {
        ImagePicker
                .with(getActivity())
                .galleryOnly()
                .compress(1024)
                .maxResultSize(720, 720)
                .start(REQUEST_FROM_CAMERA_TO_SELF_PHOTO);
    }

    private void pickFromGallery() {
        ImagePicker
                .with(getActivity())
                .galleryOnly()
                .compress(1024)
                .maxResultSize(720, 720)
                .start(REQUEST_FROM_GALLERY_TO_SELF_PHOTO);
    }


    private void updateProfile() {
        binding.updateProfileBtn.setOnClickListener(view -> {
            new AlertDialog.Builder(getActivity())
                    .setTitle("Konfirmasi Perbarui Profil")
                    .setMessage("Apakah kamu yakin ingin memperbarui profil, berdasarkan data yang telah kamu inputkan ?")
                    .setIcon(R.drawable.ic_baseline_edit_24)
                    .setPositiveButton("YA", (dialogInterface, i) -> {
                        // SIMPAN PERUBAHAN PROFIL PENGGUNA KE DATABASE
                        saveProfileChangesToDatabase();
                    })
                    .setNegativeButton("TIDAK", null)
                    .show();
        });
    }

    private void saveProfileChangesToDatabase() {
        String name = binding.nameEt.getText().toString().trim();
        String phone = binding.phoneEt.getText().toString().trim();
        String height = binding.heightEt.getText().toString().trim();
        String weight = binding.weightEt.getText().toString().trim();

        // VALIDASI KOLOM PROFIL, JANGAN SAMPAI ADA YANG KOSONG
        if (name.isEmpty()) {
            binding.nameEt.setError("Nama Lengkap tidak boleh kosong");
            return;
        } else if (phone.isEmpty()) {
            binding.phoneEt.setError("Nomor Telepon tidak boleh kosong");
            return;
        } else if (height.isEmpty()) {
            binding.heightEt.setError("Tinggi Badan tidak boleh kosong");
            return;
        } else if (weight.isEmpty()) {
            binding.weightEt.setError("Berat Badan tidak boleh kosong");
            return;
        }

        Map<String, Object> updateProfile = new HashMap<>();
        updateProfile.put("name", name);
        updateProfile.put("phone", phone);
        updateProfile.put("height", height);
        updateProfile.put("weight", weight);

        // SIMPAN PERUBAHAN PROFIL TERBARU KE DATABASE
        binding.progressBar.setVisibility(View.VISIBLE);
        FirebaseFirestore
                .getInstance()
                .collection("users")
                .document(user.getUid())
                .update(updateProfile)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        binding.progressBar.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "Berhasil memperbarui profil", Toast.LENGTH_SHORT).show();
                    } else {
                        binding.progressBar.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "Gagal memperbarui profil", Toast.LENGTH_SHORT).show();
                        Log.e("Error update profil", task.toString());
                    }
                });

    }

    private void populateUI() {
        if (user != null) {

            // TERAPKAN BACKGROUND SESUAI WAKTU
            HomeBackground.setBackgroundImage(getActivity(), binding.imageView2);


            // AMBIL DATA PENGGUNA DARI DATABASE, UNTUK DITAMPILKAN SEBAGAI PROFIL
            FirebaseFirestore
                    .getInstance()
                    .collection("users")
                    .document(user.getUid())
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        String name = "" + documentSnapshot.get("name").toString();
                        String email = "" + documentSnapshot.get("email").toString();
                        String phone = "" + documentSnapshot.get("phone").toString();
                        String dob = "" + documentSnapshot.get("dob").toString();
                        String gender = "" + documentSnapshot.get("gender").toString();
                        String height = "" + documentSnapshot.get("height").toString();
                        String weight = "" + documentSnapshot.get("weight").toString();
                        String userDp = "" + documentSnapshot.get("userDp").toString();

                        //TERAPKAN PADA UI PROFIL
                        binding.nameEt.setText(name);
                        binding.emailEt.setText(email);
                        binding.phoneEt.setText(phone);
                        binding.button.setText(dob);
                        binding.heightEt.setText(height);
                        binding.weightEt.setText(weight);

                        Glide.with(getActivity())
                                .load(userDp)
                                .error(R.drawable.ic_baseline_face_24)
                                .into(binding.userDp);

                        if (gender.equals("Laki-laki")) {
                            binding.male.setChecked(true);
                        } else {
                            binding.female.setChecked(true);
                        }
                        binding.radioGroup2.setEnabled(false);
                        binding.emailEt.setEnabled(false);

                    })
                    .addOnFailureListener(e -> {
                        Log.e("Error get profil", e.toString());
                        Toast.makeText(getActivity(), "Gagal mengambil data pengguna", Toast.LENGTH_SHORT).show();
                    });

        }
    }

    private void loginClick() {
        binding.loginBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            getActivity().finish();
        });
    }

    private void checkIsUserLoginOrNot() {
        if (user != null) {
            binding.constraintLayout.setVisibility(View.VISIBLE);
            binding.notLogin.setVisibility(View.GONE);

            // TAMPILKAN PROFIL PENGGUNA JIKA SUDAH LOGIN
            populateUI();
        } else {
            binding.constraintLayout.setVisibility(View.GONE);
            binding.notLogin.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK) {
            binding.progressBar.setVisibility(View.VISIBLE);
            ProfileDatabase.uploadImageToDatabase(data.getData(), getActivity(), user.getUid());
            Glide.with(this)
                    .load(data.getData())
                    .placeholder(R.drawable.ic_baseline_face_24)
                    .error(R.drawable.ic_baseline_face_24)
                    .into(binding.userDp);
            binding.progressBar.setVisibility(View.GONE);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}