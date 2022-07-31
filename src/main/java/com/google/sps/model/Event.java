package com.google.sps.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.lang.Long;

public class Event implements Comparable<Event>{
    private String eventId;
    private String tripID;
    private String title;
    private String location;
    //uses user hashes
    private List<String> participants;
    private String date;
    private float estimatedCost;
    private float actualCost;
    private boolean paid;
    //user Hash
    private String paidBy;
    public Category tag;
    private boolean includeInBudget = true;
    //marks if the cost is split by all people or if it's a per person cost.
    private boolean splitCost = true;

    public Event(String eventId, String tripID, String title, String location, String date, float estimatedCost){
        this.eventId=eventId;
        this.tripID=tripID;
        this.title=title;
        this.location=location;
        this.date=date;
        this.estimatedCost=estimatedCost;
        this.paid = false;
        this.includeInBudget = true;
        this.splitCost = true;
        this.participants = new ArrayList<String>();

    }

    public String getID(){
        return eventId;
    }

    public String getTripID(){
        return tripID;
    }
    
    public String getDate(){
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

    public List<String> getParticipants(){
        return participants;
    }

    public void setParticipants(List<String> usernameList){
        this.participants = usernameList;
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
