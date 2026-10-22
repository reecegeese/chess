package services;

import dataaccess.AlreadyTakenException;
import dataaccess.authdataaccess.AuthDataAccess;
import dataaccess.gamedataaccess.GameDataAccess;
import dataaccess.userdataaccess.UserDataAccess;
import model.UserData;

public class UserService {

    public static void registerUser(String username, String password, String email) {
        try {
            UserDataAccess.findUserData(username, password, email);
        } catch (AlreadyTakenException e){
            System.out.println("Exception caught: " + e.getMessage());
        }
    }

    //public RegisterResult register(RegisterRequest registerRequest) {}
    //public LoginResult login(LoginRequest loginRequest) {}
    //public void logout(LogoutRequest logoutRequest) {}
}