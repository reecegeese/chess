package services;

import dataaccess.authdataaccess.AuthDataAccess;
import dataaccess.gamedataaccess.GameDataAccess;
import dataaccess.userdataaccess.SQLUserDataAccess;

public class DeleteService {

    public static void deleteService() throws RuntimeException {
        //DataAccess -> Database
        AuthDataAccess.deleteAuthData();
        GameDataAccess.deleteGameData();
        SQLUserDataAccess.deleteUserData();
    }
}