
package com.google.sps.app.servlets;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.KeyFactory;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.StructuredQuery.OrderBy;
import com.google.gson.Gson;
import com.google.sps.model.Category;
import com.google.sps.model.Event;
import com.google.sps.util.UUIDs;
import com.google.sps.util.DataStoreHelper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.text.StringEscapeUtils;

import io.opencensus.common.ServerStatsFieldEnums.Id;

@WebServlet("/NewEvent")
public class NewEventServlet extends HttpServlet {

    private static String TITLE_PARAM = "text-input-title";
    private static String ESTIMATED_PARAM = "text-input-estimatedCost";
    private static String DATE_PARAM = "text-input-date";
    private static String LOCATION_PARAM = "text-input-location";

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String error = validateInput(request);
        if (error.isEmpty()) {
            Event newEvent= getEvent(request);
            writeToDatastore(newEvent);

            final Gson gson = new Gson();
            response.setContentType("application/json;");
            response.getWriter().println(gson.toJson(newEvent.getID()));

        } else {
            response.getWriter().println("Input information error");
        }

        response.sendRedirect("https://summer22-sps-36.appspot.com/");
    }

    public void writeToDatastore(Event newEvent) {
        Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
        KeyFactory keyFactory = datastore.newKeyFactory().setKind("Event");
        FullEntity eventEntity = Entity.newBuilder(keyFactory.newKey())
                .set("eventID", newEvent.getID())
                .set("title", newEvent.getTitle().trim())
                .set("estimatedCost", newEvent.getEstimatedCost())
                .set("location", newEvent.getLocation())
                .set("date", newEvent.getDate().toString())
                .setProperty("associatedUsers", newEvent.getParticipants())
                .build();
        datastore.put(eventEntity);
    }

    public String validateInput(HttpServletRequest request) {
        if (!request.getParameter(TITLE_PARAM).matches("[\\w*\\s*]*")) {
            return "Invalid title";
        }
        try {
            float estimatedCost = Float.parseFloat(request.getParameter(ESTIMATED_PARAM));
        } catch (NumberFormatException e) {
            return "Invalid estimatedCost";
        }

        if(DataStoreHelper.parseInputDate(request.getParameter(DATE_PARAM))==null){
            return "Invalid date";
        }
        //no idea how to validate address for now.

        return "";
    }

    public Event getEvent(HttpServletRequest request){
        // Get the value entered in the form.
        String title = StringEscapeUtils.escapeHtml4(request.getParameter(TITLE_PARAM));
        float estimatedCost = Float
                .parseFloat(request.getParameter(ESTIMATED_PARAM));

        long eventID = UUIDs.generateID();
        String location = StringEscapeUtils.escapeHtml4(request.getParameter(LOCATION_PARAM));
        Date date = DataStoreHelper.parseInputDate(request.getParameter(DATE_PARAM));
        return new Event(eventID,title,location,date,estimatedCost);
    }

}