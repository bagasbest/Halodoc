package com.halodoc.halodoc.ui.consultation.status;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.halodoc.halodoc.R;
import com.halodoc.halodoc.databinding.FragmentConsultationStatusProgressBinding;
import com.halodoc.halodoc.ui.buatjanji.BuatJanjiAdapter;
import com.halodoc.halodoc.ui.buatjanji.BuatJanjiViewModel;
import com.halodoc.halodoc.ui.consultation.ConsultationAdapter;
import com.halodoc.halodoc.ui.consultation.ConsultationViewModel;

import org.jetbrains.annotations.NotNull;

public class ConsultationStatusProgressFragment extends Fragment {

    private FragmentConsultationStatusProgressBinding binding;
    private ConsultationAdapter adapter;
    private String userUid;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentConsultationStatusProgressBinding.inflate(inflater, container, false);
        userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // TAMPILKAN RIWAYAT KONSULTASI
        initRecyclerView();
        initViewModel();


        return binding.getRoot();
    }

    private void initRecyclerView() {
        binding.rvConsultation.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ConsultationAdapter(userUid);
        adapter.notifyDataSetChanged();
        binding.rvConsultation.setAdapter(adapter);
    }

    private void initViewModel() {
        ConsultationViewModel viewModel = new ViewModelProvider(this).get(ConsultationViewModel.class);

        binding.progressBar.setVisibility(View.VISIBLE);
        viewModel.setUser(userUid);
        viewModel.getConsultation().observe(getViewLifecycleOwner(), list -> {
            if (list.size() > 0) {
                binding.noData.setVisibility(View.GONE);
                adapter.setData(list);
            } else {
                binding.noData.setVisibility(View.VISIBLE);
            }
            binding.progressBar.setVisibility(View.GONE);
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}