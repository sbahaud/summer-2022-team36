package com.google.sps.app.servlets;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.KeyFactory;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.gson.Gson;

@WebServlet("/LogIn")
public class LogIn extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if(!validInput(request)) {
            response.getWriter().println("Invalid Username");
            return;
        }

        String username = request.getParameter("text-input-user-name");

        UUID userId = readFromDatastore(username);

        response.getWriter().println(userId);
    }

    public UUID readFromDatastore(String username){

        Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
        Query<Entity> query = 
            Query.newEntityQueryBuilder().setKind(username).build();
        QueryResults<Entity> results = datastore.run(query);

        //TODO: how can I get a UUID
        return results.next().getLong("userId");
    }

    public boolean validInput(HttpServletRequest request) {
        return request.getParameter("text-input-user-name").matches("[\\w\\d]*");
    }
}
