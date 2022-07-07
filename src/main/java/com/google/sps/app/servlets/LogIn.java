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
import com.google.sps.util.DataStoreHelper;
import com.google.sps.util.Validator;

@WebServlet("/LogIn")
public class LogIn extends HttpServlet {

    private static final String USER_NAME_PARAM =
        "text-input-user-name";

    /**
     * Returns a response for the POST request in standard text not JSON.
     * @param request a post request. Expects "text-input-user-name" with a username in the request param.
     * @return the user's ID if the username can be found and an error message if the username can't be found.
     * The error message contains a hyperlink that can be integrated with the front end by setting the innerHTML
     * of a container to the error message, which contains an <a> tag pointing to the signup servlet.
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter(USER_NAME_PARAM).trim();

        // if(!Validator.validUserName(username)) {
        //     response.getWriter().println("Invalid Username: Improper characters. Please use letters and numbers only.");
        //     return;
        // }

        long userId;
        try {
            userId = DataStoreHelper.queryUserID(username);
        } catch (IllegalArgumentException e) {
            response.getWriter().println(e.getMessage());
            return;
        }

        response.getWriter().println(userId);
    }
}
