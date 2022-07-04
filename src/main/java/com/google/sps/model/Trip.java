package com.google.sps.model;

import com.google.auto.value.AutoValue;

import java.util.Date;
import java.util.HashSet;
import java.lang.Long;
import java.util.Set;


@AutoValue
public abstract class Trip {

    static public Trip create(long tripID, String title, float totalBudget, Date start, Date end){
        return new AutoValue_Trip(tripID, title, start, end, new HashSet<Long>(), new HashSet<Long>(), 0.0F, totalBudget);
    }
    abstract public long tripID();
    abstract public String title();
    abstract public Date start();
    abstract public Date end();

    abstract public Set<Long> participants();
    abstract public Set<Long> eventIds();
    abstract public float totalCost();
    abstract public float totalBudget();
}

/*public long getParticipantAmmountOwed(long userId){
    //TODO: calculate participant ammount owed
}

public long getParticipantAmmountPaid(long userId){
    //TODO: calculate ammount paid  by a user
}*/
