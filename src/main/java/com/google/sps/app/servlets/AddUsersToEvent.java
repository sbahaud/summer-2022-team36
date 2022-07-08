package com.google.sps.app.servlets;

import java.util.List;

import javax.HttpServlet;

@WebServlet("/addUserToEvent")
public class AddUsersToEvent extends HttpServlet {
    
    private static String EVENT_ID = "javascript-input-event-id";
    private static String USERS = "text-input-user-list";

    /**
     * Reads an events userlist and adds users that aren't there yet.
     * @param request. Containing an event ID and
     * a comma seperated list of usernames too add.
     * @param response
     * @return response containing a JSON representation of the new username list
     * and a list of usernames that failed to add, with an error message.
     * @throws IOException
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //get paramaters
        Long id = response.getParameter(EVENT_ID);
        String[] usersToAdd = cleanInput(response.getParameter(USERS));
        
        //get lists
        List<String> usernames = getAssociatedEventUsernames();
        List<Long> userIDs = getAssociatedEventUserIDs();

        //add to lists
        UserList resp = addUsersToEvent(usersToAdd, usernames, userIDs);
        
        //push list
        String usernameStringList = listToString(usernames);
        String userIDStringList = listToString(userIDs);
        FullEntitiy eventEntity = getEventEntity(id);
        pushUserstoEvent(eventEntity, usernameStringList, userIDStringList);

        //return response
        Gson gson = new Gson();
        response.setContentType("appliation/json;");
        response.getWriter.println(gson.toJson(resp));
    }

    private String[] cleanInput(String input){
        input = input.replace(" ", "");
        String[] usersToAdd = participants.split(",");
    }

    private UserList addUsersToEvent(String[] usersToAdd, List<String> usernames, List<Long> userIDs) {
        for (int i = 0; i < usersToAdd.length; i++) {
            String username = usersToAdd[i];
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
    }

    private static FullEntity getEventEntity(Long eventID){
        String gqlQuery = "SELECT * FROM Event WHERE eventID=" + eventID;
        Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
        Query<?> query = Query.newGqlQueryBuilder(gqlQuery).build();
        QueryResults<?> results = datastore.run(query);

        return (FullEntity<Key>) results.next();
    }

    private static void pushUserstoEvent(FullEntity eventEntity, String usernames, String userIDs){
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
