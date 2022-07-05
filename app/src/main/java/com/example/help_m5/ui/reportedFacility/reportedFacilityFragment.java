package com.example.help_m5.ui.reportedFacility;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.help_m5.R;

public class reportedFacilityFragment extends Fragment {

    private ReportedFacilityViewModel mViewModel;

    public static reportedFacilityFragment newInstance() {
        return new reportedFacilityFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reported_facility, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ReportedFacilityViewModel.class);
        // TODO: Use the ViewModel
    }

}