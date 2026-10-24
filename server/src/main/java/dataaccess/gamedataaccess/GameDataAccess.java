package dataaccess.gamedataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;
import java.util.List;

public class GameDataAccess {
    static List<GameData> allGameData = new ArrayList<>();

    public static void addGameData(int gameID, String whiteUsername, String blackUsername,
                            String gameName, ChessGame game) {
        allGameData.add(new GameData(gameID, whiteUsername, blackUsername, gameName, game));
    }

    public static boolean findGameByName(String gameName) {
        for (GameData registeredGameData: allGameData) {
            if (registeredGameData.gameName().equals(gameName)) {
                //DataAccess <- database
                return true;
            }
        }
        return false;
    }

    public static boolean findGameByID(int gameID) {
        for (GameData registeredGameData: allGameData) {
            if (registeredGameData.gameID() == gameID) {
                //DataAccess <- database
                return true;
            }
        }
        return false;
    }

    public static List<GameData> findAllGames() {
        return allGameData;
    }

    public static GameData getGameData(int gameID) {
        for (GameData registeredGameData: allGameData) {
            if (registeredGameData.gameID() == gameID) {
                //DataAccess <- database
                return registeredGameData;
            }
        }
        return null;
    }

    public static void deleteGameByID(int gameID) {
        //DataAccess <- database
        allGameData.removeIf(registeredGameData -> registeredGameData.gameID() == gameID);
    }

    public static void deleteGameData() {
        allGameData = new ArrayList<>();
    }
}