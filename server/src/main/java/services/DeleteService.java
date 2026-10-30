package services;

import dataaccess.authdataaccess.SQLAuthDataAccess;
import dataaccess.gamedataaccess.SQLGameDataAccess;
import dataaccess.userdataaccess.SQLUserDataAccess;

public class DeleteService {

    public static void deleteService() throws RuntimeException {
        //DataAccess -> Database
        SQLAuthDataAccess.deleteAuthData();
        SQLGameDataAccess.deleteGameData();
        SQLUserDataAccess.deleteUserData();
    }
}