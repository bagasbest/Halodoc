package com.halodoc.halodoc.ui.transaction;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.halodoc.halodoc.LoginActivity;
import com.halodoc.halodoc.databinding.FragmentTransactionBinding;

import org.jetbrains.annotations.NotNull;


public class TransactionFragment extends Fragment {

    private FragmentTransactionBinding binding;
    private FirebaseUser user;
    private TransactionAdapter adapter;

    @Override
    public void onResume() {
        super.onResume();
        checkIsUserLoginOrNot();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTransactionBinding.inflate(inflater, container, false);

        user = FirebaseAuth.getInstance().getCurrentUser();

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
            binding.transactionHistory.setVisibility(View.VISIBLE);
            //CEK APAKAH YANG LOGIN MERUPAKAN USER / ADMIN
            checkIsAdminOrNot();
        }
        else {
            binding.notLogin.setVisibility(View.VISIBLE);
            binding.transactionHistory.setVisibility(View.GONE);

        }
    }

    private void checkIsAdminOrNot() {
        FirebaseFirestore
                .getInstance()
                .collection("users")
                .document(user.getUid())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                   if(("" + documentSnapshot.get("role")).equals("admin")) {
                       // TAMPILKAN SEMUA TRANSAKSI DARI SELURUH USER
                       initRecyclerView("admin");
                       initViewModel("admin");
                   } else {
                       // TAMPILKAN SEMUA TRANSAKSI DARI USER TERSEBUT
                       initRecyclerView("user");
                       initViewModel("user");
                   }
                });
    }

    private void initRecyclerView(String role) {
        if(role.equals("admin")){
            binding.rvTransaction.setLayoutManager(new LinearLayoutManager(getActivity()));
        } else {
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            layoutManager.setReverseLayout(true);
            layoutManager.setStackFromEnd(true);
            binding.rvTransaction.setLayoutManager(layoutManager);
        }
        adapter = new TransactionAdapter(role);
        adapter.notifyDataSetChanged();
        binding.rvTransaction.setAdapter(adapter);
    }

    private void initViewModel(String role) {

        // tampilkan daftar rumah sakit
        TransactionViewModel viewModel = new ViewModelProvider(this).get(TransactionViewModel.class);

        if (role.equals("admin")) {
            viewModel.setAllTransaction();
        } else {
            viewModel.setTransactionByUid(user.getUid());
        }

        viewModel.getTransaction().observe(getViewLifecycleOwner(), transactionModelArrayList -> {
            if (transactionModelArrayList.size() > 0) {
                binding.noData.setVisibility(View.GONE);
                adapter.setData(transactionModelArrayList);
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