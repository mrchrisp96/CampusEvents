package com.example.campusevents;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AttendeesList extends AppCompatActivity {

    RecyclerView students;
    RecyclerView.LayoutManager layoutManager;
    AttendeeAdapter adapter;

    ArrayList<Student> members;

    static final int REQUEST_CODE = 2;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference reference = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendees_list);

        students = (RecyclerView) findViewById(R.id.recyclerView2);

        layoutManager = new LinearLayoutManager(this);
        students.setHasFixedSize(true);
        adapter = new AttendeeAdapter(members);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if(requestCode == REQUEST_CODE) {
                String event = getIntent().getStringExtra("Event");
                reference = reference.child("College").child(Student.currentStudent.college).child("All Clubs").child(event).child("Members");
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot data: dataSnapshot.getChildren()) {
                            String name = data.child("Name").getValue().toString();
                            String email = data.child("Email").getValue().toString();
                            Student temp = new Student();
                            temp.name = name;
                            temp.email = email;
                            members.add(temp);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        } catch(Exception e) {

        }
    }
}
