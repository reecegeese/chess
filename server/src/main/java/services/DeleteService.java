package services;

import dataaccess.authdataaccess.AuthDataAccess;
import dataaccess.gamedataaccess.GameDataAccess;
import dataaccess.userdataaccess.UserDataAccess;

public class DeleteService {

    public static void deleteService() {
        AuthDataAccess.deleteAuthData();
        GameDataAccess.deleteGameData();
        UserDataAccess.deleteUserData();
    }
}