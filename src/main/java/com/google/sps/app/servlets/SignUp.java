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
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.KeyFactory;
import com.google.gson.Gson;
import com.google.sps.util.UUIDs;
import com.google.sps.util.Validator;

@WebServlet("/SignUp")
public class SignUp extends HttpServlet{

    private static final String USER_NAME_PARAM =
        "text-input-user-name";
    
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("USER_NAME_PARAM").trim();
        
        if(!Validator.validUserName(username)) {
            response.getWriter().println("Invalid Username: Please only use letters and numbers.");
            return;
        } else if (!Validator.userNameAvalible(username)) {
            response.getWriter().println("Username Taken: <a href=\"/LogIn\">Login</a>");
            return;
        }

        long userId = writeToDatastore(username);

        response.getWriter().println(userId);
    }

    public long writeToDatastore(String username){
        long userId = UUIDs.generateID();

        Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
        KeyFactory keyFactory = datastore.newKeyFactory().setKind("User");
        FullEntity userEntity = Entity.newBuilder(keyFactory.newKey())
            .set("username", username)
            .set("userId", userId)
            .build();
        datastore.put(userEntity);
        
        return userId;
    }
}
