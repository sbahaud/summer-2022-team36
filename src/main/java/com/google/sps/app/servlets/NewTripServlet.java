
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
import com.google.sps.model.Trip;
import com.google.sps.util.IDgenerator;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.text.StringEscapeUtils;

@WebServlet("/NewTrip")
public class NewTripServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String error = validateInput(request);
        if (error.isEmpty()) {
            // Get the value entered in the form.
            String textValuetitle = StringEscapeUtils.escapeHtml4(request.getParameter("text-input-title"));
            float totalBudget = Float
                    .parseFloat(request.getParameter("text-input-totalBudget"));

            UUID tripID = IDgenerator.generateID();
            writeToDatastore(tripID, textValuetitle, totalBudget);
        } else {
            response.getWriter().println("Input information error");
        }

        response.sendRedirect("https://summer22-sps-36.appspot.com/");
    }

    public void writeToDatastore(UUID tripID, String textValuetitle, float totalBudget) {
        Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
        KeyFactory keyFactor = datastore.newKeyFactory().setKind("Trip");
        FullEntity tripEntity = Entity.newBuilder(keyFactor.newKey())
                .set("tripID", tripID.toString())
                .set("title", textValuetitle.trim())
                .set("totalBudget", totalBudget)
                .build();
        datastore.put(tripEntity);
    }

    public String validateInput(HttpServletRequest request) {
        if (!request.getParameter("text-input-title").matches("[\\w*\\s*]*")) {
            return "Invalid title";
        }
        try {
            float totalBudget = Float.parseFloat(request.getParameter("text-input-totalBudget"));
        } catch (NumberFormatException e) {
            return "Invalid totalBudget";
        }
        return "";
    }

}