package services;

import dataaccess.authdataaccess.SQLAuthDataAccess;
import dataaccess.gamedataaccess.MemoryGameDataAccess;
import dataaccess.userdataaccess.SQLUserDataAccess;

public class DeleteService {

    public static void deleteService() throws RuntimeException {
        //DataAccess -> Database
        SQLAuthDataAccess.deleteAuthData();
        MemoryGameDataAccess.deleteGameData();
        SQLUserDataAccess.deleteUserData();
    }
}