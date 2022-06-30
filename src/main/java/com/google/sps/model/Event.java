package com.google.sps.model;

import java.util.Date;
import java.util.List;
import java.lang.Long;

public class Event implements Comparable<Event>{
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
    public int compareTo(Event that){
        int dateComparison = this.date.compareTo(that.date);
        if (dateComparison == 0) {
            return this.title.compareTo(that.title);
        }
        return dateComparison;
    }
}
