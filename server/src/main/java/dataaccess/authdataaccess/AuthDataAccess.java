package dataaccess.authdataaccess;

import model.AuthData;

import java.util.ArrayList;
import java.util.List;

public class AuthDataAccess {
    static List<AuthData> allAuthData = new ArrayList<>();

    public void addAuthData(String authToken, String username) {
        allAuthData.add(new AuthData(authToken, username));
    }

    public static void deleteAuthData() {
        allAuthData = new ArrayList<>();
    }
}