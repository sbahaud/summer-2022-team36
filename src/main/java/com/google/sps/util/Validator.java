package com.google.sps.util;
import com.google.sps.util.UUIDs;

public class Validator {

    /**
     * @param String username from post request
     * @return  boolean, true if String is an acceptable username
     *          containing: letters, digits, and no symbols
     * may be updated later
     */
    public static boolean validUserName(String username) {
        return username.matches("[\\w\\d]*");
    }

    public static boolean userNameAvalible(String username){
        try {
            //searches for username
            long userID = DataStoreHelper.queryUserID(username);
        //user name could not be found in database
        } catch (IllegalArgumentException e) {
            //username must be avalible
            return true;
        }
        //if user id could be found it isn't avalible
        return false;
    }
}
