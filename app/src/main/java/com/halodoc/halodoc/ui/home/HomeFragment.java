package com.halodoc.halodoc.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.halodoc.halodoc.databinding.FragmentHomeBinding;
import com.halodoc.halodoc.utils.HomeBackground;


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
//                            if (("" + document.get("role")).equals("admin")) {
//                                binding.addProduct.setVisibility(View.VISIBLE);
//                            }
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