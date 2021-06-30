package com.halodoc.halodoc.ui.buatjanji;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.halodoc.halodoc.LoginActivity;
import com.halodoc.halodoc.databinding.FragmentBuatjanjiBinding;
import com.halodoc.halodoc.ui.buatjanji.status.DoneBuatJanjiFragment;
import com.halodoc.halodoc.ui.buatjanji.status.ProgressBuatJanjiFragment;

import org.jetbrains.annotations.NotNull;


public class BuatJanjiFragment extends Fragment {

    private FragmentBuatjanjiBinding binding;
    private FirebaseUser user;

    @Override
    public void onResume() {
        super.onResume();
        user = FirebaseAuth.getInstance().getCurrentUser();
        checkIsUserLoginOrNot();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentBuatjanjiBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

       binding.loginBtn.setOnClickListener(view1 -> {
           Intent intent = new Intent(getActivity(), LoginActivity.class);
           intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
           startActivity(intent);
           getActivity().finish();
       });
    }

    private void checkIsUserLoginOrNot() {
        if(user != null) {
            binding.notLogin.setVisibility(View.GONE);
            binding.buatjanjiHistory.setVisibility(View.VISIBLE);

            // KLIK TAB VIEW DALAM PROSES / SELESAI
            SectionPagerAdapterBuatJanji sectionPagerAdapter = new SectionPagerAdapterBuatJanji(getChildFragmentManager());
            binding.viewPager.setAdapter(sectionPagerAdapter);
            binding.tabs.setupWithViewPager(binding.viewPager);
            binding.tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    switch (tab.getPosition()){
                        case 0:
                            new ProgressBuatJanjiFragment();
                        case 1:
                            new DoneBuatJanjiFragment();
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });

        } else {
            binding.notLogin.setVisibility(View.VISIBLE);
            binding.buatjanjiHistory.setVisibility(View.GONE);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}