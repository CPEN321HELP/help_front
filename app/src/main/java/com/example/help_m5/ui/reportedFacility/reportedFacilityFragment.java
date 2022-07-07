package com.example.help_m5.ui.reportedFacility;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.help_m5.R;
import com.example.help_m5.databinding.FragmentReportedFacilityBinding;

public class reportedFacilityFragment extends Fragment {

    private FragmentReportedFacilityBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ReportedFacilityViewModel ViewModel =
                new ViewModelProvider(this).get(ReportedFacilityViewModel.class);

        binding = FragmentReportedFacilityBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textReportFacility;
        ViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}