package com.example.campusevents;

import com.google.firebase.auth.FirebaseUser;

/* Class written by Christopher Park */

public class GlobalFirebase {

    static FirebaseUser user;
    static String username;

    public GlobalFirebase(String username, FirebaseUser user) {
        GlobalFirebase.username = username;
        GlobalFirebase.user = user;
    }

}
