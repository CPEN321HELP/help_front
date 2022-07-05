package com.example.help_m5.ui.reportedUser;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ReportedUserViewModel extends ViewModel {
    private final MutableLiveData<String> mText;

    public ReportedUserViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is ReportedUserViewModel fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }}