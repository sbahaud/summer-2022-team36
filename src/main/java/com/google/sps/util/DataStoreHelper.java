package com.google.sps.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.google.cloud.datastore.BaseEntity;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.DatastoreException;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;

public class DataStoreHelper {

    private static final String USER_QUERY_TEMPLATE =
        "SELECT userId FROM User WHERE username=";

    //parse date in html's input format
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
    //parse date in datastore's format
    public static Date parseDataDate(String textDate){
        Date date;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd hh:mm:ss zzz yyyy");
            date = dateFormat.parse(textDate);
        } catch (ParseException e) {
            return null;
        }
        return date;
    }
    
    public static String queryUserID(String username) throws IllegalArgumentException {
        Query<Entity> query =
          Query.newEntityQueryBuilder()
            .setKind("User")
            .setFilter(PropertyFilter.eq("username", username))
            .build();
        Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
        QueryResults<Entity> results = datastore.run(query);
        if (!results.hasNext()) {
            throw new IllegalArgumentException("No Trip exist. Please Create One.");
        }
        String userID = results.next().getString("userId");

        return userID;
    }
}
