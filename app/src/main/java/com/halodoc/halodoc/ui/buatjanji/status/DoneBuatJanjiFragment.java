package com.halodoc.halodoc.ui.buatjanji.status;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.halodoc.halodoc.databinding.FragmentDoneBuatJanjiBinding;
import com.halodoc.halodoc.ui.buatjanji.BuatJanjiAdapter;
import com.halodoc.halodoc.ui.buatjanji.BuatJanjiViewModel;

import org.jetbrains.annotations.NotNull;


public class DoneBuatJanjiFragment extends Fragment {

    private FragmentDoneBuatJanjiBinding binding;
    private BuatJanjiAdapter adapter;
    private String userUid;

    @Override
    public void onResume() {
        super.onResume();
        // TAMPILKAN RIWAYAT BUAT JANJI RS (SELESAI)
        initRecyclerView();
        initViewModel();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDoneBuatJanjiBinding.inflate(inflater, container, false);
        userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        return binding.getRoot();
    }

    private void initRecyclerView() {
        binding.rvProgressBuatJanji.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new BuatJanjiAdapter();
        adapter.notifyDataSetChanged();
        binding.rvProgressBuatJanji.setAdapter(adapter);
    }

    private void initViewModel() {
        BuatJanjiViewModel viewModel = new ViewModelProvider(this).get(BuatJanjiViewModel.class);

        binding.progressBar.setVisibility(View.VISIBLE);
        viewModel.setDataByUserIdDone(userUid);
        viewModel.getData().observe(getViewLifecycleOwner(), list -> {
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