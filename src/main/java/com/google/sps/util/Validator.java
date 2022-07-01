package com.google.sps.util;

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
}
