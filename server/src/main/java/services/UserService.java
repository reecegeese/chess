package services;

import dataaccess.AlreadyTakenException;
import dataaccess.authdataaccess.AuthDataAccess;
import dataaccess.gamedataaccess.GameDataAccess;
import dataaccess.userdataaccess.UserDataAccess;
import model.UserData;
import model.AuthData;

public class UserService {

    public static AuthData registerUser(String username, String password, String email) {
        try {
            UserDataAccess.findUserData(username, password, email);
        } catch (AlreadyTakenException e){
            System.out.println("Exception caught: " + e.getMessage());
        }
        UserDataAccess.addUserData(username, password, email);
        String authToken = AuthDataAccess.createAuthToken();
        AuthDataAccess.addAuthData(authToken, username);
        return new AuthData(authToken, username);
    }

    //public RegisterResult register(RegisterRequest registerRequest) {}
    //public LoginResult login(LoginRequest loginRequest) {}
    //public void logout(LogoutRequest logoutRequest) {}
}