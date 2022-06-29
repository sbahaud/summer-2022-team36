package com.google.sps.model;

import java.util.List;

public class Trip {
    private long tripID;
    private String title;
    private List<Long> participants;
    public float totalCost;
    private float totalBudget;
    
    public long getParticipantAmmountOwed(long userId){
        //TODO: calculate participant ammount owed
    }

    public long getParticipantAmmountPaid(long userId){
        //TODO: calculate ammount paid  by a user
    }
}
