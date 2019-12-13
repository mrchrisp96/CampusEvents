package com.example.campusevents;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.provider.ContactsContract;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class MainScreen extends AppCompatActivity implements LocationListener, NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;

    LocationManager locationManager;
    Location location;
    double longitude, latitude;
    TextView title;
    RecyclerView nearbyEvents;
    RecyclerView.LayoutManager layoutManager;
    NearbyEventsAdapter adapter;
    ArrayList<Events> events;
    ArrayList<String> attendingClubs;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference reference = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
//        mAppBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.nav_home)
//                .setDrawerLayout(drawer)
//                .build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_view);
//        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
//        NavigationUI.setupWithNavController(navigationView, navController);

        //textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24f);

        title = (TextView) findViewById(R.id.text_home);
        title.setText("Here are your nearby events at\n" + Student.currentStudent.college);
        events = new ArrayList<>();
        populateMyEvents();

        if (this.checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        if (checkLocationPermission()) {
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 1, this);
//        }

        if(checkLocationPermission()) {
            LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if(location != null) {
                longitude = location.getLongitude();
                latitude = location.getLatitude();
            }
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 2, this);
            // changes every meter

        }
        nearbyEvents = (RecyclerView) findViewById(R.id.todaysEvents);
        layoutManager = new LinearLayoutManager(this);
        adapter = new NearbyEventsAdapter(events, new OnItemClickListener() {
            @Override
            public void onClick(Events event) {

            }
        });
        nearbyEvents.setLayoutManager(layoutManager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_screen, menu);
        TextView student = findViewById(R.id.welcomeStudent);
        student.setText(Student.currentStudent.name);

        TextView studentEmail = findViewById(R.id.welcomeEmail);
        studentEmail.setText(Student.currentStudent.email);
        return true;
    }

    public boolean checkLocationPermission() {
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = ContextCompat.checkSelfPermission(this, permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public void onLocationChanged(Location location) {
        longitude = location.getLongitude();
        latitude = location.getLatitude();
        System.out.println(latitude);
        System.out.println(longitude);
        events = new ArrayList<>();
        // Change recyclerview here
        database.getReference().child("College").child(Student.currentStudent.college).child("Clubs")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot data: dataSnapshot.getChildren()) {
                            if(data != null) {
                                if(data.hasChild("Coordinates")) {
                                    Events event = new Events();
                                    Location l1 = new Location("");
                                    l1.setLatitude(latitude);
                                    l1.setLongitude(longitude);

                                    double lat = (double) data.child("Coordinates").child("Latitude").getValue();
                                    double lon = (double) data.child("Coordinates").child("Longitude").getValue();
                                    Location l2 = new Location("");
                                    l1.setLatitude(lat);
                                    l1.setLongitude(lon);

                                    float meters = l1.distanceTo(l2);
                                    if(meters >= 2) {
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
                            }
                        } adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        if(id == R.id.nav_home) {
            Intent intent = new Intent(MainScreen.this, MainScreen.class);
            startActivity(intent);
        } else if(id == R.id.nav_acc) {
            Intent intent = new Intent(MainScreen.this, MyAccount.class);
            startActivity(intent);
        } else if(id == R.id.nav_allclubs) {
            Intent intent = new Intent(MainScreen.this, AllClubs.class);
            startActivity(intent);
        } else if(id == R.id.nav_myclubs) {
            Intent intent = new Intent(MainScreen.this, MyClubs.class);
            startActivity(intent);
        } else if(id == R.id.nav_newclub) {
            Intent intent = new Intent(MainScreen.this, CreateEvent.class);
            startActivity(intent);
        }

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    public void populateMyEvents() {
        attendingClubs = new ArrayList<>();

        reference = database.getReference().child("Students").child(Student.currentStudent.username).child("Attending Clubs");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    if(data != null) {
                        attendingClubs.add(data.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        reference = database.getReference().child("Students").child(Student.currentStudent.username).child("My Clubs");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Events event = new Events();

                    event.name = data.getKey();
                    event.host = data.child("Host").getValue().toString();
                    event.email = data.child("Email").getValue().toString();
                    event.phone = data.child("Phone").getValue().toString();
                    event.location = data.child("Location").getValue().toString();
                    event.time = data.child("Time").getValue().toString();
                    event.description = data.child("Description").getValue().toString();
                    if (attendingClubs.contains(event.name)) {
                        Student.currentStudent.memberClub.add(event);
                    }
                    Student.currentStudent.myEvents.add(event);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(MainScreen.this, login.class);
        startActivity(intent);
    }
}
