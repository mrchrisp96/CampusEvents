package com.example.campusevents;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

/* Class written by Christopher Park */

public class NewEvent {

    String location, name, whichOne, phone, description, time;
    // location is building, roomNum is number of room in building
    ArrayList<String> tags = new ArrayList<String>();
    Date date;

    public static NewEvent newEvent = new NewEvent();


    public NewEvent() {
        this.location = location;
        this.name = name;
        this.whichOne = whichOne;
        this.phone = phone;
        this.description = description;
        this.tags = tags;
        this.date = date;
        this.time = time;
    }
}
