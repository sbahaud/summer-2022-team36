package com.google.sps.util;

import com.google.cloud.datastore.DatastoreException;

public class Validator {

    /**
     * @param String username from post request
     * @return  boolean, true if String is an acceptable username
     *          containing: letters, digits, and no symbols
     * may be updated later
     */

    public static String validateUserName(String username){
        System.out.println("Started ValidateUserName()");
        String error = "";
        System.out.println("Checking for valid chars");
        if (!validUserNameCharacters(username)){
            error = "characters";
            System.out.println("error is chars");
        }
        System.out.println("Checking for valid length");
        if (!validUserNameLength(username)){
            error = "length";
            System.out.println("error is length");
        }
        return error;
    }
    
    private static boolean validUserNameCharacters(String username) {
        System.out.println("starting validUserNameCharacters");
        return username.matches("[\\w\\d]*");
    }

    private static boolean validUserNameLength(String username){
        System.out.println("starting validUserNameLength()");
        return username.length() > 0 && username.length() < 65;
    }

    public static boolean userNameAvalible(String username){
        System.out.println("starting userNameAvalible");
        try {
            System.out.println("looking for user");
            //searches for username
            long userID = DataStoreHelper.queryUserID(username);
            System.out.println("user found: id=" + userID);
        //user name could not be found in database
        } catch (com.google.cloud.datastore.DatastoreException e) {
            System.out.println("datastore error, assumed to be not found");
            //username must be avalible
            return true;
        }
        //if user id could be found it isn't avalible
        System.out.println("user found so not avalible");
        return false;
    }
}
