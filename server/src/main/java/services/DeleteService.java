package services;

import dataaccess.userdataaccess.UserDataAccess;

public class DeleteService {

    public static void deleteService() {
        UserDataAccess.deleteUserData();
        //deleteGameData();
        //authdataaccess.deleteAuthData();
    }
}