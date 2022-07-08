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
import com.google.sps.model.Event;

public class DataStoreHelper {

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
    
    public static long queryUserID(String username) throws com.google.cloud.datastore.DatastoreException {
        String gqlQuery = USER_QUERY_TEMPLATE + username;

        Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
        Query<?> query = Query.newGqlQueryBuilder(gqlQuery).build();
        QueryResults<?> results = datastore.run(query);
        
        long userID = ((BaseEntity<Key>) results.next()).getLong("userId");
        
        //possible class cast or null pointer
        return userID;
    }

    public static String addUsersToEvent(Event eventObj, Long eventID, String inputParticipantList) {
        //removes spaces and formatting
        participants = participants.replace(" ", "");

        String[] usernames = participants.split(",");

        String errors = "";
        List<Long> userIDList = new ArrayList<Long>();
        List<String> usernameList = new ArrayList<String>();
        for (int i = 0; i < usernames.length; i++) {
            String username = usernames[i];
            String validationErrors = Validator.validateUserName(username);
            if (!validationErrors.isEmpty()) {
                errors += String.format("Invalid %s for user %s\n", validationErrors, username);
                continue;
            }
            
            try {
                long userID = DataStoreHelper.queryUserID(username);
            } catch (com.google.cloud.datastore.DatastoreException e) {
                errors += String.format("User %s not found", username);
                continue;
            }

            //no error
            userIDList.add(userID);
            usernameList.add(username);
        }

        eventObj.setParticipants(usernameList);

        FullEntity eventEntity = getEventEntity(eventID);
        pushUserIDstoEvent(eventEntity, eventID, usernameList, userIDList);
    }

    private static FullEntity getEventEntity(Long eventID){
        String gqlQuery = "SELECT * FROM User WHERE associatedUsers=" + eventID;
        Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
        Query<?> query = Query.newGqlQueryBuilder(gqlQuery).build();
        QueryResults<?> results = datastore.run(query);

        return (FullEntity<Key>) results.next();
    }

    private static void pushUserIDstoEvent(FullEntity eventEntity, Long eventID, List<String> newUsernames, List<Long> newUserIDs){
        String usernames = getEventUsernames();
        String userIDs = getEventUserIDs();

        usernames = addToEventsStringListOfUsernames(usernames, newUsernames);
        userIDs = addToEventsStringListOfUserIDs(userIDs, newUserIDs);

        eventEntity
        .set("associatedUsernames", usernames)
        .set("associatedUserIDs", userIDs)
        .build();
        datastore.put(eventEntity);
    }

    private static String addToEventsStringListOfUserString(String usernames, List<String> newUsernames){

    }

    private static String addToEventsStringListOfUserIDs(String userIDs, List<String> newUserIDs){
        
    }
}