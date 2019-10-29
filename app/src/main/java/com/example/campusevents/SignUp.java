package com.example.campusevents;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import static android.R.layout.simple_spinner_item;

public class SignUp extends AppCompatActivity {

    Button create;
    Spinner college;
    TextView passNum, passLength, passMatch;
    EditText name, email, password, confirmPassword;
    ArrayList<String> allColleges;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference reference;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        name = (EditText) findViewById(R.id.fullName);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        confirmPassword = (EditText) findViewById(R.id.confirmPass);
        passNum = (TextView) findViewById(R.id.passNum);
        passLength = (TextView) findViewById(R.id.passLength);
        passMatch = (TextView) findViewById(R.id.passMatch);

        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference();

        allColleges = new ArrayList<>();
//        allColleges.add("George Mason University");
//        allColleges.add("James Madison University");
//        allColleges.add("George Washington University");
//        allColleges.add("Virginia Tech");
//        allColleges.add("University of Virginia");
//        allColleges.add("Georgetown University");
//        allColleges.add("College of William and Mary");
//        allColleges.add("Virginia Commonwealth University");
//        allColleges.add("University of Richmond");

        reference = firebaseDatabase.getReference("College");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    allColleges.add(snapshot.getKey());
                }
                college = (Spinner) findViewById(R.id.spinner);
                ArrayAdapter<String> collegeAdapter = new ArrayAdapter<String>(SignUp.this, simple_spinner_item, allColleges);
                collegeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                collegeAdapter.notifyDataSetChanged();
                college.setAdapter(collegeAdapter);
                college.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Student.currentStudent.college = parent.getItemAtPosition(position).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        create = (Button) findViewById(R.id.button);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String checkEmail = email.getText().toString().toLowerCase().trim();
                String checkPass = password.getText().toString().trim();
                String checkConfirmPass = confirmPassword.getText().toString().trim();

                if (name.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter your full name.", Toast.LENGTH_SHORT).show();
                } else if (checkEmail.equals("") || !validEmail()) {
                    Toast.makeText(getApplicationContext(), "Please enter a valid email for your school.", Toast.LENGTH_SHORT).show();
                } else if (checkPass.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter a password.", Toast.LENGTH_SHORT).show();
                } else if (!validPass()) {
                    Toast.makeText(getApplicationContext(), "Your password must be at least 8 characters with 1 number.", Toast.LENGTH_SHORT).show();
                } else if (!checkPass.equals(checkConfirmPass)) {
                    Toast.makeText(getApplicationContext(), "Your passwords must match.", Toast.LENGTH_SHORT).show();
                } else {
                    mAuth = FirebaseAuth.getInstance();
                    mAuth.createUserWithEmailAndPassword(checkEmail, checkPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                Objects.requireNonNull(mAuth.getCurrentUser()).sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            String[] splitEmail = Objects.requireNonNull(email.getText().toString()).split("@");
                                            String userName = splitEmail[0].toLowerCase();

                                            reference = firebaseDatabase.getReference().child("Students").child(userName);
                                            reference.child("Name").setValue(name.getText().toString());
                                            reference.child("College").setValue(Student.currentStudent.college);
                                            reference.child("Email").setValue(checkEmail);

                                            Intent intent = new Intent(SignUp.this, Success.class);
                                            startActivity(intent);
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(SignUp.this, "Email account already exists. Please log in or confirm your email.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                confirmPassword.setText("");
                if(password.getText().length() < 8) {
                    passLength.setText("\u274C 8 letters");
                } else if(password.getText().length() >= 8) {
                    SpannableString spantext = new SpannableString("\u2713 8 letters");
                    spantext.setSpan(new ForegroundColorSpan(Color.parseColor("#008000")), 0, 1, 0);
                    passLength.setText(spantext);
                }
                if(!password.getText().toString().matches(".*\\d.*")) {
                    passNum.setText("\u274C 1 number");
                } else if(password.getText().toString().matches(".*\\d.*")) {
                    SpannableString spantext = new SpannableString("\u2713 1 number");
                    spantext.setSpan(new ForegroundColorSpan(Color.parseColor("#008000")), 0, 1, 0);
                    passNum.setText(spantext);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        confirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!password.getText().toString().equals(confirmPassword.getText().toString())) {
                    passMatch.setText("\u274C No match");
                } else {
                    SpannableString spantext = new SpannableString("\u2713 Match");
                    spantext.setSpan(new ForegroundColorSpan(Color.parseColor("#008000")), 0, 1, 0);
                    passMatch.setText(spantext);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

//    public void populateColleges() {
//        reference = firebaseDatabase.getReference("College");
//        reference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for(String college: allColleges) {
//                    reference.child(college).setValue(college);
//                }
//                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }

    private boolean validEmail() {
        if(email.getText().toString().contains("gmu.edu")) {
            return true;
        }
        return false;
    }

    private boolean validPass() {
        char ch;
        String pass = password.getText().toString();
        boolean length = false;
        boolean number = false;
        if(pass.length() >= 8) {
            length = true;
        }
        for(int i = 0; i < pass.length(); i++) {
            ch = pass.charAt(i);
            if(Character.isDigit(ch)) {
                number = true;
            }
        }
        return length & number;
    }
}
