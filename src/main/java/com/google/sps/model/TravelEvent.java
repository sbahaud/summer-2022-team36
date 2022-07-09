package com.google.sps.model;

import java.util.Date;

public class TravelEvent extends Event {

    public TravelEvent(long eventId, long tripId, String title, String location, Date date, float estimatedCost) {
        super(eventId, tripId, title, location, date, estimatedCost);
        //TODO Auto-generated constructor stub
    }
}