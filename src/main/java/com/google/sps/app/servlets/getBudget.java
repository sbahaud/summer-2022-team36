package com.google.sps.app.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.cloud.datastore.Value;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;
import com.google.sps.model.Trip;
import com.google.sps.model.BudgetResponse;
import com.google.sps.util.DataStoreHelper;

@WebServlet("/get-budget")
public class getBudget extends HttpServlet{

    /**
     * Sends a JSON response containing all the estimated contributions and budgets of trips associated with the user
     * @param req contains a userID
     * @param resp returns JSON  with list of budgets and expected contributions
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userID = req.getHeader("userID");

        List<String> associatedTripIDs = getAssociatedTrips(userID);

        BudgetResponse responseObj = new BudgetResponse();
        for (String tripID: associatedTripIDs){
            double contribution;
            try {
                contribution = getEstimatedContribution(userID, tripID);
            } catch (IllegalArgumentException e){
                responseObj.addToErrors("Could not calculate expected contribution");
                continue;
            }
            
            double tripBudget;
            try {
                tripBudget = getTripBuget(tripID);
            } catch (IllegalArgumentException e) {
                responseObj.addToErrors("Could not get trip budget");
                continue;
            }
            
            //no errors
            responseObj.addToHTML(tripBudget, contribution);
        }
    }

    private List<String> getAssociatedTrips(String tripID) {
        Query<Entity> query =
          Query.newEntityQueryBuilder()
            .setKind("Trip")
            .setFilter(PropertyFilter.eq("tripID", tripID))
            .build();
        Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
        QueryResults<Entity> results = datastore.run(query);
        if (!results.hasNext()){
            throw new IllegalArgumentException("Trip could not be found");
        }

        List<Value<String>> associatedTrips = results.next().getList("tripIDs");
        return DataStoreHelper.convertToStringList(associatedTrips);
    }

    private double getTripBuget(String tripID) {
        Query<Entity> query =
          Query.newEntityQueryBuilder()
            .setKind("Trip")
            .setFilter(PropertyFilter.eq("tripID", tripID))
            .build();
        Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
        QueryResults<Entity> results = datastore.run(query);
        if (!results.hasNext()){
            throw new IllegalArgumentException("Trip could not be found");
        }

        return results.next().getDouble("totalBudget");
    }

    private double getEstimatedContribution(String userID, String tripID) {
        List<String> associatedEvents = getAssociatedEvents(tripID);
        
        double sum = 0.0;
        for (String eventID : associatedEvents) {
            double cost = getEventCost(eventID);

            int numTripParticipants = getNumTripParticipants(tripID);
            int divisor = getSplitBy(eventID, userID, numTripParticipants);
            double contribution = divisor == 0 ? 0.0 : (cost / divisor);

            sum += contribution;
        }

        return sum;
    }

    private int getNumTripParticipants(String tripID) {
        Query<Entity> query =
          Query.newEntityQueryBuilder()
            .setKind("Trip")
            .setFilter(PropertyFilter.eq("tripID", tripID))
            .build();
        Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
        QueryResults<Entity> results = datastore.run(query);
        if (!results.hasNext()) {
            throw new IllegalArgumentException("Trip could not be found");
        }

        return DataStoreHelper.convertToStringList(results.next().getList("participants")).size();
    }

    private List<String> getAssociatedEvents(String tripID) {
        Query<Entity> query =
          Query.newEntityQueryBuilder()
            .setKind("Event")
            .setFilter(PropertyFilter.eq("tripID", tripID))
            .build();
        Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
        QueryResults<Entity> results = datastore.run(query);
        
        List<String> eventIDs = new ArrayList<>();
        while (results.hasNext()){
            Entity event = results.next();
            String id = event.getString("eventID");
            eventIDs.add(id);
        }

        return eventIDs;
    }

    private double getEventCost(String eventID) {
        Query<Entity> query =
          Query.newEntityQueryBuilder()
            .setKind("Event")
            .setFilter(PropertyFilter.eq("eventId", eventID))
            .build();
        Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
        QueryResults<Entity> results = datastore.run(query);
        if (!results.hasNext()){
            throw new IllegalArgumentException("Event could not be found");
        }

        return results.next().getDouble("estimatedCost");
    }

    private int getSplitBy(String eventID, String userID, int numTripParticipants) {
        Query<Entity> query =
          Query.newEntityQueryBuilder()
            .setKind("Event")
            .setFilter(PropertyFilter.eq("eventID", eventID))
            .build();
        Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
        QueryResults<Entity> results = datastore.run(query);
        if (!results.hasNext()){
            throw new IllegalArgumentException("Event could not be found");
        }

        List<Value<String>> datastoreEventParticipants = results.next().getList("participants");
        List<String> eventParticipants = DataStoreHelper.convertToStringList(datastoreEventParticipants);
        if (eventParticipants.size() == 0) {
            return numTripParticipants;
        } else if (!eventParticipants.contains(userID)) { // user not included in participant
            return 0;
        } else {
            return eventParticipants.size();
        }
    }
}