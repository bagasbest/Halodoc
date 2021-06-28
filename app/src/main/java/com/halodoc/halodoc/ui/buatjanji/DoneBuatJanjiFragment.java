package com.halodoc.halodoc.ui.buatjanji;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.halodoc.halodoc.R;
import com.halodoc.halodoc.databinding.FragmentDoneBuatJanjiBinding;

import org.jetbrains.annotations.NotNull;


public class DoneBuatJanjiFragment extends Fragment {

    private FragmentDoneBuatJanjiBinding binding;
    private BuatJanjiAdapter adapter;
    private String userUid;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDoneBuatJanjiBinding.inflate(inflater, container, false);
        userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // CEK APAKAH ADMIN ATAU BUKAN
        checkIsAdminOrNot();

        return binding.getRoot();
    }

    private void checkIsAdminOrNot() {
        FirebaseFirestore
                .getInstance()
                .collection("users")
                .document(userUid)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (("" + document.get("role")).equals("admin")) {
                            // user yang login adalah admin
                            initRecyclerView();
                            initViewModel("admin");
                        } else {
                            // user yang login adalah pengguna biasa/kustomer
                            initRecyclerView();
                            initViewModel("user");
                        }
                    }
                });
    }



    private void initRecyclerView() {
        binding.rvProgressBuatJanji.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new BuatJanjiAdapter();
        adapter.notifyDataSetChanged();
        binding.rvProgressBuatJanji.setAdapter(adapter);
    }

    private void initViewModel(String role) {
        // tampilkan daftar belanjaan di Halaman Order/Payment
        BuatJanjiViewModel viewModel = new ViewModelProvider(this).get(BuatJanjiViewModel.class);

        binding.progressBar.setVisibility(View.VISIBLE);
        if (role.equals("admin")) {
            viewModel.setDataByAdminSide();
        } else {
            viewModel.setDataByUserIdDone(userUid);
        }
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