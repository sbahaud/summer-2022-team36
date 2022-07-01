
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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
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
            // Get the value entered in the form.
            String textValuetitle = StringEscapeUtils.escapeHtml4(request.getParameter("text-input-title"));
            float estimatedCost = Float
                    .parseFloat(request.getParameter("text-input-estimatedCost"));

            UUID eventID = UUIDs.generateID();
            String location = StringEscapeUtils.escapeHtml4(request.getParameter("text-input-location"));
            String date = request.getParameter("text-input-date");
            writeToDatastore(eventID, textValuetitle, estimatedCost, location, date);

            final Gson gson = new Gson();
            response.setContentType("application/json;");
            response.getWriter().println(gson.toJson(eventID));

        } else {
            response.getWriter().println("Input information error");
        }


        response.sendRedirect("https://summer22-sps-36.appspot.com/");
    }

    public void writeToDatastore(UUID eventID, String textValuetitle, float estimatedCost, String location,  String date) {
        Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
        KeyFactory keyFactor = datastore.newKeyFactory().setKind("Event");
        FullEntity eventEntity = Entity.newBuilder(keyFactor.newKey())
                .set("eventID", eventID.toString())
                .set("title", textValuetitle.trim())
                .set("estimatedCost", estimatedCost)
                .set("location", location)
                .set("date", date.toString())
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
        try {
            Timestamp date = Timestamps.parse(request.getParameter("text-input-date"));
        } catch (ParseException e) {
            return "Invalid date";
        }
        //no idea how to validate address for now.

        return "";
    }

}