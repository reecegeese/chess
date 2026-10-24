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

    public static void deleteAuthToken(String authToken) {
        //DataAccess <- database
        allAuthData.removeIf(registeredAuthData -> registeredAuthData.authToken().equals(authToken));
    }

    static public boolean findAuthToken(String authToken) {
        //DataAccess -> Database
        for (AuthData registeredAuthData: allAuthData) {
            if (registeredAuthData.authToken().equals(authToken)) {
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