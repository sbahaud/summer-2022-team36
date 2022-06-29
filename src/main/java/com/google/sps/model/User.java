package com.google.sps.model;

import java.util.List;

public class User {
    private long userId;
    private String userName;
    private List<Trip> trips;

    public float totalSpent(){
        float total = 0;
        for (Trip trip: trips){
            total += trip.getParticipantAmmountPaid(userId);
        }
        
        return total;
    }

    public float totalDue(){
        float total = 0;
        for (Trip trip: trips){
            total+= trip.getParticipantAmmountOwed(userId);
        }

        return total - totalSpent();
    }
}
