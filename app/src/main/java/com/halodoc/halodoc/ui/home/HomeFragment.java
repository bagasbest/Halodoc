package com.halodoc.halodoc.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.halodoc.halodoc.databinding.FragmentHomeBinding;
import com.halodoc.halodoc.ui.home.buatjanji.HospitalActivity;
import com.halodoc.halodoc.ui.home.konsultasi.ConsultationDashboardActivity;
import com.halodoc.halodoc.ui.home.lainnya.MenuLainnyaActivity;
import com.halodoc.halodoc.ui.home.tokokesehatan.TokoKesehatanActivity;
import com.halodoc.halodoc.utils.HomeBackground;

import org.jetbrains.annotations.NotNull;


public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private FirebaseUser user;


    @Override
    public void onStart() {
        super.onStart();
        // TERAPKAN BACKGROUND SESUAI WAKTU
        HomeBackground.setBackgroundImage(getActivity(), binding.background);

        user = FirebaseAuth.getInstance().getCurrentUser();

        // TERAPKAN NAMA PENGGUNA
        checkIfUserLoginOrNot();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);


        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // KLIK BUAT JANJI RS
        clickBuatJanjiRS();

        // KLIK MENU LAINNYA
        clickOtherMenu();

        // KLIK MENU KONSULTASI
        clickConsultationMenu();

        //KLIK TOKO KESEHATAN
        clickTokoKesehatan();
    }

    private void clickTokoKesehatan() {
        binding.cardView3.setOnClickListener(view -> {
            startActivity(new Intent(getActivity(), TokoKesehatanActivity.class));
        });
    }

    private void clickConsultationMenu() {
        binding.cardView2.setOnClickListener(view -> {
            startActivity(new Intent(getActivity(), ConsultationDashboardActivity.class));
        });
    }

    private void clickBuatJanjiRS() {
        binding.cardView.setOnClickListener(view -> {
            startActivity(new Intent(getActivity(), HospitalActivity.class));
        });
    }

    private void clickOtherMenu() {
        binding.cardView4.setOnClickListener(view -> {
            startActivity(new Intent(getActivity(), MenuLainnyaActivity.class));
        });
    }

    private void checkIfUserLoginOrNot() {
        if(user != null) {
            FirebaseFirestore
                    .getInstance()
                    .collection("users")
                    .document(user.getUid())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            binding.greeting.setText("Selamat Datang,\n " + document.get("name"));

                        }
                    });
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}