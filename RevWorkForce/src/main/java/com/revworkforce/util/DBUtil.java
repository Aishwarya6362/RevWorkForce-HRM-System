package com.revworkforce.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*  Central utility class for database connections.
    Keeps JDBC connection logic in one place to avoid duplication.*/
public class DBUtil {

    // Oracle XE connection details
    private static final String URL =
            "jdbc:oracle:thin:@localhost:1521/XEPDB1";
    private static final String USER = "aish";
    private static final String PASSWORD = "aish";

    // Prevent creating objects of this utility class
    private DBUtil() {
    }

    //Provides a new database connection whenever required by DAO classes.

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}