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
import com.google.sps.model.Event;
import com.google.sps.util.dataStoreHelper;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/get-event")
public class GetEvent extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String tripID = request.getParameter("tripID");
        List<Event> Event = getEvent(tripID);
        Gson gson = new Gson();
        response.setContentType("application/json;");
        response.getWriter().println(gson.toJson(Event));
    }

    public List<Event> getEvent(String userID) {
        QueryResults<?> results = queryDatastore(userID);

        List<Event> events = new ArrayList<>();
        while (results.hasNext()) {
            BaseEntity<Key> entity = (BaseEntity<Key>) results.next();

            long eventID = entity.getLong("eventID");
            String title = entity.getString("title");
            float estimatedCost = (float) entity.getDouble("estimatedCost");
            String location = entity.getString("location");
            Date date = dataStoreHelper.parseInputDate(entity.getString("date"));
            events.add(new Event(eventID, title, location, date, estimatedCost));
        }
        return events;
    }

    public QueryResults<?> queryDatastore(String tripID) throws IllegalArgumentException {

        Datastore datastore = DatastoreOptions.getDefaultInstance().getService();

        String gqlQuery = "select * from Event where tripID=" + tripID;

        Query<?> query = Query.newGqlQueryBuilder(gqlQuery).build();
        QueryResults<?> results = datastore.run(query);

        // checks if there are no results for the username
        if (!results.hasNext()) {
            throw new IllegalArgumentException("No Event exist. Please Create One.");
        }

        return results;

    }
}
