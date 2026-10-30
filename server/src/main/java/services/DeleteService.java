package services;

import dataaccess.authdataaccess.AuthDataAccess;
import dataaccess.gamedataaccess.GameDataAccess;

import java.sql.DriverManager;
import java.sql.SQLException;

import static dataaccess.DatabaseManager.*;

public class DeleteService {

    public static void deleteService() throws RuntimeException {
        //DataAccess -> Database
        AuthDataAccess.deleteAuthData();
        GameDataAccess.deleteGameData();
        try (var conn = DriverManager.getConnection(connectionUrl, dbUsername, dbPassword)) {
             conn.setCatalog(databaseName);
             String deleteAllData = "TRUNCATE TABLE allUserData";
            try (var deleteTableStatement = conn.prepareStatement(deleteAllData)) {
                deleteTableStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}