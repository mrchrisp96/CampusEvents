package com.example.campusevents;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.campusevents.ui.home.HomeFragment;
import com.example.campusevents.ui.home.HomeViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

import java.util.Objects;


public class login extends AppCompatActivity {

    Button signin, signup;
    EditText email, password;
    String myEmail;

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference reference = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (EditText) findViewById(R.id.editText);
        password = (EditText) findViewById(R.id.editText2);

        auth = FirebaseAuth.getInstance();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NotNull FirebaseAuth firebaseAuth) {
                if(FirebaseAuth.getInstance().getCurrentUser() != null) {

                }
            }
        };

        signin = (Button) findViewById(R.id.signin);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(email.getText().toString()) || TextUtils.isEmpty(password.getText().toString())) {
                    Toast.makeText(login.this, "Please enter a valid email or password!", Toast.LENGTH_SHORT).show();
                } else {
                    myEmail = email.getText().toString().replaceAll(" ","");

                    auth.signInWithEmailAndPassword(email.getText().toString().toLowerCase().replaceAll(" ", ""), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NotNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                if(Objects.requireNonNull(auth.getCurrentUser()).isEmailVerified()) {
                                    GlobalFirebase.user = auth.getCurrentUser();

                                    String[] splitEmail = Objects.requireNonNull(GlobalFirebase.user.getEmail()).split("@");
                                    Student.currentStudent.username = splitEmail[0].toLowerCase();
                                    retrieveStudentInfo(Student.currentStudent.username);
                                } else {
                                    Toast.makeText(login.this, "Please verify your email first!", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(login.this, "Incorrect email or password.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        signup = (Button) findViewById(R.id.signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login.this, SignUp.class);
                startActivity(intent);
            }
        });
    }

    public void retrieveStudentInfo(final String username) {
        FirebaseDatabase.getInstance().getReference().child("Students").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data: dataSnapshot.getChildren()) {
                    if(data.getKey().equals(username)) {
                        Student.currentStudent.name = data.child("Name").getValue().toString();
                        Student.currentStudent.college = data.child("College").getValue().toString();
                        Student.currentStudent.email = data.child("Email").getValue().toString();
                        Intent intent = new Intent(login.this, MainActivity.class);
                        startActivity(intent);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authStateListener);
    }

    @Override
    public void onBackPressed() {
        this.finishAffinity();
    }
}
