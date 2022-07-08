package com.google.sps.app.servlets;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.KeyFactory;
import com.google.sps.util.UUIDs;
import com.google.sps.util.Validator;

@WebServlet("/SignUp")
public class SignUp extends HttpServlet{

    private static final String USER_NAME_PARAM = "text-input-user-name";
    // Formatted string for validation error messages
    private static final String VALIDATOR_ERROR_MESSAGE = "<p class=\"error\">Invalid Username: %s<p>";
    // Validation error message discriptions
    private static final String IMPROPER_CHARACTERS = VALIDATOR_ERROR_MESSAGE + "Improper characters. Please use letters and numbers only.";
    private static final String USERNAME_LENGTH = VALIDATOR_ERROR_MESSAGE + "Usernames must be between 1 and 64 characters";
    // Taken user name error message
    private static final String USERNAME_TAKEN = "<p class=\"error\">Username Taken: <a href=\"/LogIn\">Login</a></p>";
    
    /**
     * Returns a response for the POST request in standard text not JSON.
     * @param request a post request. Expects "text-input-user-name" with a username in the request param.
     * @return the user's ID if the username is avalible and an error message if the username is taken.
     * If succesful the method also creates a User entity and pushes it to datastore.
     * The error message contains a hyperlink that can be integrated with the front end by setting the innerHTML
     * of a container to the error message, which contains an <a> tag pointing to the login servlet.
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("Started SignUp doPost");

        String username = request.getParameter(USER_NAME_PARAM).trim();
        System.out.println("Username gotten: username=" + username);

        System.out.println("Checking for errors");
        // gaurd clause for invalid usernames
        String error = Validator.validateUserName(username);
        if(!error.isEmpty()) {
            System.out.println("Validation Error");
            error = error == "length" ? USERNAME_LENGTH : IMPROPER_CHARACTERS;
            System.out.println("error=" + error);
            response.getWriter().println(String.format(VALIDATOR_ERROR_MESSAGE, error));
            System.out.println("Resopnse written from validation error");
            return;
        }

        // gaurd clause for already taken usernames
        else if (!Validator.userNameAvalible(username)) {
            System.out.println("Username taken error");
            response.getWriter().println(USERNAME_TAKEN);
            System.out.println("Response written from username taken error");
            return;
        }
        System.out.println("No errors found");

        long userId = writeToDatastore(username);
        System.out.println("User pushed and userID=" + userId);

        response.getWriter().println(userId);
        System.out.println("Response written from success");
        // upon success redirect user to portfolio
        // response.sendRedirect("");
    }


    /**
     * Creates a user Entity to add to
     * datastore.
     * @param username
     * @return randomly generated ID
     */
    public long writeToDatastore(String username){
        System.out.println("Entering writeToDatastore()");
        long userId = UUIDs.generateID();
        System.out.println("Id generated. ID=" + userId);

        Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
        System.out.println("Datastore created");
        KeyFactory keyFactory = datastore.newKeyFactory().setKind("User");
        System.out.println("keyFactory created");
        FullEntity userEntity = Entity.newBuilder(keyFactory.newKey())
            .set("username", username)
            .set("userId", userId)
            .build();
        System.out.println("Entity built");
        datastore.put(userEntity);
        System.out.println("Entity pushed");
        
        System.out.println("Leaving writeToDatastore()");
        return userId;
    }
}
