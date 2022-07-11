package com.google.sps.model;

import com.google.auto.value.AutoValue;

import java.util.Date;
import java.util.HashSet;
import java.lang.Long;
import java.util.ArrayList;
import java.util.Set;
import java.util.List;


@AutoValue
public abstract class Trip {

    static public Trip create(String tripID, String title, List<String> participants, float totalBudget, Date start, Date end){
        return new AutoValue_Trip(tripID, title, start, end ,participants, new ArrayList<String>(), 0.0F, totalBudget);
    }
    abstract public String tripID();
    abstract public String title();
    abstract public Date start();
    abstract public Date end();

    abstract public List<String> participants();
    abstract public List<String> eventIds();
    abstract public float totalCost();
    abstract public float totalBudget();
}

/*public long getParticipantAmmountOwed(long userId){
    //TODO: calculate participant ammount owed
}

public long getParticipantAmmountPaid(long userId){
    //TODO: calculate ammount paid  by a user
}*/
