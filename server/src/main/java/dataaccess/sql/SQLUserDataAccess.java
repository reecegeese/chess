package dataaccess.sql;

import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

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
                addStatement.setString(2, BCrypt.hashpw(user.password(), BCrypt.gensalt()));
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
            String findUser = "SELECT password FROM userData WHERE username = ?";
            try (var findStatement = conn.prepareStatement(findUser)) {
                findStatement.setString(1, user.username());
                try (var returnStatement = findStatement.executeQuery()) {
                    if (!returnStatement.next()) {
                        return false;
                    }
                    String hashedPassword = returnStatement.getString("password");
                    return BCrypt.checkpw(user.password(), hashedPassword);
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