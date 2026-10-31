package services;

import dataaccess.AlreadyTakenException;
import dataaccess.BadRequestException;
import dataaccess.UnauthorizedException;
import dataaccess.sql.SQLAuthDataAccess;
import dataaccess.sql.SQLUserDataAccess;
import model.UserData;

public class UserService {

    public static String registerUser(UserData user) throws AlreadyTakenException, BadRequestException {
        //Service -> DataAccess
        //Check for bad request
        if (user.username() == null || user.password() == null || user.email() == null) {
            throw new BadRequestException("Error: bad request");
        }
        //If user is not in database
        if (!SQLUserDataAccess.findUserData(user)) {
            //Service -> DataAccess
            SQLUserDataAccess.addUserData(user);
            String authToken = SQLAuthDataAccess.createAuthToken();
            SQLAuthDataAccess.addAuthData(authToken, user.username());
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
        if (SQLUserDataAccess.findUserData(user)) {
            //Service -> DataAccess
            String authToken = SQLAuthDataAccess.createAuthToken();
            SQLAuthDataAccess.addAuthData(authToken, user.username());
            return "\"" + user.username() + "\"\":\"\", \"" + authToken + "\":\"\"";
        }
        throw new UnauthorizedException("Error: unauthorized");
    }

    public static void logoutUser(String authToken) throws UnauthorizedException{
        //Service -> DataAccess
        if (SQLAuthDataAccess.findAuthToken(authToken)) {
            //Service -> DataAccess
            SQLAuthDataAccess.deleteAuthToken(authToken);
            return;
        }
        throw new UnauthorizedException("Error: unauthorized");
    }
}