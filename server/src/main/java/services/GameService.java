package services;

import dataaccess.AlreadyTakenException;
import dataaccess.BadRequestException;
import dataaccess.UnauthorizedException;
import dataaccess.authdataaccess.AuthDataAccess;
import dataaccess.gamedataaccess.GameDataAccess;
import model.GameData;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import java.util.List;

public class GameService {

    public static GameData createGame(String gameName, String authToken) throws UnauthorizedException, BadRequestException {
        //Service -> DataAccess
        //Check for bad request
        if (GameDataAccess.findGameByName(gameName) || gameName == null) {
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

    public static List<GameData> listGames(String authToken) throws UnauthorizedException {
        //Service -> DataAccess
        //If authToken is in database
        System.out.println("listing games");
        if (AuthDataAccess.findAuthToken(authToken)) {
            return GameDataAccess.findAllGames();
        }
        throw new UnauthorizedException("Error: unauthorized");
    }

    public static void joinGame(int gameID, String playerColor, String username, String authToken) throws UnauthorizedException, BadRequestException, AlreadyTakenException {
        //Service -> DataAccess
        //Check for bad request
        if (!GameDataAccess.findGameByID(gameID)) {
            throw new BadRequestException("Error: bad request");
        } else {
            GameData gameData = GameDataAccess.getGameData(gameID);
            if (gameData != null && Objects.equals(playerColor, "WHITE")
                && gameData.whiteUsername() != null) {
                throw new AlreadyTakenException("Error: already taken");
            } else if (gameData != null && Objects.equals(playerColor, "BLACK")
                    && gameData.blackUsername() != null) {
                throw new AlreadyTakenException("Error: already taken");
            } else if (!Objects.equals(playerColor, "WHITE") &&
                    !Objects.equals(playerColor, "BLACK")){
                throw new BadRequestException("Error: bad request");
            }
        }
        //If authToken is in database
        System.out.println("authorizing join game");
        if (AuthDataAccess.findAuthToken(authToken)) {
            //Service -> DataAccess
            System.out.println("authorized");
            GameData gameData = GameDataAccess.getGameData(gameID);
            GameDataAccess.deleteGameByID(gameID);
            if (Objects.equals(playerColor, "WHITE")) {
                GameDataAccess.addGameData(gameID, username,
                        gameData.blackUsername(), gameData.gameName(), gameData.game());
            } else {
                GameDataAccess.addGameData(gameID, gameData.whiteUsername(),
                        username, gameData.gameName(), gameData.game());
            }
            return;
        }
        throw new UnauthorizedException("Error: unauthorized");
    }
}