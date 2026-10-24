package services;

import dataaccess.AlreadyTakenException;
import dataaccess.BadRequestException;
import dataaccess.UnauthorizedException;
import dataaccess.authdataaccess.AuthDataAccess;
import dataaccess.userdataaccess.UserDataAccess;
import model.UserData;

public class UserService {

    public static String registerUser(UserData user) throws AlreadyTakenException, BadRequestException {
        //Service -> DataAccess
        //Check for bad request
        if (user.username() == null || user.password() == null || user.email() == null) {
            throw new BadRequestException("Error: bad request");
        }
        //If user is not in database
        if (!UserDataAccess.findUserData(user)) {
            //Service -> DataAccess
            UserDataAccess.addUserData(user);
            String authToken = AuthDataAccess.createAuthToken();
            AuthDataAccess.addAuthData(authToken, user.username());
            return "\"" + user.username() + "\"\":\"\", \"" + authToken + "\":\"\"";
        }
        throw new AlreadyTakenException("Error: already taken");
    }

    public static String loginUser(UserData user) throws BadRequestException, UnauthorizedException{
        //Service -> DataAccess
        //Check for bad request
        if (user.username() == null || user.password() == null) {
            throw new BadRequestException("Error: bad request");
        }
        if (UserDataAccess.findUserData(user)) {
            //Service -> DataAccess
            String authToken = AuthDataAccess.createAuthToken();
            AuthDataAccess.addAuthData(authToken, user.username());
            return "\"" + user.username() + "\"\":\"\", \"" + authToken + "\":\"\"";
        }
        throw new UnauthorizedException("Error: unauthorized");
    }

    public static void logoutUser(String authToken) throws UnauthorizedException{
        //Service -> DataAccess
        if (AuthDataAccess.findAuthToken(authToken)) {
            //Service -> DataAccess
            AuthDataAccess.deleteAuthToken(authToken);
            return;
        }
        throw new UnauthorizedException("Error: unauthorized");
    }
}