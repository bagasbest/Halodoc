package com.halodoc.halodoc.ui.transaction;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.halodoc.halodoc.R;
import com.halodoc.halodoc.databinding.FragmentTransactionBinding;

import org.jetbrains.annotations.NotNull;


public class TransactionFragment extends Fragment {

    private FragmentTransactionBinding binding;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTransactionBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}