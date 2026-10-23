package dataaccess.userdataaccess;

import model.UserData;
import dataaccess.AlreadyTakenException;

import java.util.ArrayList;
import java.util.List;

public class UserDataAccess {
    //Database
    static List<UserData> allUserData = new ArrayList<>();

    static public void addUserData(UserData user) {
        //DataAccess -> Database
        allUserData.add(user);
    }

    static public boolean findUserData(UserData user) {
        //DataAccess -> Database
        for (UserData registeredUser: allUserData) {
            if (user == registeredUser) {
                //DataAccess <- database
                return false;
            }
        }
        //DataAccess <- database
        return true;
    }

    public static void deleteUserData() {
        //DataAccess -> Database
        allUserData = new ArrayList<>();
    }
}