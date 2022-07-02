package com.google.sps.app.servlets;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.cloud.datastore.BaseEntity;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
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

        long userId;
        try {
            userId = queryDatastore(username);
        } catch (IllegalArgumentException e) {
            response.getWriter().println(e.getMessage());
            return;
        }

        response.getWriter().println(userId);
    }

    public long queryDatastore(String username) throws IllegalArgumentException {

        Datastore datastore = DatastoreOptions.getDefaultInstance().getService();

        String gqlQuery = "select * from User where username=" + username;

        Query<?> query = Query.newGqlQueryBuilder(gqlQuery).build();
        QueryResults<?> results = datastore.run(query);
        
        //checks if there are no results for the username
        if(!results.hasNext()){
            throw new IllegalArgumentException("Username doesn't exist. Please Signup.");
        }

        //possible class cast or null pointer
        return ((BaseEntity<Key>) results.next()).getLong("userId");

    }
}
