package com.google.sps.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.google.cloud.datastore.BaseEntity;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;

public class Helper {

    private static final String USER_QUERY_TEMPLATE =
        "SELECT userId FROM User WHERE username=";

    public static Date parseInputDate(String textDate){
        Date date;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            date = dateFormat.parse(textDate);
        } catch (ParseException e) {
            return null;
        }
        return date;
    }
    
    public static long queryUserID(String username) throws IllegalArgumentException {
        String gqlQuery = USER_QUERY_TEMPLATE + username;

        Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
        Query<?> query = Query.newGqlQueryBuilder(gqlQuery).build();
        QueryResults<?> results = datastore.run(query);
        
        //checks if there are no results for the username
        if(!results.hasNext()){
            throw new IllegalArgumentException("Username doesn't exist. <a href\"/SignUp\">Please Signup</a>.");
        }

        //possible class cast or null pointer
        return ((BaseEntity<Key>) results.next()).getLong("userId");
    }
}
