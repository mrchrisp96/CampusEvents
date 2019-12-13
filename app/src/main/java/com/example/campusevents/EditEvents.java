package com.example.campusevents;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Date;

/* Class written by Christopher Park */

public class EditEvents extends AppCompatActivity {

    String name, email, phone, location, time, description, newPhone;
    EditText editName, editLocation, editPhone;
    TextView edittime;
    Button next, delete, selectTime;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference reference = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_events);

        name = getIntent().getStringExtra("Name");
        time = getIntent().getStringExtra("Time");
        phone = getIntent().getStringExtra("Phone");
        location = getIntent().getStringExtra("Location");
        description = getIntent().getStringExtra("Description");

        editName = (EditText) findViewById(R.id.editclub);
        editLocation = (EditText) findViewById(R.id.editloc);
        editPhone = (EditText) findViewById(R.id.editphone);
        edittime = (TextView) findViewById(R.id.edittime);

        newPhone = phone.substring(1, 4);
        newPhone += phone.substring(6, 9);
        newPhone += phone.substring(10, 14);

        editName.setText(name);
        editName.setEnabled(false);
        editLocation.setText(location);
        editPhone.setText(newPhone);
        edittime.setText(time);

        next = (Button) findViewById(R.id.editnext);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditEvents.this, EditDesc.class);
                intent.putExtra("Description", getIntent().getStringExtra("Description"));
                intent.putExtra("Name", editName.getText().toString());
                intent.putExtra("Time", edittime.getText().toString());
                intent.putExtra("Phone", editPhone.getText().toString());
                intent.putExtra("Location", editLocation.getText().toString());
                intent.putExtra("Description", description);
                startActivity(intent);
            }
        });

        delete = (Button) findViewById(R.id.deleteButton);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(EditEvents.this);
                dialog.setMessage("This action cannot be undone. This will remove the club from your school.")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                database.getReference().child("College").child(Student.currentStudent.college).child("Clubs").child(name).removeValue();
                                database.getReference().child("Students").child(Student.currentStudent.username).child("My Clubs").child(name).removeValue();

                                Intent intent = new Intent(EditEvents.this, MainScreen.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = dialog.create();
                alertDialog.setTitle("Would you like to remove this club?");
                alertDialog.show();
            }
        });

        selectTime = (Button) findViewById(R.id.openEditTime);
        selectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                final int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(EditEvents.this, R.style.DialogTheme, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String AM_PM;
                        int newHour = 0;
                        if(hourOfDay < 12) {
                            newHour = hourOfDay;
                            AM_PM = "AM";
                            if(newHour == 0) {
                                newHour = 12;
                            }
                        } else {
                            newHour = hourOfDay - 12;
                            AM_PM = "PM";
                            if(newHour == 0) {
                                newHour = 12;
                            }
                        }
//                        NewEvent.newEvent.time = hourOfDay + ":" + minute;
                        Calendar cal = Calendar.getInstance();
                        cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        cal.set(Calendar.MINUTE, minute);
                        cal.set(Calendar.SECOND, 0);
                        Date date = cal.getTime();
                        System.out.println(date);
                        NewEvent.newEvent.date = date;
                        if(minute < 10) {
                            String tempMin = "0" + minute;
                            edittime.setText(newHour + ":" + tempMin + AM_PM);
                            time = newHour + ":" + tempMin + AM_PM;
                        } else {
                            edittime.setText(newHour + ":" + minute + AM_PM);
                            time = newHour + ":" + minute + AM_PM;
                        }
                    }
                }, hour, minute, false);

                timePickerDialog.show();
            }
        });
    }
}
