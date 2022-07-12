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
import com.google.sps.model.Event;
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

@WebServlet("/get-events")
public class GetEvents extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String tripID = request.getParameter("tripID");
        List<Event> Events = getEvents(tripID);
        Gson gson = new Gson();
        response.setContentType("application/json;");
        response.getWriter().println(gson.toJson(Events));
    }

    public List<Event> getEvents(String tripID) {
        QueryResults<Entity> results = queryDatastore(tripID);

        List<Event> events = new ArrayList<>();
        while (results.hasNext()) {
            Entity entity = results.next();

            String eventID = entity.getString("eventID");
            String title = entity.getString("title");
            float estimatedCost = (float) entity.getDouble("estimatedCost");
            String location = entity.getString("location");
            Date date = DataStoreHelper.parseInputDate(entity.getString("date"));
            events.add(new Event(eventID, Long.parseLong(tripID),title, location, date, estimatedCost));
        }
        return events;
    }

    public QueryResults<Entity> queryDatastore(String tripID) throws IllegalArgumentException {

        Query<Entity> query =
        Query.newEntityQueryBuilder()
          .setKind("Event")
          .setFilter(PropertyFilter.eq("tripID", tripID))
          .build();

        Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
        QueryResults<Entity> results = datastore.run(query);
        // checks if there are no results for the username
        if (!results.hasNext()) {
            throw new IllegalArgumentException("No Event exist. Please Create One.");
        }

        return results;

    }
}
