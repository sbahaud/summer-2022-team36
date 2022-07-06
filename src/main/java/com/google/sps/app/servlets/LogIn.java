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
     * @param "text-input-user-name"
     * Accepts a post parameter with a username
     * If the username is found
     * @return the user's ID is returned
     * If the username is NOT found
     * @return an error message with a hyperlink
     * The hyperlink can be integrated with the front end
     * by setting the innerHTML of a container to the
     * error message, which contains an <a> tag
     * Note: HTTP response is in standard text not JSON
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter(USER_NAME_PARAM).trim();
        if(!Validator.validUserName(username)) {
            response.getWriter().println("Invalid Username: Improper characters. Please use letters and numbers only.");
            return;
        }

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
