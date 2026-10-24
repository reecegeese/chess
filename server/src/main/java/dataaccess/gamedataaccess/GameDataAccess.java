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

    public static boolean findGame(String gameName) {
        for (GameData registeredGameData: allGameData) {
            if (registeredGameData.gameName().equals(gameName)) {
                //DataAccess <- database
                return true;
            }
        }
        return false;
    }

    public static void deleteGameData() {
        allGameData = new ArrayList<>();
    }
}