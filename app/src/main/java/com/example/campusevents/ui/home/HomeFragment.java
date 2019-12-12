package com.example.campusevents.ui.home;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campusevents.Event;
import com.example.campusevents.Events;
import com.example.campusevents.MyAccount;
import com.example.campusevents.OnItemClickListener;
import com.example.campusevents.R;
import com.example.campusevents.Student;
import com.example.campusevents.TodayEventsAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    RecyclerView todaysEvents;
    RecyclerView.LayoutManager layoutManager;
    TodayEventsAdapter adapter;
    ArrayList<Events> events = new ArrayList<>();
    Button account;

    //static final int REQUEST_CODE = 2;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference reference = database.getReference();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24f);
        populateMyEvents();

        account = (Button) root.findViewById(R.id.button3);
        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyAccount.class);
                startActivity(intent);
            }
        });

        todaysEvents = (RecyclerView) root.findViewById(R.id.todaysEvents);
        layoutManager = new LinearLayoutManager(getActivity());
        adapter = new TodayEventsAdapter(events, new OnItemClickListener() {
            @Override
            public void onClick(Events event) {
                Intent intent = new Intent(getActivity(), Event.class);
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

        return root;
    }

    public void populateMyEvents() {
        System.out.println(Student.currentStudent.college);
        System.out.println(reference);
        System.out.println(database);
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
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("Resume!");

    }

    @Override
    public void onPause() {
        super.onPause();
        System.out.println("OnPause!");
    }
}