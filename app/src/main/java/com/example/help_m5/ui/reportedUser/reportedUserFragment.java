package com.example.help_m5.ui.reportedUser;

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
import com.example.help_m5.databinding.FragmentReportedUserBinding;
import com.example.help_m5.ui.study.StudyViewModel;

public class reportedUserFragment extends Fragment {

    private FragmentReportedUserBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ReportedUserViewModel ViewModel =
                new ViewModelProvider(this).get(ReportedUserViewModel.class);

        binding = FragmentReportedUserBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textReportUser;
        ViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}