package com.google.sps.app.servlets;

import javax.HttpServlet;

@WebServlet("/addUserToEvent")
public class AddUsersToEvent extends HttpServlet {
    
    private static String EVENT_ID = "javascript-input-event-id";

    /**
     * Reads an events userlist and adds users that aren't there yet.
     * @param request. Containing an event ID
     * @param response
     * @return response containing a JSON representation of the new username list
     * and a list of usernames that failed to add, with an error message.
     * @throws IOException
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long id = response.getParameter(EVENT_ID);

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
        removeDuplicates(usernames, newUsernames);
        for (String username: newUsernames){
            usernames += ","+username;
        }
        if (userIDs.charAt(0) == ',') {
            userIDs = userIDs.substring(1);
        }
        return usernames;
    }

    private static String addToEventsStringListOfUserIDs(String userIDs, List<String> newUserIDs){
        removeDuplicates(userIDs, newUserIDs);
        for (Long id: newUserIDs){
            userIDs += ","+id;
        }
        if (userIDs.charAt(0) == ',') {
            userIDs = userIDs.substring(1);
        }
        return userIDs;
    }

    private static void removeDuplicates(String original, List<T> newList){
        
    }
}
