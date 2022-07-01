
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
import com.google.protobuf.Timestamp;
import com.google.protobuf.util.Timestamps;
import com.google.sps.model.Category;
import com.google.sps.model.Event;
import com.google.sps.util.UUIDs;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.UUID;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.text.StringEscapeUtils;

@WebServlet("/NewEvent")
public class NewEventServlet extends HttpServlet {

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
        KeyFactory keyFactor = datastore.newKeyFactory().setKind("Event");
        FullEntity eventEntity = Entity.newBuilder(keyFactor.newKey())
                .set("eventID", newEvent.getID().toString())
                .set("title", newEvent.getTitle().trim())
                .set("estimatedCost", newEvent.getEstimatedCost())
                .set("location", newEvent.getLocation())
                .set("date", newEvent.getDate().toString())
                .build();
        datastore.put(eventEntity);
    }

    public String validateInput(HttpServletRequest request) {
        if (!request.getParameter("text-input-title").matches("[\\w*\\s*]*")) {
            return "Invalid title";
        }
        try {
            float estimatedCost = Float.parseFloat(request.getParameter("text-input-estimatedCost"));
        } catch (NumberFormatException e) {
            return "Invalid estimatedCost";
        }

        if(parseInputDate(request)==null){
            return "Invalid date";
        }
        //no idea how to validate address for now.

        return "";
    }

    public Event getEvent(HttpServletRequest request){
        // Get the value entered in the form.
        String textValuetitle = StringEscapeUtils.escapeHtml4(request.getParameter("text-input-title"));
        float estimatedCost = Float
                .parseFloat(request.getParameter("text-input-estimatedCost"));

        UUID eventID = UUIDs.generateID();
        String location = StringEscapeUtils.escapeHtml4(request.getParameter("text-input-location"));
        Date date = parseInputDate(request);
        return new Event(eventID,textValuetitle,location,date,estimatedCost);
    }

    public Date parseInputDate(HttpServletRequest request){
        Date date;
        try {
            date = DateFormat.getDateInstance().parse(request.getParameter("text-input-date"));
        } catch (ParseException e) {
            return null;
        }
        return date;
    }

}