package dataaccess.authdataaccess;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.UUID;

import static dataaccess.DatabaseManager.*;
import static dataaccess.DatabaseManager.databaseName;

public class SQLAuthDataAccess {
    public static String createAuthToken() {
        return UUID.randomUUID().toString();
    }

    public static void addAuthData(String authToken, String username) {
        try (var conn = DriverManager.getConnection(connectionUrl, dbUsername, dbPassword)) {
            conn.setCatalog(databaseName);
            String addData = "INSERT INTO authData (authToken, username) VALUES (?, ?)";
            try (var addStatement = conn.prepareStatement(addData)) {
                addStatement.setString(1, authToken);
                addStatement.setString(2, username);
                addStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteAuthToken(String authToken) {
        //DataAccess -> Database
        try (var conn = DriverManager.getConnection(connectionUrl, dbUsername, dbPassword)) {
            conn.setCatalog(databaseName);
            String deleteToken = "UPDATE authData SET authToken = NULL WHERE authToken = ?";
            try (var deleteStatement = conn.prepareStatement(deleteToken)) {
                deleteStatement.setString(1, authToken);
                deleteStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    static public boolean findAuthToken(String authToken) {
        //DataAccess -> Database
        try (var conn = DriverManager.getConnection(connectionUrl, dbUsername, dbPassword)) {
            conn.setCatalog(databaseName);
            String findToken = "SELECT EXISTS (SELECT 1 FROM authData WHERE authToken = ?)";
            try (var findStatement = conn.prepareStatement(findToken)) {
                findStatement.setString(1, authToken);
                try (var returnStatement = findStatement.executeQuery()) {
                    return returnStatement.next();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteAuthData() {
        //DataAccess -> Database
        try (var conn = DriverManager.getConnection(connectionUrl, dbUsername, dbPassword)) {
            conn.setCatalog(databaseName);
            String deleteAllData = "TRUNCATE TABLE authData";
            try (var deleteTableStatement = conn.prepareStatement(deleteAllData)) {
                deleteTableStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}