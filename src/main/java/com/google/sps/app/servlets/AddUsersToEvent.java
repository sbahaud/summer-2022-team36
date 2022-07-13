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
import com.google.cloud.datastore.Value;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;
import com.google.datastore.v1.client.DatastoreHelper;
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
     * @param request contains an event ID and
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
        List<String> usersToAdd = cleanInput(request.getParameter(USERS));
        
        //get lists
        Entity eventEntity = getEventEntity(id);
        List<String> usernames = getAssociatedEventUsernames(eventEntity);
        List<String> userIDs = getAssociatedEventUserIDs(eventEntity);

        //add to lists
        UserList resp = addUsersToExistingUsers(usersToAdd, usernames, userIDs);

        pushUserstoEvent(eventEntity, resp);

        //return response
        Gson gson = new Gson();
        response.setContentType("appliation/json;");
        response.getWriter().println(gson.toJson(resp));
    }

    private List<String> getAssociatedEventUserIDs(Entity eventEntity) {
        List<Value<String>> datastoreList = eventEntity.getList("associatedUserIDs");
        List<String> list = DataStoreHelper.convertToStringList(datastoreList);
        return list;
    }

    private List<String> getAssociatedEventUsernames(Entity eventEntity) {
        List<Value<String>> usernames = eventEntity.getList("associatedUsernames");
        List<String> list = DataStoreHelper.convertToStringList(usernames);
        return list;
    }

    private List<String> cleanInput(String input){
        input = input.replace(" ", "");
        String[] strArr = input.split(",");
        return Arrays.asList(strArr);
    }

    private UserList addUsersToExistingUsers(List<String> usersToAdd, List<String> usernames, List<String> userIDs) {
        UserList responseObj = new UserList(usernames, userIDs);

        for (String userToAdd: usersToAdd){
            String validationErrors = Validator.validateUserName(userToAdd);
            if (!validationErrors.isEmpty()) {
                responseObj.addError(String.format("Invalid %s for user %s", validationErrors, userToAdd));
                continue;
            } else if (usernames.contains(userToAdd)){
                responseObj.addError(String.format("User %s already in list", userToAdd));
                continue;
            }

            String userIDToAdd;
            try {
                userIDToAdd = DataStoreHelper.queryUserID(userToAdd);
            } catch (IllegalArgumentException e) {
                responseObj.addError(String.format("User %s not found", userToAdd));
                continue;
            }

            //no error
            responseObj.addUsername(userToAdd);
            responseObj.addUserID(userIDToAdd);           

        }

        return responseObj;
    }

    private static Entity getEventEntity(String eventID) throws IllegalArgumentException {

        Query<Entity> query =
          Query.newEntityQueryBuilder()
            .setKind("Event")
            .setFilter(PropertyFilter.eq("eventID", eventID))
            .build();
        Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
        QueryResults<?> results = datastore.run(query);

        if (!results.hasNext()){
            System.out.println(String.format("Could not find event with ID %s", eventID));
            throw new IllegalArgumentException(String.format("Could not find event with ID %s", eventID));
        }

        return (Entity) results.next();
    }

    private static void pushUserstoEvent(Entity eventEntity, UserList updates){
        List<Value<String>> usernames = DataStoreHelper.convertToValueList(updates.getAssociatedEventUsernames());
        List<Value<String>> userIDs = DataStoreHelper.convertToValueList(updates.getAssociatedEventUserIDs());

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
