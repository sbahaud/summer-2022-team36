
package com.google.sps.app.servlets;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.KeyFactory;
import com.google.cloud.datastore.ListValue;
import com.google.cloud.datastore.Value;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.StringValue;
import com.google.cloud.datastore.StructuredQuery.OrderBy;
import com.google.gson.Gson;
import com.google.sps.model.Trip;
import com.google.sps.util.DataStoreHelper;
import com.google.sps.util.UUIDs;
import com.google.sps.util.Validator;
import java.io.IOException;
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
    private static String PARTICIPANTS_PARAM = "text-input-participants";
    private static String USERNAME_PARAM = "text-input-userName";
    private static String USERID_PARAM = "text-input-userID";

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String error = validateInput(request);
        if (error.isEmpty()) {
            Trip newTrip = getTrip(request);
            writeToDatastore(newTrip);
            final Gson gson = new Gson();
            response.setContentType("application/json;");
            //response.getWriter().println(gson.toJson(newTrip.tripID()));
            response.getWriter().print(newTrip.tripID());
        } 
        else {
            System.out.println(request.getParameter(START_DATE_PARAM));
            System.out.println("Input information error: "+ error);
            response.getWriter().println("Input information error: "+ error);
        }
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
                .set("participants", DataStoreHelper.convertToValueList(newTrip.participants()))
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
        Date start = DataStoreHelper.parseInputDate(request.getParameter(START_DATE_PARAM));
        Date end = DataStoreHelper.parseInputDate(request.getParameter(END_DATE_PARAM));
        if(start==null){
            return "Invalid start date";
        }
        if(end==null){
            return "Invalid end date";
        }
        if(end.before(start))
            return "Start date should before end date";

        String participantInput = StringEscapeUtils.escapeHtml4(request.getParameter(PARTICIPANTS_PARAM));
        List<String> participants = DataStoreHelper.splitUserList(participantInput);
        for (String userToAdd: participants){
            String validationErrors = Validator.validateUserName(userToAdd);
            if (!validationErrors.isEmpty()) {
                return String.format("Invalid %s for user %s", validationErrors, userToAdd);
            }
            String userIDToAdd;
            try {
                userIDToAdd = DataStoreHelper.queryUserID(userToAdd);
            } catch (IllegalArgumentException e) {
                return String.format("User %s not found", userToAdd);
            }
        }
        return "";
    }

    public Trip getTrip(HttpServletRequest request){
        // Get the value entered in the form.
        String textValuetitle = StringEscapeUtils.escapeHtml4(request.getParameter(TITLE_PARAM));
        float totalBudget = Float
                .parseFloat(request.getParameter(TOTAL_BUDGET_PARAM));

        String tripID = UUIDs.generateID();
        Date start = DataStoreHelper.parseInputDate(request.getParameter(START_DATE_PARAM));
        Date end = DataStoreHelper.parseInputDate(request.getParameter(END_DATE_PARAM));
        String participantInput = StringEscapeUtils.escapeHtml4(request.getParameter(PARTICIPANTS_PARAM));
        List<String> participantIds = new ArrayList<String>();
        List<String> checkerduplicate = new ArrayList<String>();
        List<String> participants = DataStoreHelper.splitUserList(participantInput);
        checkerduplicate.add(request.getParameter(USERNAME_PARAM));
        participantIds.add(request.getParameter(USERID_PARAM));
        for (String name: participants){
            if(checkerduplicate.contains(name))
                continue;
            participantIds.add(DataStoreHelper.queryUserID(name));
            checkerduplicate.add(name);
        }
        return Trip.create(tripID,textValuetitle,participantIds, totalBudget,start,end);
    }

}