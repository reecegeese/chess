package services;

import dataaccess.authdataaccess.SQLAuthDataAccess;
import dataaccess.gamedataaccess.SQLGameDataAccess;
import dataaccess.userdataaccess.SQLUserDataAccess;

import java.sql.SQLException;

public class DeleteService {

    public static void deleteService() throws RuntimeException {
        //DataAccess -> Database
        try {
            SQLAuthDataAccess.deleteAuthData();
            SQLGameDataAccess.deleteGameData();
            SQLUserDataAccess.deleteUserData();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}