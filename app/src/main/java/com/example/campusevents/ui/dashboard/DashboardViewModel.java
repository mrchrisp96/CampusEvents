package com.example.campusevents.ui.dashboard;

import android.widget.TextView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DashboardViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    TextView welcome;

    public DashboardViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("~ Events Around Me ~\nYou must be on campus to find nearby events");
    }

    public LiveData<String> getText() {
        return mText;
    }
}