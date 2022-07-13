package com.google.sps.model;

import java.util.ArrayList;
import java.util.List;

import com.google.auto.value.AutoValue;

@AutoValue
public class UserList {
    List<String> associatedUsernames;
    List<String> associatedUserIDs;
    List<String> failedToAdd;

    public UserList(List<String> usernamesAssociated, List<String> userIDsAssociated){
        this.associatedUsernames = usernamesAssociated;
        this.associatedUserIDs = userIDsAssociated;
        this.failedToAdd = new ArrayList<String>();
    }

    public UserList(){
        this.failedToAdd = new ArrayList<String>();
    }

    public void addError(String msg){
        failedToAdd.add(msg);
    }

	public void setAssociatedUserIDs(List<String> userIDs) {
        this.associatedUserIDs = userIDs;
	}

	public void setAssociatedUsernames(List<String> usernames) {
        this.associatedUsernames = usernames;
	}
}
