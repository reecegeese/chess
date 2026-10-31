package services;

import dataaccess.sql.SQLAuthDataAccess;
import dataaccess.sql.SQLGameDataAccess;
import dataaccess.sql.SQLUserDataAccess;

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