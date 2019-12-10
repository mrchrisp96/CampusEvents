package com.example.campusevents;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.CharacterStyle;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/* Class written by Christopher Park */

public class Preview extends AppCompatActivity {

    TextView title, mainDesc;
    String phone;
    Button publish;

    static final int TOAST_CODE = 1;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference reference = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            phone = extras.getString("Phone");
            if(phone != null) {
                phone = "(" + phone.substring(0, 3) + ")-" + phone.substring(2, 5) + "-" + phone.substring(6, 10);
            }
        }

        title = (TextView) findViewById(R.id.textView25);
        title.setText(NewEvent.newEvent.name);
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

        mainDesc = (TextView) findViewById(R.id.textView31);

        mainDesc.setText(host);
        mainDesc.append(Student.currentStudent.name + "\n");
        mainDesc.append(email);
        mainDesc.append(Student.currentStudent.email + "\n");
        mainDesc.append(textPhone);
        mainDesc.append(phone + "\n");
        mainDesc.append(loc);
        mainDesc.append(NewEvent.newEvent.location + "\n");
        mainDesc.append(time);
        mainDesc.append(NewEvent.newEvent.time + "\n");
        mainDesc.append(desc);
        mainDesc.append("\n" + NewEvent.newEvent.description + "\n");

        publish = (Button) findViewById(R.id.button7);
        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference = reference.child(Student.currentStudent.college).child("Clubs").child(NewEvent.newEvent.name);
                reference.child("Host").setValue(Student.currentStudent.name);
                reference.child("Email").setValue(Student.currentStudent.email);
                reference.child("Phone").setValue(textPhone);
                reference.child("Location").setValue(NewEvent.newEvent.location);
                reference.child("Time").setValue(NewEvent.newEvent.time);
                reference.child("Description").setValue(NewEvent.newEvent.description);

                reference = database.getReference().child("Students").child(Student.currentStudent.name)
                        .child("My Clubs").child(NewEvent.newEvent.name);

                reference.child("Host").setValue(Student.currentStudent.name);
                reference.child("Email").setValue(Student.currentStudent.email);
                reference.child("Phone").setValue(textPhone);
                reference.child("Location").setValue(NewEvent.newEvent.location);
                reference.child("Time").setValue(NewEvent.newEvent.time);
                reference.child("Description").setValue(NewEvent.newEvent.description);

                Intent intent = new Intent(Preview.this, MainActivity.class);
                startActivityForResult(intent, TOAST_CODE);
            }
        });
//        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(450, ActionBar.LayoutParams.WRAP_CONTENT);
//        publish.setLayoutParams(params);
//        publish.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f);
    }
}
