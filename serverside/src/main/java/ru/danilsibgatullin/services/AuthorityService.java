package ru.danilsibgatullin.services;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AuthorityService{

    //TODO шифрование пароля
    public String authorityService(String login, String password){
        Statement statement = null;
        String userName =null;
        try{
            statement = ConnectDB.getConnectDB().createStatement();
            ResultSet set = statement.executeQuery("SELECT Username FROM User where Username ='"+login+"' and Password ='"+password+"'");
            while(set.next()){
                userName= set.getString("Username");
            }
        }
        catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
        finally {
            if (statement != null){
                try{
                    statement.close();
                }
                catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }
        return userName;
    }
}