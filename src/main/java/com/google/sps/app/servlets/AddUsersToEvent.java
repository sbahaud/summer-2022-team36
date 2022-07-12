package com.google.sps.app.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.KeyFactory;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;
import com.google.gson.Gson;
import com.google.sps.model.UserList;
import com.google.sps.util.DataStoreHelper;
import com.google.sps.util.Validator;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
        String id = request.getParameter(EVENT_ID);
        String[] usersToAdd = cleanInput(request.getParameter(USERS));
        
        //get lists
        Entity eventEntity = getEventEntity(id);
        List<String> usernames = getAssociatedEventUsernames(eventEntity);
        List<String> userIDs = getAssociatedEventUserIDs(eventEntity);

        //add to lists
        UserList resp = addUsersToEvent(usersToAdd, usernames, userIDs);
        
        //push list
        String usernameStringList = listToString(usernames);
        String userIDStringList = listToString(userIDs);
        pushUserstoEvent(eventEntity, usernameStringList, userIDStringList);

        //return response
        Gson gson = new Gson();
        response.setContentType("appliation/json;");
        response.getWriter().println(gson.toJson(resp));
    }

    private String listToString(List<?> users) {
        //build up list
        String strList = "";
        for (Object item : users) {
            strList += item + ",";
        }
        //removes last comma
        strList = strList.substring(0,strList.length()-1);
        return strList;
    }

    private List<String> getAssociatedEventUserIDs(Entity eventEntity) {
        String stringList = eventEntity.getString("associatedUserIDs");
        String[] arrayList = stringList.split(",");
        List<String> list = new ArrayList<>();
        for (int i = 0; i < arrayList.length; i++){
            list.add((arrayList[i]));
        }
        return list;
    }

    private List<String> getAssociatedEventUsernames(Entity eventEntity) {
        String stringList = eventEntity.getString("associatedUsernames");
        String[] arrayList = stringList.split(",");
        List<String> list = Arrays.asList(arrayList);
        return list;
    }

    private String[] cleanInput(String input){
        input = input.replace(" ", "");
        return input.split(",");
    }

    private UserList addUsersToEvent(String[] usersToAdd, List<String> usernames, List<String> userIDs) {
        UserList responseObj = new UserList();
        
        for (int i = 0; i < usersToAdd.length; i++) {
            String username = usersToAdd[i];
            String validationErrors = Validator.validateUserName(username);
            if (!validationErrors.isEmpty()) {
                responseObj.addError(String.format("Invalid %s for user %s\n", validationErrors, username));
                continue;
            }
            String userID;
            try {
                userID = DataStoreHelper.queryUserID(username);
            } catch (com.google.cloud.datastore.DatastoreException e) {
                responseObj.addError(String.format("User %s not found", username));
                continue;
            }

            //no error
            usernames.add(username);
            userIDs.add(userID);
        }

        responseObj.setAssociatedUsernames(usernames);
        responseObj.setAssociatedUserIDs(userIDs);

        return responseObj;
    }

    private static Entity getEventEntity(String eventID){

        Query<Entity> query =
          Query.newEntityQueryBuilder()
            .setKind("Event")
            .setFilter(PropertyFilter.eq("eventID", eventID))
            .build();
        Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
        QueryResults<?> results = datastore.run(query);

        return (Entity) results.next();
    }

    private static void pushUserstoEvent(Entity eventEntity, String usernames, String userIDs){
        Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
        KeyFactory keyFactory = datastore.newKeyFactory().setKind("User");
        Key eventKey = eventEntity.getKey();
        FullEntity updatedEventEntity = Entity.newBuilder(keyFactory.newKey(eventKey.getId()))
            .set("eventID", eventEntity.getString("eventID"))
            .set("tripID", eventEntity.getString("tripID"))
            .set("title", eventEntity.getString("title"))
            .set("estimatedCost", eventEntity.getDouble("estimatedCost"))
            .set("location", eventEntity.getString("location"))
            .set("date", eventEntity.getString("date"))
            .set("associatedUsernames", usernames)
            .set("associatedUserIDs", userIDs)
        .build();
        datastore.put(updatedEventEntity);   
    }
}
