import java.util.ArrayList;

@AutoValue
public class UserList {
    List<String> associatedUsernames;
    List<Long> associatedUserIDs;
    List<String> failedToAdd;

    public UserList(List<String> usernamesAssociated, List<Long> userIDsAssociated){
        this.associatedUsernames = usersAssociated;
        this.associatedUserIDs = userIDsAssociated;
        this.failedToAdd = new ArrayList<String>();
    }
}
