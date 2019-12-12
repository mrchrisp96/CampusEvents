package com.example.campusevents;

import java.util.ArrayList;

/* Class written by Christopher Park */

public class Student {

    public String username, name, college, email, uid;
    public ArrayList<Events> myEvents = new ArrayList<>(); // stores the events by the students
    public ArrayList<String> interests = new ArrayList<>(); // lists of interests by student

    public static Student currentStudent = new Student();

    public Student() {
        this.username = username;
        this.name = name;
        this.email = email;
        this.college = college;
        this.myEvents = myEvents;
        this.interests = interests;
        this.uid = uid;
    }
}
