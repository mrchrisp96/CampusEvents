package com.example.campusevents;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/* Class written by Christopher Park */

public class DateEvent extends AppCompatActivity {

    Date todaysDate, selectedDate;
    TextView showDate;
    CalendarView calendar;
    Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_event);

        @SuppressLint("SimpleDateFormat") final SimpleDateFormat testDate = new SimpleDateFormat("MMM d, yyyy");

        Calendar c = Calendar.getInstance();
        String dayToStr = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());

        showDate = (TextView) findViewById(R.id.textView24);
        showDate.setText("Selected date:\n" + dayToStr);

        try {
            Date temp = testDate.parse(dayToStr);
            System.out.println(temp);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        next = (Button) findViewById(R.id.button6);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DateEvent.this, CreateEvent2.class);
                startActivity(intent);
            }
        });

        calendar = (CalendarView) findViewById(R.id.calendarView);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);
                String changedCal = DateFormat.getDateInstance(DateFormat.FULL).format(newDate.getTime());

                showDate.setText("Selected date:\n" + changedCal);
            }
        });
    }
}
