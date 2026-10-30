package dataaccess.gamedataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static dataaccess.DatabaseManager.*;
import static dataaccess.DatabaseManager.databaseName;

public class SQLGameDataAccess {
    public static void addGameData(int gameID, String whiteUsername, String blackUsername,
                                   String gameName, ChessGame game) {
        try (var conn = DriverManager.getConnection(connectionUrl, dbUsername, dbPassword)) {
            conn.setCatalog(databaseName);
            String addData = "INSERT INTO gameData (gameID, whiteUsername, blackUsername, " +
                    "gameName, game) VALUES (?, ?, ?, ?, ?, ?)";
            try (var addStatement = conn.prepareStatement(addData)) {
                addStatement.setInt(1, gameID);
                addStatement.setString(2, whiteUsername);
                addStatement.setString(3, blackUsername);
                addStatement.setString(4, gameName);
                addStatement.setString(5, new Gson().toJson(game));
                addStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean findGameByName(String gameName) {
        //DataAccess -> Database
        try (var conn = DriverManager.getConnection(connectionUrl, dbUsername, dbPassword)) {
            conn.setCatalog(databaseName);
            String findGame = "SELECT EXISTS (SELECT 1 FROM gameData WHERE gameName = ?)";
            try (var findStatement = conn.prepareStatement(findGame)) {
                findStatement.setString(1, gameName);
                try (var returnStatement = findStatement.executeQuery()) {
                    return returnStatement.next();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean findGameByID(int gameID) {
        //DataAccess -> Database
        try (var conn = DriverManager.getConnection(connectionUrl, dbUsername, dbPassword)) {
            conn.setCatalog(databaseName);
            String findGame = "SELECT EXISTS (SELECT 1 FROM gameData WHERE gameID = ?)";
            try (var findStatement = conn.prepareStatement(findGame)) {
                findStatement.setInt(1, gameID);
                try (var returnStatement = findStatement.executeQuery()) {
                    return returnStatement.next();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<GameData> findAllGames() {
        List<GameData> games = new ArrayList<>();
        //DataAccess -> Database
        try (var conn = DriverManager.getConnection(connectionUrl, dbUsername, dbPassword)) {
            conn.setCatalog(databaseName);
            String getData = "SELECT gameID, whiteUsername, blackUsername, gameName, game" +
                    "FROM gameData WHERE gameID = ?)";
            try (var getStatement = conn.prepareStatement(getData);
                  var returnStatement = getStatement.executeQuery()) {
                while (returnStatement.next()) {
                    ChessGame chessGame = new Gson().fromJson(returnStatement.getString("game"), ChessGame.class);
                    games.add(new GameData(
                            returnStatement.getInt("gameID"),
                            returnStatement.getString("whiteUsername"),
                            returnStatement.getString("blackUsername"),
                            returnStatement.getString("gameName"),
                            chessGame));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return games;
    }

    public static GameData getGameData(int gameID) {
        //DataAccess -> Database
        try (var conn = DriverManager.getConnection(connectionUrl, dbUsername, dbPassword)) {
            conn.setCatalog(databaseName);
            String getData = "SELECT gameID, whiteUsername, blackUsername, gameName, game" +
                    "FROM gameData WHERE gameID = ?)";
            try (var getStatement = conn.prepareStatement(getData)) {
                getStatement.setInt(1, gameID);
                try (var returnStatement = getStatement.executeQuery()) {
                    if (returnStatement.next()) {
                        ChessGame chessGame = new Gson().fromJson(returnStatement.getString("game"), ChessGame.class);
                        return new GameData(
                                returnStatement.getInt("gameID"),
                                returnStatement.getString("whiteUsername"),
                                returnStatement.getString("blackUsername"),
                                returnStatement.getString("gameName"),
                                chessGame);
                    }
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteGameByID(int gameID) {
        //DataAccess -> Database
        try (var conn = DriverManager.getConnection(connectionUrl, dbUsername, dbPassword)) {
            conn.setCatalog(databaseName);
            String deleteGame = "DELETE FROM gameData WHERE gameID = ?";
            try (var deleteStatement = conn.prepareStatement(deleteGame)) {
                deleteStatement.setInt(1, gameID);
                deleteStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteGameData() {
        //DataAccess -> Database
        try (var conn = DriverManager.getConnection(connectionUrl, dbUsername, dbPassword)) {
            conn.setCatalog(databaseName);
            String deleteAllData = "TRUNCATE TABLE gameData";
            try (var deleteTableStatement = conn.prepareStatement(deleteAllData)) {
                deleteTableStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}