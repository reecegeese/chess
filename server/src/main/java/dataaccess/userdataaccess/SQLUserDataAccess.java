package dataaccess.userdataaccess;

import model.UserData;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
        for (UserData registeredUser: allUserData) {
            if (registeredUser.username().equals(user.username()) &&
                    registeredUser.password().equals(user.password())) {
                //DataAccess <- database
                return true;
            }
        }
        //DataAccess <- database
        return false;
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