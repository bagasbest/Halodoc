package com.halodoc.halodoc.ui.transaction;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.halodoc.halodoc.R;
import com.halodoc.halodoc.databinding.FragmentTransactionBinding;
import com.halodoc.halodoc.ui.home.buatjanji.HospitalAdapter;
import com.halodoc.halodoc.ui.home.buatjanji.HospitalViewModel;

import org.jetbrains.annotations.NotNull;


public class TransactionFragment extends Fragment {

    private FragmentTransactionBinding binding;
    private FirebaseUser user;
    private TransactionAdapter adapter;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTransactionBinding.inflate(inflater, container, false);

        user = FirebaseAuth.getInstance().getCurrentUser();
        checkIsUserLoginOrNot();



        return binding.getRoot();
    }

    private void checkIsUserLoginOrNot() {
        if(user != null) {
            binding.notLogin.setVisibility(View.GONE);

            // TAMPILKAN SEMUA RUMAH SAKIT YANG TERSEDIA
            initRecyclerView();
            initViewModel("all");
        }
        else {
            binding.notLogin.setVisibility(View.VISIBLE);
        }
    }

    private void initRecyclerView() {
        binding.rvTransaction.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new TransactionAdapter();
        adapter.notifyDataSetChanged();
        binding.rvTransaction.setAdapter(adapter);
    }

    private void initViewModel(String role) {

        // tampilkan daftar rumah sakit
        TransactionViewModel viewModel = new ViewModelProvider(this).get(TransactionViewModel.class);


        if (role.equals("admin")) {
          //  viewModel.setHospitalByAllSpecialist();
        } else {
            viewModel.setTransactionByUid(user.getUid());
        }

        viewModel.getTransaction().observe(getViewLifecycleOwner(), transactionModelArrayList -> {
            if (transactionModelArrayList.size() > 0) {
            //    binding.noData.setVisibility(View.GONE);
                adapter.setData(transactionModelArrayList);
            } else {
             //   binding.noData.setVisibility(View.VISIBLE);
            }
         //   binding.progressBar.setVisibility(View.GONE);
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}