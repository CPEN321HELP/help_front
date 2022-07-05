package com.example.help_m5.ui.reportedFacility;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ReportedFacilityViewModel extends ViewModel {
    private final MutableLiveData<String> mText;

    public ReportedFacilityViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is ReportedFacilityViewModel fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}