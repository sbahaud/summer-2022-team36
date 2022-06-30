package com.google.sps.model;

import java.util.Date;
import java.util.List;
import java.lang.Long;

public class Event {
    private long eventId;
    private String title;
    private String location;
    //uses user hashes
    private List<Long> participants;
    private Date date;
    private float estimatedCost;
    private float actualCost;
    private boolean paid;
    //user Hash
    private long paidBy;
    public Category tag;
    private boolean includeInBudget = true;
    //marks if the cost is split by all people or if it's a per person cost.
    private boolean splitCost = true;

    @Override
    public int compareTo(Object other){
        if (!(other instanceof Event)){
            throw new ClassCastException();
        }
        Event that = (Event)other;
        return this.date.compareTo(that.date);
    }
}
