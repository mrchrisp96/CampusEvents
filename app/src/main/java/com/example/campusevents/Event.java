package com.example.campusevents;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ActionBar;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
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
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/* Class written by Christopher Park */

// Shows selected club/event to the student

public class Event extends AppCompatActivity {

    String name, host, email, phone, location, time, date, description;
    TextView mainDesc, title;
    Button join, attendees;
    Events tempEvent;
    boolean signedUp = false, myClub = false; // if the student is already signed up

    static final int REQUEST_CODE = 2;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference reference = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        name = getIntent().getStringExtra("Name");
        host = getIntent().getStringExtra("Host");
        time = getIntent().getStringExtra("Time");
        email = getIntent().getStringExtra("Email");
        phone = getIntent().getStringExtra("Phone");
        location = getIntent().getStringExtra("Location");
        description = getIntent().getStringExtra("Description");

        title = (TextView) findViewById(R.id.textView36);
        title.setText(name);
        join = (Button) findViewById(R.id.button8);

        database.getReference().child("Students").child(Student.currentStudent.username).child("My Clubs")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot data: dataSnapshot.getChildren()) {
                            if(data.getKey() != null && data.getKey().equals(name)) {
                                join.setText("Edit Club");
                                myClub = true;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        if(!myClub) {
            database.getReference().child("Students").child(Student.currentStudent.username).child("Attending Clubs")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                if (data.getKey() != null && data.getKey().equals(name)) {
                                    signedUp = true;
                                    join.setText("Leave Club");
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }

        // only if the student didn't sign up for it
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myClub) {
                    // if the students owns the club and wishes to edit it
                    Intent intent = new Intent(Event.this, EditEvents.class);
                    intent.putExtra("Name", name);
                    intent.putExtra("Host", host);
                    intent.putExtra("Email", email);
                    intent.putExtra("Phone", phone);
                    intent.putExtra("Location", location);
                    intent.putExtra("Time", time);
                    intent.putExtra("Description", description);
                    startActivity(intent);
                } else {
                    if (!signedUp) {
                        // if student didn't sign up for this club yet
                        AlertDialog.Builder dialog = new AlertDialog.Builder(Event.this);
                        dialog.setMessage("")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        reference = database.getReference().child("College").child(Student.currentStudent.college).child("Clubs").child(name)
                                                .child("Members").child(Student.currentStudent.uid);
                                        reference.child("Name").setValue(Student.currentStudent.name);
                                        reference.child("Email").setValue(Student.currentStudent.email);

                                        reference = database.getReference().child("Students").child(Student.currentStudent.username).child("Attending Clubs").child(name);
                                        reference.setValue(name);
                                        signedUp = true;
                                        join.setText("Leave Club");

                                        Events events = new Events();
                                        events.name = name;
                                        events.time = time;
                                        events.host = host;
                                        events.location = location;
                                        events.description = description;
                                        events.phone = phone;
                                        events.email = email;
                                        Student.currentStudent.memberClub.add(events);
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alertDialog = dialog.create();
                        alertDialog.setTitle("Would you like to join " + name + "?");
                        alertDialog.show();
                    } else {
                        // if the student intends on leaving the club
                        AlertDialog.Builder dialog = new AlertDialog.Builder(Event.this);
                        dialog.setMessage("You can always rejoin again")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        reference = database.getReference().child("College").child(Student.currentStudent.college)
                                                .child("Clubs").child(name).child("Members");
                                        reference.child(Student.currentStudent.uid).removeValue();

                                        reference = database.getReference().child("Students").child(Student.currentStudent.username).child("Attending Clubs").child(name);
                                        reference.removeValue();
                                        // change color of button
                                        signedUp = false;
                                        join.setText("Join Club");
                                        int counter = 0;
                                        for (Events event : Student.currentStudent.memberClub) {
                                            if (event.name.equals(name)) {
                                                Student.currentStudent.memberClub.remove(counter);
                                                break;
                                            }
                                            counter++;
                                        }
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alertDialog = dialog.create();
                        alertDialog.setTitle("Are you sure you want to leave " + name + "?");
                        alertDialog.show();
                    }
                }
            }
        });

        attendees = (Button) findViewById(R.id.button10);
        attendees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Event.this, AttendeesList.class);
                intent.putExtra("Event", name);
                startActivity(intent);
            }
        });

        mainDesc = (TextView) findViewById(R.id.textView35);

        final StyleSpan boldText = new StyleSpan(android.graphics.Typeface.BOLD);

        SpannableString spanHost = new SpannableString("Host: ");
        spanHost.setSpan(new UnderlineSpan(), 0, spanHost.length() - 1, 0);
        spanHost.setSpan(CharacterStyle.wrap(boldText), 0, spanHost.length() - 1, SpannableString.SPAN_INCLUSIVE_INCLUSIVE);

        SpannableString spanEmail = new SpannableString("Email: ");
        spanEmail.setSpan(new UnderlineSpan(), 0, spanEmail.length() - 1, 0);
        spanEmail.setSpan(CharacterStyle.wrap(boldText), 0, spanEmail.length() - 1, SpannableString.SPAN_INCLUSIVE_INCLUSIVE);

        final SpannableString textPhone = new SpannableString("Phone: ");
        textPhone.setSpan(new UnderlineSpan(), 0, 5, 0);
        textPhone.setSpan(CharacterStyle.wrap(boldText), 0, textPhone.length() - 1, SpannableString.SPAN_INCLUSIVE_INCLUSIVE);

        SpannableString loc = new SpannableString("Location: ");
        loc.setSpan(new UnderlineSpan(), 0, loc.length() - 1, 0);
        loc.setSpan(CharacterStyle.wrap(boldText), 0, loc.length() - 1, SpannableString.SPAN_INCLUSIVE_INCLUSIVE);

        SpannableString spantime = new SpannableString("Time: ");
        spantime.setSpan(new UnderlineSpan(), 0, spantime.length() - 1, 0);
        spantime.setSpan(CharacterStyle.wrap(boldText), 0, spantime.length() - 1, SpannableString.SPAN_INCLUSIVE_INCLUSIVE);

        SpannableString desc = new SpannableString("Description: ");
        desc.setSpan(new UnderlineSpan(), 0, desc.length() - 1, 0);
        desc.setSpan(CharacterStyle.wrap(boldText), 0, desc.length() - 1, SpannableString.SPAN_INCLUSIVE_INCLUSIVE);

        mainDesc.setText(spanHost);
        mainDesc.append(host + "\n");
        mainDesc.append(spanEmail);
        mainDesc.append(email + "\n");
        mainDesc.append(textPhone);
        mainDesc.append(phone + "\n");
        mainDesc.append(loc);
        mainDesc.append(location + "\n");
        mainDesc.append(spantime);
        mainDesc.append(time + "\n");
        mainDesc.append(desc);
        mainDesc.append("\n" + description + "\n");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE) {
            System.out.println("Received!");
            name = getIntent().getStringExtra("Name");
            host = getIntent().getStringExtra("Host");
            time = getIntent().getStringExtra("Time");
            email = getIntent().getStringExtra("Email");
            phone = getIntent().getStringExtra("Phone");
            location = getIntent().getStringExtra("Location");
            description = getIntent().getStringExtra("Description");
        }
    }


}
