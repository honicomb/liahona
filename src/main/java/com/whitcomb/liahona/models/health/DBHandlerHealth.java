package com.whitcomb.liahona.models.health;

import com.whitcomb.liahona.models.configs.HealthDB;
import java.sql.Connection;
import java.sql.DriverManager;

public class DBHandlerHealth extends HealthDB {

    Connection dbConnection;

    public Connection getConnection() {
        String connectionString = "jdbc:mysql://"
                + dbHost + ":"
                + dbPort + "/"
                + dbName;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            dbConnection = DriverManager.getConnection(connectionString, dbUser, dbPass);
            return dbConnection;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
