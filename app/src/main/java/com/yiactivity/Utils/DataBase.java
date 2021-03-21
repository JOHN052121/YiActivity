package com.yiactivity.Utils;

import java.sql.Connection;
import java.sql.DriverManager;

public class DataBase {

    private static String user = "sa";
    private static String password = "wyh052121";
    private static String url="jdbc:jtds:sqlserver://192.168.1.109:1433;DatabaseName=YiActivity;useUnicode=true;characterEncoding=UTF-8";
    public static Connection getSQLConnection(){

        Connection connection = null;
        try {
        Class.forName("net.sourceforge.jtds.jdbc.Driver");

            connection = DriverManager.getConnection(url, user, password);
        }catch (Exception e){
            e.printStackTrace();
        }
        return connection;
    }

}
