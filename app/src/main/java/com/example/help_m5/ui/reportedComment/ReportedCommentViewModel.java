package com.example.help_m5.ui.reportedComment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ReportedCommentViewModel extends ViewModel {
    private final MutableLiveData<String> mText;

    public ReportedCommentViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is ReportedCommentViewModel fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}