package com.example.groepsproject;

import java.sql.*;

public class MySQLConnect {

    static Connection staticConnection = null;

    //PreparedStatement aanmaken voor INSERT (zorgt voor de connectie met de Database)
    public static PreparedStatement insertQuery(String string) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://pdbmbook.com:3306/db2022_19","db2022_19","6359379a6bb16"
            );
            if(connection==null) {
                System.out.println("Failed to connect to the database");
            }
            staticConnection = connection;
            statement = connection.prepareStatement(string);
        }

        catch(Exception e){
            System.out.println("Failed to connect to the database");
        }
        return statement;
    }

    //Statement aanmaken voor SELECT (zorgt voor de connectie met de Database)
    public static Statement selectQuery() throws SQLException {
        Connection connection = null;
        Statement statement = null;
        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://pdbmbook.com:3306/db2022_19", "db2022_19", "6359379a6bb16"
            );
            if (connection == null) {
                System.out.println("Failed to connect to the database");
            }
            staticConnection = connection;
            statement = connection.createStatement();
        }
        catch(Exception e){
            System.out.println("Failed to connect to the database");
        }
        return statement;
    }

    //PreparedStatement aanmaken voor UPDATE (zorgt voor de connectie met de Database)
    public static PreparedStatement updateQuery(String string) throws SQLException {

        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://pdbmbook.com:3306/db2022_19","db2022_19","6359379a6bb16"
            );
            if(connection==null) {
                System.out.println("Failed to connect to the database");
            }
            staticConnection = connection;
            statement = connection.prepareStatement(string);
        }

        catch(Exception e){
            System.out.println("Failed to connect to the database");
        }
        return statement;
    }

    //PreparedStatement aanmaken voor DELETE (zorgt voor de connectie met de Database)
    public static PreparedStatement deleteQuery(String string) throws SQLException {

        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://pdbmbook.com:3306/db2022_19","db2022_19","6359379a6bb16"
            );
            if(connection==null) {
                System.out.println("Failed to connect to the database");
            }
            staticConnection = connection;
            statement = connection.prepareStatement(string);
        }

        catch(Exception e){
            System.out.println("Failed to connect to the database");
        }
        return statement;
    }

}
