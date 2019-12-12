package com.example.campusevents.ui.notifications;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
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

import com.example.campusevents.CreateEvent;
import com.example.campusevents.Event;
import com.example.campusevents.Events;
import com.example.campusevents.MyEventsAdapter;
import com.example.campusevents.OnItemClickListener;
import com.example.campusevents.R;
import com.example.campusevents.Student;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class NotificationsFragment extends Fragment {

    // This shows all the events that you have signed up for

    private NotificationsViewModel notificationsViewModel;
    ArrayList<Events> events;
    RecyclerView myEvents;
    RecyclerView.LayoutManager layoutManager;
    MyEventsAdapter myEventsAdapter;
    Button addEvent;

    static final int REQUEST_CODE = 2;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference reference = database.getReference();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel = ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_myevents, container, false);
        final TextView textView = root.findViewById(R.id.text_notifications);
        notificationsViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24f);

        events = Student.currentStudent.myEvents;
        populateMyEvents();

        myEvents = (RecyclerView) root.findViewById(R.id.myEvents);

        layoutManager = new LinearLayoutManager(root.getContext());
        myEvents.setHasFixedSize(true);
        myEventsAdapter = new MyEventsAdapter(events, new OnItemClickListener() {
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
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
        myEvents.setLayoutManager(layoutManager);
        myEvents.setAdapter(myEventsAdapter);

        addEvent = (Button) root.findViewById(R.id.addEvent);
        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CreateEvent.class);
                startActivity(intent);
            }
        });

        addEvent.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f);

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 600);
        myEvents.setLayoutParams(lp);
        return root;
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

    @Override
    public void onResume() {
        super.onResume();
        populateMyEvents();
        myEvents.getAdapter().notifyDataSetChanged();
    }
}