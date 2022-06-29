package com.google.sps.model;

import java.util.List;

public class Users {
    private long id;
    private String userName;
    private List<Trip> trips;

    public float totalSpent(){
        float total = 0;
        for (Trip trip: trips){
            total += trip.getParticipantAmmountPaid(id);
        }
        
        return total;
    }

    public float totalDue(){
        float total = 0;
        for (Trip trip: trips){
            total+= trip.getParticipantAmmountOwed(id);
        }

        return total - totalSpent();
    }
}
