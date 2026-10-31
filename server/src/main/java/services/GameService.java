package services;

import dataaccess.AlreadyTakenException;
import dataaccess.BadRequestException;
import dataaccess.UnauthorizedException;
import dataaccess.sql.SQLAuthDataAccess;
import dataaccess.sql.SQLGameDataAccess;
import model.GameData;

import java.util.Objects;
import java.util.Random;
import java.util.List;

public class GameService {

    public static GameData createGame(String gameName, String authToken) throws UnauthorizedException, BadRequestException {
        //Service -> DataAccess
        //Check for bad request
        if (SQLGameDataAccess.findGameByName(gameName) || gameName == null) {
            throw new BadRequestException("Error: bad request");
        }
        //If authToken is in database
        if (SQLAuthDataAccess.findAuthToken(authToken)) {
            //Service -> DataAccess
            Random rand = new Random();
            int gameID = rand.nextInt(9000) + 1000;
            GameData currentGame = new GameData(gameID, null, null, gameName, null);
            SQLGameDataAccess.addGameData(gameID, null, null, gameName, null);
            return currentGame;
        }
        throw new UnauthorizedException("Error: unauthorized");
    }

    public static List<GameData> listGames(String authToken) throws UnauthorizedException {
        //Service -> DataAccess
        //If authToken is in database
        if (SQLAuthDataAccess.findAuthToken(authToken)) {
            return SQLGameDataAccess.findAllGames();
        }
        throw new UnauthorizedException("Error: unauthorized");
    }

    public static void joinGame(int gameID, String playerColor, String username, String authToken)
            throws UnauthorizedException, BadRequestException, AlreadyTakenException {
        //Service -> DataAccess
        //Check for bad request
        if (!SQLGameDataAccess.findGameByID(gameID)) {
            throw new BadRequestException("Error: bad request");
        } else {
            GameData gameData = SQLGameDataAccess.getGameData(gameID);
            if (gameData != null && Objects.equals(playerColor, "WHITE")
                && !gameData.whiteUsername().isEmpty()) {
                throw new AlreadyTakenException("Error: already taken");
            } else if (gameData != null && Objects.equals(playerColor, "BLACK")
                    && !gameData.blackUsername().isEmpty()) {
                throw new AlreadyTakenException("Error: already taken");
            } else if (!Objects.equals(playerColor, "WHITE") &&
                    !Objects.equals(playerColor, "BLACK")){
                throw new BadRequestException("Error: bad request");
            }
        }
        //If authToken is in database
        if (SQLAuthDataAccess.findAuthToken(authToken)) {
            //Service -> DataAccess
            GameData gameData = SQLGameDataAccess.getGameData(gameID);
            SQLGameDataAccess.deleteGameByID(gameID);
            if (Objects.equals(playerColor, "WHITE")) {
                SQLGameDataAccess.addGameData(gameID, username,
                        gameData.blackUsername(), gameData.gameName(), gameData.game());
            } else {
                SQLGameDataAccess.addGameData(gameID, gameData.whiteUsername(),
                        username, gameData.gameName(), gameData.game());
            }
            return;
        }
        throw new UnauthorizedException("Error: unauthorized");
    }
}