package com.example.campusevents;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

public class MyAccount extends AppCompatActivity {

    TextView name, college, email;
    Button signout;


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
    }
}
