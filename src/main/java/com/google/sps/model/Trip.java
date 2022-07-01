package com.google.sps.model;

import com.google.auto.value.AutoValue;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@AutoValue
public abstract class Trip {

    static public Trip create(UUID tripID, String title, float totalBudget){
        return new AutoValue_Trip(tripID, title, new HashSet<Long>(), new HashSet<Long>(), 0.0F, totalBudget);
    }
    abstract public UUID tripID();
    abstract public String title();

    abstract public Set<UUID> participants();
    abstract public Set<UUID> eventIds();
    abstract public float totalCost();
    abstract public float totalBudget();
}

/*public long getParticipantAmmountOwed(long userId){
    //TODO: calculate participant ammount owed
}

public long getParticipantAmmountPaid(long userId){
    //TODO: calculate ammount paid  by a user
}*/
