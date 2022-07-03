package com.google.sps.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;


import com.google.cloud.datastore.BaseEntity;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;

public class DataStoreHelper {

    public static final Datastore DATASTORE = DatastoreOptions.getDefaultInstance().getService();
    private static final String USER_QUERY_TEMPLATE =
        "SELECT userId FROM User WHERE username=";

    static public Date parseInputDate(String textDate){
        Date date;
        try {
            date = DateFormat.getDateInstance().parse(textDate);
        } catch (ParseException e) {
            return null;
        }
        return date;
    }


    static public long queryUserID(String username) throws IllegalArgumentException {
        String gqlQuery = USER_QUERY_TEMPLATE + username;

        Query<?> query = Query.newGqlQueryBuilder(gqlQuery).build();
        QueryResults<?> results = DATASTORE.run(query);
        
        //checks if there are no results for the username
        if(!results.hasNext()){
            throw new IllegalArgumentException("Username doesn't exist. <a href\"/SignUp\">Please Signup</a>.");
        }

        //possible class cast or null pointer
        return ((BaseEntity<Key>) results.next()).getLong("userId");
    }
}