package com.example.campusevents;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class AllClubs extends AppCompatActivity {

    RecyclerView todaysEvents;
    RecyclerView.LayoutManager layoutManager;
    TodayEventsAdapter adapter;
    ArrayList<Events> events = new ArrayList<>();
    TextView title;

    //static final int REQUEST_CODE = 2;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference reference = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_clubs);
        populateMyEvents();

        title = (TextView) findViewById(R.id.text_home);
        title.setText("All Clubs");

        todaysEvents = (RecyclerView) findViewById(R.id.todaysEvents);
        layoutManager = new LinearLayoutManager(this);
        adapter = new TodayEventsAdapter(events, new OnItemClickListener() {
            @Override
            public void onClick(Events event) {
                Intent intent = new Intent(AllClubs.this, Event.class);
                intent.putExtra("Name", event.name);
                intent.putExtra("Host", event.host);
                intent.putExtra("Email", event.email);
                intent.putExtra("Phone", event.phone);
                intent.putExtra("Location", event.location);
                intent.putExtra("Time", event.time);
                intent.putExtra("Description", event.description);
                startActivity(intent);
            }
        });
        todaysEvents.setLayoutManager(layoutManager);
        todaysEvents.setAdapter(adapter);
    }

    public void populateMyEvents() {
        reference = database.getReference().child("College").child(Student.currentStudent.college).child("Clubs");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data: dataSnapshot.getChildren()) {
                    Events event = new Events();

                    event.name = data.getKey();
                    event.host = data.child("Host").getValue().toString();
                    event.email = data.child("Email").getValue().toString();
                    event.phone = data.child("Phone").getValue().toString();
                    event.location = data.child("Location").getValue().toString();
                    event.time = data.child("Time").getValue().toString();
                    event.description = data.child("Description").getValue().toString();

                    events.add(event);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
