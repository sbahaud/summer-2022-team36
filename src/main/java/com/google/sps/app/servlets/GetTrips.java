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
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;
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

import com.google.cloud.datastore.Value;

@WebServlet("/get-trips")
public class GetTrips extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String userID = request.getHeader("userID");
        List<Trip> Trips = getTrips(userID);
        if(Trips.isEmpty()){
            response.getWriter().println("No Trip exist. Please Create One.");
            return;
        }
        Gson gson = new Gson();
        response.setContentType("application/json;");
        response.getWriter().println(gson.toJson(Trips));
    }

    public List<Trip> getTrips(String userID) {
        QueryResults<Entity> results;
        try {
        results = queryDatastore(userID);
        }
        catch (IllegalArgumentException e) {
            return new ArrayList(); //an empty list
        }
        List<Trip> trips = new ArrayList<>();
        while (results.hasNext()) {
            Entity entity = results.next();

            String tripID = entity.getString("tripID");
            String title = entity.getString("title");
            float totalBudget = (float) entity.getDouble("totalBudget");
            Date start = DataStoreHelper.parseDataDate(entity.getString("startDate"));
            Date end = DataStoreHelper.parseDataDate(entity.getString("endDate"));
            List<String> participants = DataStoreHelper.convertToStringList(entity.getList("participants"));
            List<String> names = DataStoreHelper.convertToStringList(entity.getList("names"));
            trips.add(Trip.create(tripID, title, start, end, participants, names, totalBudget));
        }
        return trips;
    }

    public QueryResults<Entity> queryDatastore(String userID) throws IllegalArgumentException {

        Query<Entity> query =
        Query.newEntityQueryBuilder()
          .setKind("Trip")
          .setFilter(PropertyFilter.eq("participants", userID))
          .build();

        Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
        QueryResults<Entity> results = datastore.run(query);

        // checks if there are no results for the username
        if (!results.hasNext()) {
            throw new IllegalArgumentException("No Trip exist. Please Create One.");
        }

        return results;

    }
}
