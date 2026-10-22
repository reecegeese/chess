package dataaccess.userdataaccess;

import model.UserData;
import dataaccess.AlreadyTakenException;

import java.util.ArrayList;
import java.util.List;

public class UserDataAccess {
    static List<UserData> allUserData = new ArrayList<>();

    static public void addUserData(String username, String password, String email) {
        allUserData.add(new UserData(username, password, email));
    }

    static public void findUserData(String username, String password, String email) {
        UserData currentUser = new UserData(username, password, email);
        for (UserData registeredUser: allUserData) {
            if (currentUser == registeredUser) {
                throw new AlreadyTakenException("Error: already taken");
            }
        }
    }

    public static void deleteUserData() {
        allUserData = new ArrayList<>();
    }
}