package ru.danilsibgatullin.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public final class ConnectDB {

    static final String DRIVER = "com.mysql.jdbc.Driver";
    static final String DB = "jdbc:mysql://localhost:3306/CloudStorageBase";
    static final String USER = "m1xer";
    static final String PASSWORD = "1qaz!QAZ";

    private static ConnectDB connectDB;
    private static Connection conn = null;


    public static Connection getConnectDB () throws SQLException, ClassNotFoundException {
        if(conn == null){
            conn = createConnect();
        }
        return conn;
    }

    private static Connection createConnect() throws ClassNotFoundException, SQLException {
        Class.forName(DRIVER);
        conn = DriverManager.getConnection(DB,USER,PASSWORD);
        return conn;
    }

}