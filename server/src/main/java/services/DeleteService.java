package services;

import dataaccess.authdataaccess.SQLAuthDataAccess;
import dataaccess.gamedataaccess.GameDataAccess;
import dataaccess.userdataaccess.SQLUserDataAccess;

public class DeleteService {

    public static void deleteService() throws RuntimeException {
        //DataAccess -> Database
        SQLAuthDataAccess.deleteAuthData();
        GameDataAccess.deleteGameData();
        SQLUserDataAccess.deleteUserData();
    }
}