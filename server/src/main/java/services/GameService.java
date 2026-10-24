package services;

import dataaccess.BadRequestException;
import dataaccess.UnauthorizedException;
import dataaccess.authdataaccess.AuthDataAccess;
import dataaccess.gamedataaccess.GameDataAccess;
import model.GameData;

import java.util.Random;

public class GameService {

    public static GameData createGame(String gameName, String authToken) throws UnauthorizedException, BadRequestException {
        //Service -> DataAccess
        //Check for bad request
        if (GameDataAccess.findGame(gameName)) {
            throw new BadRequestException("Error: bad request");
        }
        //If authToken is in database
        if (AuthDataAccess.findAuthToken(authToken)) {
            //Service -> DataAccess
            Random rand = new Random();
            int gameID = rand.nextInt(9000) + 1000;
            GameData currentGame = new GameData(gameID, null, null, gameName, null);
            GameDataAccess.addGameData(gameID, null, null, gameName, null);
            return currentGame;
        }
        throw new UnauthorizedException("Error: unauthorized");
    }
}