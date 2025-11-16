package com.whitcomb.liahona.models.tracking;

import com.whitcomb.liahona.models.configs.TrackingDB;
import java.sql.Connection;
import java.sql.DriverManager;

public class DBHandlerTracking extends TrackingDB {

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
