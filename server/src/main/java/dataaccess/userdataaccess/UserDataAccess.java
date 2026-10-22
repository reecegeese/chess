package dataaccess.userdataaccess;

import model.UserData;
import dataaccess.AlreadyTakenException;

import java.util.ArrayList;
import java.util.List;

public class UserDataAccess {
    static List<UserData> allUserData = new ArrayList<>();

    static public void addUserData(UserData user) {
        allUserData.add(user);
    }

    static public void findUserData(UserData user) {
        for (UserData registeredUser: allUserData) {
            try {
                if (user == registeredUser) {
                    throw new AlreadyTakenException("Error: already taken");
                }
            } catch (AlreadyTakenException e){
                System.out.println("Exception caught: " + e.getMessage());
            }
        }
    }

    public static void deleteUserData() {
        allUserData = new ArrayList<>();
    }
}