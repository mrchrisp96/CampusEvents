package com.example.campusevents;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Event extends AppCompatActivity {

    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
    }
}
