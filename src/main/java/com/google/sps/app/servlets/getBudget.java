package com.google.sps.app.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;
import com.google.sps.model.BudgetResponse;
import com.google.sps.model.Trip;
import com.google.sps.model.budgetResponse;

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

        List<String> associatedTripIDs = getAssociatedTrips();

        BudgetResponse responseObj = new BudgetResponse();
        for (String tripID: associatedTripIDs){

            double contribution;
            try {
                contribution = getEstimatedContribution(tripID);
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
}