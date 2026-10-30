package dataaccess.userdataaccess;

import model.UserData;

import java.sql.DriverManager;
import java.sql.SQLException;

import static dataaccess.DatabaseManager.*;
import static dataaccess.DatabaseManager.databaseName;

public class SQLUserDataAccess {
    static public void addUserData(UserData user) {
        //DataAccess -> Database
        try (var conn = DriverManager.getConnection(connectionUrl, dbUsername, dbPassword)) {
            conn.setCatalog(databaseName);
            String addData = "INSERT INTO userData (username, password, email) VALUES (?, ?, ?)";
            try (var addStatement = conn.prepareStatement(addData)) {
                addStatement.setString(1, user.username());
                addStatement.setString(2, user.password());
                addStatement.setString(3, user.email());
                addStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    static public boolean findUserData(UserData user) {
        //DataAccess -> Database
        try (var conn = DriverManager.getConnection(connectionUrl, dbUsername, dbPassword)) {
            conn.setCatalog(databaseName);
            String findUser = "SELECT 1 FROM userData WHERE username = ?" +
                    "AND password = ?;";
            try (var findStatement = conn.prepareStatement(findUser)) {
                findStatement.setString(1, user.username());
                findStatement.setString(2, user.password());
                try (var returnStatement = findStatement.executeQuery()) {
                    return returnStatement.next();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    static public void deleteUserData() {
        //DataAccess -> Database
        try (var conn = DriverManager.getConnection(connectionUrl, dbUsername, dbPassword)) {
            conn.setCatalog(databaseName);
            String deleteAllData = "TRUNCATE TABLE userData";
            try (var deleteTableStatement = conn.prepareStatement(deleteAllData)) {
                deleteTableStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}