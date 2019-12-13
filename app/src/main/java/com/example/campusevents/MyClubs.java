package com.example.campusevents;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyClubs extends AppCompatActivity {

    ArrayList<Events> events;
    RecyclerView myEvents;
    RecyclerView.LayoutManager layoutManager;
    MyEventsAdapter myEventsAdapter;
    Button addEvent;
    TextView title;

    static final int REQUEST_CODE = 2;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference reference = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_clubs);

        events = Student.currentStudent.myEvents;
        populateMyEvents();

        myEvents = (RecyclerView) findViewById(R.id.myEvents);

        layoutManager = new LinearLayoutManager(this);
        myEvents.setHasFixedSize(true);
        myEventsAdapter = new MyEventsAdapter(events, new OnItemClickListener() {
            @Override
            public void onClick(Events event) {
                Intent intent = new Intent(MyClubs.this, Event.class);
                intent.putExtra("Name", event.name);
                intent.putExtra("Host", event.host);
                intent.putExtra("Email", event.email);
                intent.putExtra("Phone", event.phone);
                intent.putExtra("Location", event.location);
                intent.putExtra("Time", event.time);
                intent.putExtra("Description", event.description);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
        myEvents.setLayoutManager(layoutManager);
        myEvents.setAdapter(myEventsAdapter);

        addEvent = (Button) findViewById(R.id.addEvent);
        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyClubs.this, CreateEvent.class);
                startActivity(intent);
            }
        });

        addEvent.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f);

        title = (TextView) findViewById(R.id.text_myclubs);
        title.setText("Clubs I Made");
    }

    public void populateMyEvents() {
        reference = reference.child("Students").child(Student.currentStudent.username);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    if (data.getKey().equals("Attending Clubs")) {
                        data = data.child("Attending Clubs");
                        for (DataSnapshot dataSnapshot1 : data.getChildren()) {
                            Events event = new Events();

                            event.name = dataSnapshot1.getKey();
                            event.host = dataSnapshot1.child("Host").getValue().toString();
                            event.email = dataSnapshot1.child("Email").getValue().toString();
                            event.phone = dataSnapshot1.child("Phone").getValue().toString();
                            event.location = dataSnapshot1.child("Location").getValue().toString();
                            event.time = dataSnapshot1.child("Time").getValue().toString();
                            event.description = dataSnapshot1.child("Description").getValue().toString();

                            events.add(event);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
