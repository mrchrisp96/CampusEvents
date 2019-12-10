package com.example.campusevents;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.CharacterStyle;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/* Class written by Christopher Park */

// Shows selected club/event to the student

public class Event extends AppCompatActivity {

    String name, host, email, phone, location, time, date, description;
    TextView mainDesc;
    Button join;
    Events tempEvent;
    boolean signedUp = false; // if the student is already signed up

    static final int REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        join = (Button) findViewById(R.id.button8);
        for(Events event: Student.currentStudent.myEvents) {
            if(event.name.equals(tempEvent.name)) {
                signedUp = true;
                join.setText("Leave Club");
                join.setBackgroundColor(0xFF0000);
                break;
            }
        }

        // only if the student didn't sign up for it
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!signedUp) {
                    // if student didn't sign up for this club yet

                } else {

                }
            }
        });

        if( ) {

        }

        mainDesc = (TextView) findViewById(R.id.textView35);

        final StyleSpan boldText = new StyleSpan(android.graphics.Typeface.BOLD);

        SpannableString host = new SpannableString("Host: ");
        host.setSpan(new UnderlineSpan(), 0, host.length() - 1, 0);
        host.setSpan(CharacterStyle.wrap(boldText), 0, host.length() - 1, SpannableString.SPAN_INCLUSIVE_INCLUSIVE);

        SpannableString email = new SpannableString("Email: ");
        email.setSpan(new UnderlineSpan(), 0, email.length() - 1, 0);
        email.setSpan(CharacterStyle.wrap(boldText), 0, email.length() - 1, SpannableString.SPAN_INCLUSIVE_INCLUSIVE);

        final SpannableString textPhone = new SpannableString("Phone: ");
        textPhone.setSpan(new UnderlineSpan(), 0, 5, 0);
        textPhone.setSpan(CharacterStyle.wrap(boldText), 0, textPhone.length() - 1, SpannableString.SPAN_INCLUSIVE_INCLUSIVE);

        SpannableString loc = new SpannableString("Location: ");
        loc.setSpan(new UnderlineSpan(), 0, loc.length() - 1, 0);
        loc.setSpan(CharacterStyle.wrap(boldText), 0, loc.length() - 1, SpannableString.SPAN_INCLUSIVE_INCLUSIVE);

        SpannableString time = new SpannableString("Time: ");
        time.setSpan(new UnderlineSpan(), 0, time.length() - 1, 0);
        time.setSpan(CharacterStyle.wrap(boldText), 0, time.length() - 1, SpannableString.SPAN_INCLUSIVE_INCLUSIVE);

        SpannableString desc = new SpannableString("Description: ");
        desc.setSpan(new UnderlineSpan(), 0, desc.length() - 1, 0);
        desc.setSpan(CharacterStyle.wrap(boldText), 0, desc.length() - 1, SpannableString.SPAN_INCLUSIVE_INCLUSIVE);

        mainDesc.setText(host);
        mainDesc.append(name + "\n");
        mainDesc.append(email);
        mainDesc.append(email + "\n");
        mainDesc.append(textPhone);
        mainDesc.append(phone + "\n");
        mainDesc.append(loc);
        mainDesc.append(location + "\n");
        mainDesc.append(time);
        mainDesc.append(time + "\n");
        mainDesc.append(desc);
        mainDesc.append("\n" + description + "\n");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if(requestCode == REQUEST_CODE) {
                name = getIntent().getStringExtra("Name");
                host = getIntent().getStringExtra("Host");
                time = getIntent().getStringExtra("Time");
                email = getIntent().getStringExtra("Email");
                phone = getIntent().getStringExtra("Phone");
                location = getIntent().getStringExtra("Location");
                description = getIntent().getStringExtra("Description");
            }
        } catch(Exception e) {
            Toast.makeText(Event.this, "This club/event does not exist anymore!", Toast.LENGTH_SHORT).show();
        }
    }
}
