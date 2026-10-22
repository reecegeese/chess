package dataaccess.userdataaccess;

import model.UserData;

import java.util.ArrayList;
import java.util.List;

public class UserDataAccess {
    static List<UserData> allUserData = new ArrayList<>();

    public void addUserData(String username, String password, String email) {
        allUserData.add(new UserData(username, password, email));
    }

    public static void deleteUserData() {
        allUserData = new ArrayList<>();
    }
}