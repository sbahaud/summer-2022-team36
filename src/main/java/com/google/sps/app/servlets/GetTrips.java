package com.google.sps.app.servlets;

import com.google.cloud.datastore.BaseEntity;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.KeyFactory;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.sps.model.Trip;
import com.google.sps.util.DataStoreHelper;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/get-trips")
public class GetTrips extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String userID = request.getParameter("userID");
        List<Trip> Trip = getTrips(userID);
        if(Trip.isEmpty()){
            response.getWriter().println("No Trip exist. Please Create One.");
        }
        Gson gson = new Gson();
        response.setContentType("application/json;");
        response.getWriter().println(gson.toJson(Trip));
    }

    public List<Trip> getTrips(String userID) {
        QueryResults<?> results;
        try {
        results = queryDatastore(userID);
        }
        catch (IllegalArgumentException e) {
            return new ArrayList(); //an empty list
        }
        List<Trip> trips = new ArrayList<>();
        while (results.hasNext()) {
            BaseEntity<Key> entity = (BaseEntity<Key>) results.next();

            long tripID = entity.getLong("tripID");
            String title = entity.getString("title");
            float totalBudget = (float) entity.getDouble("totalBudget");
            Date start = DataStoreHelper.parseInputDate(entity.getString("startDate"));
            Date end = DataStoreHelper.parseInputDate(entity.getString("endDate"));
            trips.add(Trip.create(tripID, title, totalBudget, start, end));
        }
        return trips;
    }

    public QueryResults<?> queryDatastore(String userID) throws IllegalArgumentException {

        Datastore datastore = DatastoreOptions.getDefaultInstance().getService();

        String gqlQuery = "select * from Trip where participant=" + userID;

        Query<?> query = Query.newGqlQueryBuilder(gqlQuery).build();
        QueryResults<?> results = datastore.run(query);

        // checks if there are no results for the username
        if (!results.hasNext()) {
            throw new IllegalArgumentException("No Trip exist. Please Create One.");
        }

        return results;

    }
}
