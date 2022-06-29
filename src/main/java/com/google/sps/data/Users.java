package com.google.sps.data;

public class Users {
    class UserTrip {
        String titile;
        String link;
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
        for (UserTrip.trip: trips){
            total+= trip.ammountOwed;
        }
        return total;
    }
}
