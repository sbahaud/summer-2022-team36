package com.google.sps.model;

import java.util.ArrayList;
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

    public Event(long eventId, String title, String location, Date date, float estimatedCost){
        this.eventId=eventId;
        this.title=title;
        this.location=location;
        this.date=date;
        this.estimatedCost=estimatedCost;
        this.paid = false;
        this.includeInBudget = true;
        this.splitCost = true;
        this.participants = new ArrayList<Long>();

    }

    public long getID(){
        return eventId;
    }
    
    public Date getDate(){
        return date;
    }

    public String getTitle(){
        return title;
    }


    public String getLocation(){
        return location;
    }

    public float getEstimatedCost(){
        return estimatedCost;
    }

    public List<Long> getParticipants(){
        return participants;
    }

    @Override
    public int compareTo(Event that){
        int dateComparison = this.date.compareTo(that.date);
        if (dateComparison == 0) {
            return this.title.compareTo(that.title);
        }
        return dateComparison;
    }

    @Override
    public boolean equals(Object other){
        if (!(other instanceof Event)){
            return false;
        }
        Event that = (Event)other;
        //if both objects have the same date and name they should be equal
        return 0 == compareTo(that);

    }
}
