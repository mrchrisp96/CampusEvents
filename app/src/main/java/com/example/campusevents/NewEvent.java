package com.example.campusevents;

import java.util.ArrayList;
import java.util.Date;

public class NewEvent {

    String location, time, name, whichOne, phone, description;
    // location is building, roomNum is number of room in building
    ArrayList<String> tags = new ArrayList<String>();
    Date date;

    public static NewEvent newEvent = new NewEvent();


    public NewEvent() {
        this.location = location;
        this.time = time;
        this.name = name;
        this.whichOne = whichOne;
        this.phone = phone;
        this.description = description;
        this.tags = tags;
        this.date = date;
    }
}
