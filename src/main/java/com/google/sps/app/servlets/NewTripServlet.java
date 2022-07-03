
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
import com.google.sps.model.Trip;
import com.google.sps.util.UUIDs;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.text.StringEscapeUtils;

@WebServlet("/NewTrip")
public class NewTripServlet extends HttpServlet {

    private static String TITLE_PARAM = "text-input-title";
    private static String TOTAL_BUDGET_PARAM = "text-input-totalBudget";
    private static String START_DATE_PARAM = "text-input-start-date";
    private static String END_DATE_PARAM = "text-input-end-date";

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String error = validateInput(request);
        if (error.isEmpty()) {
            Trip newTrip = getTrip(request);
            writeToDatastore(newTrip);
            final Gson gson = new Gson();
            response.setContentType("application/json;");
            response.getWriter().println(gson.toJson(newTrip.tripID()));
        } 
        else {
            response.getWriter().println("Input information error: "+ error);
        }

        response.sendRedirect("https://summer22-sps-36.appspot.com/");
    }

    public void writeToDatastore(Trip newTrip) {
        Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
        KeyFactory keyFactor = datastore.newKeyFactory().setKind("Trip");
        FullEntity tripEntity = Entity.newBuilder(keyFactor.newKey())
                .set("tripID", newTrip.tripID())
                .set("title", newTrip.title().trim())
                .set("totalBudget", newTrip.totalBudget())
                .set("startDate", newTrip.start().toString())
                .set("endDate", newTrip.end().toString())
                .build();
        datastore.put(tripEntity);
    }

    public String validateInput(HttpServletRequest request) {
        if (!request.getParameter(TITLE_PARAM).matches("[\\w*\\s*]*")) {
            return "Invalid title";
        }
        try {
            float totalBudget = Float.parseFloat(request.getParameter(TOTAL_BUDGET_PARAM));
        } catch (NumberFormatException e) {
            return "Invalid totalBudget";
        }
        Date start = parseInputDate(request.getParameter(START_DATE_PARAM));
        Date end = parseInputDate(request.getParameter(END_DATE_PARAM));
        if(start==null){
            return "Invalid start date";
        }
        if(end==null){
            return "Invalid end date";
        }
        if(end.before(start))
            return "Start date should before end date";
        return "";
    }

    public Trip getTrip(HttpServletRequest request){
        // Get the value entered in the form.
        String textValuetitle = StringEscapeUtils.escapeHtml4(request.getParameter(TITLE_PARAM));
        float totalBudget = Float
                .parseFloat(request.getParameter(TOTAL_BUDGET_PARAM));

        long tripID = UUIDs.generateID();
        Date start = parseInputDate(request.getParameter(START_DATE_PARAM));
        Date end = parseInputDate(request.getParameter(END_DATE_PARAM));
        return Trip.create(tripID,textValuetitle,totalBudget,start,end);
    }

    public Date parseInputDate(String textDate){
        Date date;
        try {
            date = DateFormat.getDateInstance().parse(textDate);
        } catch (ParseException e) {
            return null;
        }
        return date;
    }

}