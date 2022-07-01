package com.google.sps.app.servlets;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.gax.rpc.InvalidArgumentException;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.KeyFactory;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.gson.Gson;
import com.google.sps.util.Validator;

@WebServlet("/LogIn")
public class LogIn extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("text-input-user-name");
        if(!Validator.validUserName(username)) {
            response.getWriter().println("Invalid Username");
            return;
        }

        UUID userId;
        try {
            userId = readFromDatastore(username);
        } catch (IllegalArgumentException e) {
            response.getWriter().println(e.getMessage());
            return;
        }

        Gson gson = new Gson();

        response.setContentType("application/json;");
        response.getWriter().println(gson.toJson(userId));
    }

    public UUID readFromDatastore(String username) throws IllegalArgumentException {

        Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
        Query<Entity> query = 
            Query.newEntityQueryBuilder().setKind("User").build();
        QueryResults<Entity> results = datastore.run(query);
        
        while (results.hasNext()) {
            Entity entity = results.next();
            
            if (entity.getString("username").equals(username)){
                return UUID.fromString(entity.getString("userId"));
            }
        }
        throw new IllegalArgumentException("Username doesn't exist. Please Signup.");
    }
}
