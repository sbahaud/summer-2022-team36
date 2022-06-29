package com.google.sps.model;

import java.util.List;

public class Users {
    class UserTrip {
        String title;
        String linkToTrip;
        float ammountOwed;
        float ammountPaid;
    }

    private String userName;
    private List<UserTrip> trips;

    public float totalSpent(){
        float total = 0;
        for (UserTrip trip: trips){
            total += trip.ammountPaid;
        }
        return total;
    }

    public float totalDue(){
        float total = 0;
        for (UserTrip trip: trips){
            total+= trip.ammountOwed;
        }
        return total;
    }
}
