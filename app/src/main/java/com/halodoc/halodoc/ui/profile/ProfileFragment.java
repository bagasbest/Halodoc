package com.halodoc.halodoc.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.halodoc.halodoc.LoginActivity;
import com.halodoc.halodoc.databinding.FragmentProfileBinding;
import org.jetbrains.annotations.NotNull;


public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;

    @Override
    public void onStart() {
        super.onStart();
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

        // KLIK LOGIN, JIKA BELUM LOGIN
        loginClick();
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
        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            binding.constraintLayout.setVisibility(View.VISIBLE);
            binding.notLogin.setVisibility(View.GONE);
        } else {
            binding.constraintLayout.setVisibility(View.GONE);
            binding.notLogin.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}