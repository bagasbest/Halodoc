package com.halodoc.halodoc.ui.consultation;

import android.os.Bundle;
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
import com.halodoc.halodoc.databinding.FragmentConsultationBinding;
import com.halodoc.halodoc.databinding.FragmentHomeBinding;
import com.halodoc.halodoc.ui.buatjanji.DoneBuatJanjiFragment;
import com.halodoc.halodoc.ui.buatjanji.ProgressBuatJanjiFragment;
import com.halodoc.halodoc.ui.buatjanji.SectionPagerAdapterBuatJanji;
import com.halodoc.halodoc.ui.consultation.status.ConsultationStatusDoneFragment;
import com.halodoc.halodoc.ui.consultation.status.ConsultationStatusProgressFragment;

public class ConsultationFragment extends Fragment {

    private FragmentConsultationBinding binding;
    private FirebaseUser user;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentConsultationBinding.inflate(inflater, container, false);

        user = FirebaseAuth.getInstance().getCurrentUser();
        checkIsUserLoginOrNot();


        // KLIK TAB VIEW DALAM PROSES / SELESAI
        SectionPagerAdapterConslutation sectionPagerAdapter = new SectionPagerAdapterConslutation(getChildFragmentManager());
        binding.viewPager.setAdapter(sectionPagerAdapter);
        binding.tabs.setupWithViewPager(binding.viewPager);
        binding.tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        new ConsultationStatusProgressFragment();
                    case 1:
                        new ConsultationStatusDoneFragment();
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