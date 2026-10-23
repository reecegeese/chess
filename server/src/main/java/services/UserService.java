package services;

import dataaccess.AlreadyTakenException;
import dataaccess.authdataaccess.AuthDataAccess;
import dataaccess.gamedataaccess.GameDataAccess;
import dataaccess.userdataaccess.UserDataAccess;
import model.UserData;
import model.AuthData;

public class UserService {

    public static String registerUser(UserData user) throws AlreadyTakenException{
        //Service -> DataAccess
        //If user is not in database
        if (!UserDataAccess.findUserData(user)) {
            //Service -> DataAccess
            UserDataAccess.addUserData(user);
            String authToken = AuthDataAccess.createAuthToken();
            String currentUsername = user.username();
            AuthDataAccess.addAuthData(authToken, currentUsername);
            return "\"" + currentUsername + "\"\":\"\", \"" + authToken + "\":\"\"";
        }
        throw new AlreadyTakenException("Error: already taken");
    }

    public static String loginUser(UserData user) {
        UserDataAccess.findUserData(user);
        String authToken = AuthDataAccess.createAuthToken();
        String currentUsername = user.username();
        AuthDataAccess.addAuthData(authToken, currentUsername);
        return "\"" + currentUsername + "\"\":\"\", \"" + authToken + "\":\"\"";
    }

    //public RegisterResult register(RegisterRequest registerRequest) {}
    //public LoginResult login(LoginRequest loginRequest) {}
    //public void logout(LogoutRequest logoutRequest) {}
}