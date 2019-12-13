package com.example.campusevents;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditDesc extends AppCompatActivity {

    String name, email, phone, location, time, description;
    EditText newDesc;
    Button update;
    int numChars = 0, previousLength;
    TextView characters;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference reference = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_desc);

        name = getIntent().getStringExtra("Name");
        time = getIntent().getStringExtra("Time");
        phone = getIntent().getStringExtra("Phone");
        location = getIntent().getStringExtra("Location");
        description = getIntent().getStringExtra("Description");

        characters = (TextView) findViewById(R.id.editDescChars);
        characters.setText(numChars + "/1000");

        newDesc = (EditText) findViewById(R.id.editDesc);
        newDesc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                previousLength = s.length();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean backspaceClicked = previousLength > s.length();
                if(!backspaceClicked && s.length() == 1000) {
                    characters.setTextColor(Color.RED);
                } else if(backspaceClicked) {
                    characters.setTextColor(Color.BLACK);
                } else if(s.length() > previousLength) {
                    numChars++;
                }
                characters.setText(s.length() + "/1000");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        newDesc.setText(description);

        update = (Button) findViewById(R.id.updateButton);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference = reference.child("College").child(Student.currentStudent.college).child("Clubs").child(name);
                reference.child("Host").setValue(Student.currentStudent.name);
                reference.child("Email").setValue(Student.currentStudent.email);
                reference.child("Phone").setValue(phone);
                reference.child("Location").setValue(location);
                reference.child("Time").setValue(time);
                reference.child("Description").setValue(newDesc.getText().toString());

                reference = database.getReference().child("Students").child(Student.currentStudent.username)
                        .child("My Clubs").child(name);

                reference.child("Host").setValue(Student.currentStudent.name);
                reference.child("Email").setValue(Student.currentStudent.email);
                reference.child("Phone").setValue(phone);
                reference.child("Location").setValue(location);
                reference.child("Time").setValue(time);
                reference.child("Description").setValue(newDesc.getText().toString());

                Intent intent = new Intent(EditDesc.this, MainScreen.class);
                startActivity(intent);
            }
        });
    }
}
