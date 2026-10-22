package dataaccess.userdataaccess;

import model.UserData;

public class UserDataAccess {
    public UserData[] allUserData;

    public void addUserData() {
        allUserData = new UserData(String(username), String(username), String(username));
    }

    public void deleteUserData() {
        allUserData = new UserData[];
    }
}