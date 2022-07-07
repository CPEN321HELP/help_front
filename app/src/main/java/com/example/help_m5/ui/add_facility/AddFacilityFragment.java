package com.example.help_m5.ui.add_facility;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.help_m5.DatabaseConnection;
import com.example.help_m5.databinding.FragmentAddFacilityBinding;

public class AddFacilityFragment extends Fragment {

    private static final String TAG = "HomeFragment";
    private FragmentAddFacilityBinding binding;
    private DatabaseConnection DBconnection;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAddFacilityBinding.inflate(inflater, container, false);

        View root = binding.getRoot();


        return root;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}