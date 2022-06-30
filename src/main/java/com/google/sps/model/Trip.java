package com.google.sps.model;

import java.util.List;

public class Trip {
    private long tripID;
    private String title;
    private List<Long> participantIds;
    private List<Long> eventIds;
    public float totalCost;
    private float totalBudget;
    
    //TODO: calculate participant ammount owed
    // public long getParticipantAmmountOwed(long userId){}
    
    //TODO: calculate ammount paid  by a user
    // public long getParticipantAmmountPaid(long userId){}
}
