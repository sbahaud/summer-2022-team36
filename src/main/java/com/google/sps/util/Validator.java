package com.google.sps.util;
import com.google.sps.util.UUIDs;

import com.google.cloud.datastore.DatastoreException;

public class Validator {

    /**
     * @param String username from post request
     * @return  boolean, true if String is an acceptable username
     *          containing: letters, digits, and no symbols
     * may be updated later
     */

    public static String validateUserName(String username){
        String error = "";
        if (!validUserNameCharacters(username)){
            error = "characters";
        }
        if (!validUserNameLength(username)){
            error = "length";
        }
        return error;
    }
    
    private static boolean validUserNameCharacters(String username) {
        return username.matches("[\\w\\d]*");
    }

    private static boolean validUserNameLength(String username){
        return username.length() > 0 && username.length() < 65;
    }

    public static boolean userNameAvalible(String username){
        try {
            //searches for username
            long userID = DataStoreHelper.queryUserID(username);
        //user name could not be found in database
        } catch (com.google.cloud.datastore.DatastoreException e) {
            //username must be avalible
            return true;
        }
        //if user id could be found it isn't avalible
        return false;
    }
}
