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

    public static void deleteAuthData() {
        allAuthData = new ArrayList<>();
    }
}