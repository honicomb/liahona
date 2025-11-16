package com.whitcomb.liahona.models.goals;

import com.whitcomb.liahona.models.configs.TasksDB;
import java.sql.Connection;
import java.sql.DriverManager;

public class DBHandlerGoals extends TasksDB {

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
