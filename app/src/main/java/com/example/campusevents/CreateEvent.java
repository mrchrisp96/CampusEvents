package com.example.campusevents;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.core.view.DataEvent;

import java.util.Calendar;

import static android.R.layout.simple_spinner_item;

public class CreateEvent extends AppCompatActivity {

    EditText name, location, phone;
    TextView timeOfEvent;
    Button next, selectTime;
    RadioGroup whichOne;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        whichOne = (RadioGroup) findViewById(R.id.radiogroup);
        whichOne.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if(checkedId == R.id.radioButton) {
                    NewEvent.newEvent.whichOne = "Club";
                } else if(checkedId == R.id.radioButton2) {
                    NewEvent.newEvent.whichOne = "Event";
                }
            }
        });

        timeOfEvent = (TextView) findViewById(R.id.timeOfEvent);
        name = (EditText) findViewById(R.id.club);
        location = (EditText) findViewById(R.id.textview18);
        phone = (EditText) findViewById(R.id.textview20);

        next = (Button) findViewById(R.id.button4);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(NewEvent.newEvent.whichOne == null) {
                    Toast.makeText(getApplicationContext(), "Please choose a club or event.", Toast.LENGTH_SHORT).show();
                } else if (name.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter a name for your club/event.", Toast.LENGTH_SHORT).show();
                } else if (location.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter a location for your club/event.", Toast.LENGTH_SHORT).show();
                } else if (phone.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter a phone number for others to reach you.", Toast.LENGTH_SHORT).show();
                } else if (timeOfEvent.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter a time for your club/event.", Toast.LENGTH_SHORT).show();
                } else {
                    NewEvent.newEvent.location = location.getText().toString();
                    NewEvent.newEvent.name = name.getText().toString();
                    NewEvent.newEvent.phone = location.getText().toString();
                    if(NewEvent.newEvent.whichOne.equals("Club")) {
                        Intent intent = new Intent(CreateEvent.this, CreateEvent2.class);
                        startActivity(intent);
                    } else if(NewEvent.newEvent.whichOne.equals("Event")) {
                        Intent intent = new Intent(CreateEvent.this, DateEvent.class);
                        startActivity(intent);
                    }
                }
            }
        });

        selectTime = (Button) findViewById(R.id.openTimePicker);
        selectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                final int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(CreateEvent.this, R.style.DialogTheme, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String AM_PM;
                        int newHour = 0;
                        if(hourOfDay < 12) {
                            newHour = hourOfDay;
                            AM_PM = "AM";
                        } else {
                            newHour = hourOfDay - 12;
                            AM_PM = "PM";
                        }
                        NewEvent.newEvent.time = hourOfDay + ":" + minute;
                        System.out.println(NewEvent.newEvent.time);
                        timeOfEvent.setText(newHour + ":" + minute + AM_PM);
                    }
                }, hour, minute, false);

                timePickerDialog.show();
            }
        });
    }
}
