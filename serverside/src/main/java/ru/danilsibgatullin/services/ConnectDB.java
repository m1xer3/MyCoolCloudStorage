package ru.danilsibgatullin.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*
Создание подключения к базе данных
 */
public final class ConnectDB {

    static final String DRIVER = "com.mysql.jdbc.Driver";
    static final String DB = "jdbc:mysql://localhost:3306/CloudStorageBase";
    static final String USER = "m1xer";
    static final String PASSWORD = "1qaz!QAZ";
    private Connection conn = null;


    public  Connection getConnectDB () throws SQLException, ClassNotFoundException {
        if(conn == null){
            conn = createConnect();
        }
        return conn;
    }

    private  Connection createConnect() throws ClassNotFoundException, SQLException {
        Class.forName(DRIVER);
        conn = DriverManager.getConnection(DB,USER,PASSWORD);
        return conn;
    }

}