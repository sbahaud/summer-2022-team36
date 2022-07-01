package com.google.sps.app.servlets;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.KeyFactory;
import com.google.datastore.v1.Entity;

@WebServlet("/SignUp")
public class SignUp extends HttpServlet{
    
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if(!validInput(request)) {
            response.getWriter().println("Invalid Username");
            return;
        }

        String username = request.getParameter("text-input-user-name");
        UUID userId = writeToDatastore(username);

        response.getWriter().println(userId);
    }

    public UUID writeToDatastore(String username){
        UUID userId = UUID.fromString(username);

        Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
        KeyFactory keyFactory = datastore.newKeyFactory().setKind("Users");
        FullEntity tripEntity = Entity.newBuilder(KeyFactory.newKey())
            .set("userId", userId.toString())
            .build();
        
    }

    public boolean validInput(HttpServletRequest request) {
        return request.getParameter("text-input-user-name").matches("[\\w\\d]*");
    }
}
