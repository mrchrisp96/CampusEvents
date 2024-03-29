package com.example.campusevents;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

/* Class written by Christopher Park */

public class login extends AppCompatActivity {

    Button signin, signup;
    EditText email, password;
    String myEmail;
    TextView name, welcome, firstTime;

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference reference = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        name = (TextView) findViewById(R.id.textView);
        welcome = (TextView) findViewById(R.id.textView2);
        firstTime = (TextView) findViewById(R.id.textView4);
        name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 50f);
        welcome.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f);
        firstTime.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f);

        email = (EditText) findViewById(R.id.editText);
        password = (EditText) findViewById(R.id.editText2);

        auth = FirebaseAuth.getInstance();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
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
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                if(Objects.requireNonNull(auth.getCurrentUser()).isEmailVerified()) {
                                    GlobalFirebase.user = auth.getCurrentUser();

                                    String[] splitEmail = Objects.requireNonNull(GlobalFirebase.user.getEmail()).split("@");
                                    Student.currentStudent.username = splitEmail[0].toLowerCase();
                                    Student.currentStudent.uid = auth.getUid();
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

        signin.setLayoutParams(new LinearLayout.LayoutParams(450, ActionBar.LayoutParams.WRAP_CONTENT));
        signin.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25f);

        signup = (Button) findViewById(R.id.signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login.this, SignUp.class);
                startActivity(intent);
            }
        });
        signup.setLayoutParams(new LinearLayout.LayoutParams(300, 100));
        signup.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f);
    }

    public void retrieveStudentInfo(final String username) {
        reference.child("Students").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data: dataSnapshot.getChildren()) {
                    if(data.getKey().equals(username)) {
                        Student.currentStudent.name = data.child("Name").getValue().toString();
                        Student.currentStudent.college = data.child("College").getValue().toString();
                        Student.currentStudent.email = data.child("Email").getValue().toString();

                        email.setText("");
                        password.setText("");

                        Intent intent = new Intent(login.this, MainScreen.class);
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
