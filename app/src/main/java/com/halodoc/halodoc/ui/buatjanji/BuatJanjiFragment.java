package com.halodoc.halodoc.ui.buatjanji;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.halodoc.halodoc.databinding.FragmentBuatjanjiBinding;


public class BuatJanjiFragment extends Fragment {

    private FragmentBuatjanjiBinding binding;
    private FirebaseUser user;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentBuatjanjiBinding.inflate(inflater, container, false);

        user = FirebaseAuth.getInstance().getCurrentUser();
        checkIsUserLoginOrNot();

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

        return binding.getRoot();
    }

    private void viewPagerConfig() {

    }

    private void checkIsUserLoginOrNot() {
        if(user != null) {
            binding.notLogin.setVisibility(View.GONE);

        } else {
            binding.notLogin.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}