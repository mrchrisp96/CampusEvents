package com.example.campusevents;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MyEvents extends AppCompatActivity {

    String club = "", event = "";
    TextView title, description;
    RecyclerView recyclerView;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference reference = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_events);

        title = (TextView) findViewById(R.id.textView26);
        description = (TextView) findViewById(R.id.textView27);

        Intent intent = getIntent();
        club = intent.getStringExtra("Club");
        event = intent.getStringExtra("Event");

        if(club != null) {
            // populate recyclerview with clubs owned by the student
            title.setText("Club");
            description.setText("Select a club to edit");
        } else if(event != null) {
            // populate recyclerview with events owned by the student
            title.setText("Event");
            description.setText("Select an event to edit");
        }
    }
}
