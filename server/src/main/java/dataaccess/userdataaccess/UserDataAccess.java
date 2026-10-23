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
        System.out.println("users stored: " + allUserData);
    }

    static public boolean findUserData(UserData user) {
        //DataAccess -> Database
        for (UserData registeredUser: allUserData) {
            if (registeredUser.username().equals(user.username())) {
                //DataAccess <- database
                return true;
            }
        }
        //DataAccess <- database
        return false;
    }

    public static void deleteUserData() {
        //DataAccess -> Database
        allUserData = new ArrayList<>();
    }
}