package dataaccess.authdataaccess;

import model.AuthData;

import java.util.UUID;
import java.util.ArrayList;
import java.util.List;

public class AuthDataAccess {
    static List<AuthData> allAuthData = new ArrayList<>();

    public static String createAuthToken() {
        return UUID.randomUUID().toString();
    }

    public static void addAuthData(String authToken, String username) {
        allAuthData.add(new AuthData(authToken, username));
    }

    static public boolean findAuthData(AuthData user) {
        //DataAccess -> Database
        System.out.println("looping");
        for (AuthData registeredAuthData: allAuthData) {
            if (registeredAuthData.username().equals(user.username())) {
                //DataAccess <- database
                return true;
            }
        }
        //DataAccess <- database
        return false;
    }

    public static void deleteAuthData() {
        allAuthData = new ArrayList<>();
    }
}