package com.example.campusevents.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.campusevents.Student;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    String student;

    public HomeViewModel() {
        student = Student.currentStudent.username;
        mText = new MutableLiveData<>();
        mText.setValue("Welcome, " + student + "!\nHere are today's events at \n" + Student.currentStudent.college);
    }

    public LiveData<String> getText() {
        return mText;
    }
}