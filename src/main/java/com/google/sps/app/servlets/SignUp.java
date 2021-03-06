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
    private static final String USERNAME_TAKEN = "<p class=\"error\">Username Taken: <a href=\"userSignin.html\">Login</a></p>";
    
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
        String username = request.getParameter(USER_NAME_PARAM).trim();
        
        // gaurd clause for invalid usernames
        String error = Validator.validateUserName(username);
        if(!error.isEmpty()) {
            error = error.equals("length") ? USERNAME_LENGTH : IMPROPER_CHARACTERS;
            response.getWriter().print(String.format(VALIDATOR_ERROR_MESSAGE, error));
            return;
        }
        // gaurd clause for already taken usernames
        if (!Validator.userNameAvalible(username)) {
            response.getWriter().print(USERNAME_TAKEN);
            return;
        }

        String userId = writeToDatastore(username);

        response.getWriter().print(userId);
        // upon success redirect user to portfolio
        // response.sendRedirect("");
    }


    /**
     * Creates a user Entity to add to
     * datastore.
     * @param username
     * @return randomly generated ID
     */
    public String writeToDatastore(String username){
        String userId = UUIDs.generateID();

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
