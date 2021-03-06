package com.google.sps.app.servlets;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.cloud.datastore.DatastoreException;
import com.google.sps.util.DataStoreHelper;
import com.google.sps.util.Validator;

@WebServlet("/LogIn")
public class LogIn extends HttpServlet {

    private static final String USER_NAME_PARAM = "text-input-user-name";
    // Formatted string for validation error messages
    private static final String VALIDATOR_ERROR_MESSAGE = "<p class=\"error\">Invalid Username: %s<p>";
    // Validation error message discriptions
    private static final String IMPROPER_CHARACTERS = VALIDATOR_ERROR_MESSAGE + "Improper characters. Please use letters and numbers only.";
    private static final String USER_NAME_LENGTH = VALIDATOR_ERROR_MESSAGE + "Usernames must be between 1 and 64 characters";
    // Username not found error message
    private static final String USERNAME_NOT_FOUND = "<p class=\"error\">Username not found <a href=\"userSignup.html\">Sign up</a></p>";

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

        String error = Validator.validateUserName(username);
        if(!error.isEmpty()) {
            error = error.equals("length") ? USER_NAME_LENGTH : IMPROPER_CHARACTERS;
            response.getWriter().print(String.format(VALIDATOR_ERROR_MESSAGE, error));
            return;
        }
        String userId;
        try {
            userId = DataStoreHelper.queryUserID(username);
        } catch (IllegalArgumentException e) {
            response.getWriter().print(USERNAME_NOT_FOUND);
            return;
        }

        response.getWriter().print(userId);
        // upon success redirect user to portfolio
        // response.sendRedirect("");
    }
}
