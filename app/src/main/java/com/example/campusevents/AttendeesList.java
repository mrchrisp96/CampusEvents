package com.example.campusevents;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

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
    String event;
    TextView title;
    int count;

    ArrayList<Student> members;

    static final int REQUEST_CODE = 2;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference reference = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendees_list);

        title = (TextView) findViewById(R.id.textView28);

        students = (RecyclerView) findViewById(R.id.recyclerView2);
        members = new ArrayList<>();
        event = getIntent().getStringExtra("Event");
        populateMemberList();

        layoutManager = new LinearLayoutManager(this);
        adapter = new AttendeeAdapter(members);
        students.setLayoutManager(layoutManager);
        students.setAdapter(adapter);

        students.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
//        title.setText("Members (" + count + ")");
    }

    public void populateMemberList() {
        reference = reference.child("College").child(Student.currentStudent.college).child("Clubs").child(event).child("Members");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data: dataSnapshot.getChildren()) {
                    System.out.println(data.getKey());
                    String name = data.child("Name").getValue().toString();
                    String email = data.child("Email").getValue().toString();
                    Student temp = new Student();
                    temp.name = name;
                    temp.email = email;
                    members.add(temp);
                    count++;
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        System.out.println(requestCode);
//        System.out.println(resultCode);
//
//        if(requestCode == REQUEST_CODE) {
//        }
//    }
}
