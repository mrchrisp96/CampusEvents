package com.example.campusevents;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

public class MyAccount extends AppCompatActivity {

    TextView name, college, email;
    Button signout, editEvent, editClub;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        name = (TextView) findViewById(R.id.accountName);
        college = (TextView) findViewById(R.id.accountCollege);
        email = (TextView) findViewById(R.id.accountEmail);

        name.setText(Student.currentStudent.name);
        college.setText(Student.currentStudent.college);
        email.setText(Student.currentStudent.email);

        signout = (Button) findViewById(R.id.button2);
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MyAccount.this, login.class);
                startActivity(intent);
            }
        });

        editEvent = (Button) findViewById(R.id.editEvents);
        editEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MyAccount.this, MyEvents.class);
                intent.putExtra("Event", "Edit Event");
                startActivity(intent);
            }
        });

        editClub = (Button) findViewById(R.id.editClubs);
        editClub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MyAccount.this, MyEvents.class);
                intent.putExtra("Club", "Edit Club");
                startActivity(intent);
            }
        });

//        editClub.setLayoutParams(new RelativeLayout.LayoutParams(440, ViewGroup.LayoutParams.WRAP_CONTENT));
        editClub.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f);
//
//        editEvent.setLayoutParams(new RelativeLayout.LayoutParams(400, ViewGroup.LayoutParams.WRAP_CONTENT));
        editEvent.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f);

        signout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT));
        signout.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f);
    }
}
