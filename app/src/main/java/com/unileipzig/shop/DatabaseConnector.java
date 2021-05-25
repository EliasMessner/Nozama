package com.unileipzig.shop;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Goal of class: https://stackoverflow.com/questions/471745/how-many-jdbc-connections-in-java
public class DatabaseConnector {

    static Connection conn;

    public static Connection getConnection(){
        return getConnection("test_1");
    }

    public static Connection getConnection(String database){
        if (conn == null) {
            try {
                Class.forName("org.postgresql.Driver");
                conn = DriverManager.getConnection("jdbc:postgresql://db:5432/" + database,
                        "postgres", "example");

                if (conn != null) {
                    System.out.println("Connected to the database!");
                } else {
                    System.out.println("Failed to make connection!");
                }
            } catch (SQLException e) {
                System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return conn;
    }
}
