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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeScreen extends AppCompatActivity {

    RecyclerView todaysEvents;
    RecyclerView.LayoutManager layoutManager;
    TodayEventsAdapter adapter;
    ArrayList<Events> events = new ArrayList<>();
    Button account;
    TextView title;

    //static final int REQUEST_CODE = 2;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference reference = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        title = (TextView) findViewById(R.id.text_home);
        title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24f);
        title.setText("!\nHere are today's events at \n" + Student.currentStudent.college);

        populateMyEvents();

        account = (Button) findViewById(R.id.button3);
        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeScreen.this, MyAccount.class);
                startActivity(intent);
            }
        });

        todaysEvents = (RecyclerView) findViewById(R.id.todaysEvents);
        layoutManager = new LinearLayoutManager(this);
        adapter = new TodayEventsAdapter(events, new OnItemClickListener() {
            @Override
            public void onClick(Events event) {
                Intent intent = new Intent(HomeScreen.this, Event.class);
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

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 600);
        todaysEvents.setLayoutParams(lp);
        account.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f);

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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        reference = database.getReference().child("Students").child(Student.currentStudent.username).child("Attending Clubs");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Events event = new Events();
                    event.name = data.getKey();
                    Student.currentStudent.myEvents.add(event);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
