package model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.DriverManager;

public class MySQL {
    
    private static Connection connection;
    
    public static void createConnection()throws Exception{
        if (connection == null) {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/medipluse","root","#Lucky2003sql");
        }
    }
    
    public static ResultSet executeSearch(String quary) throws Exception{
        createConnection();
        return connection.createStatement().executeQuery(quary);
    }
    
    public static Integer executeIUD(String quary) throws Exception{
        createConnection();
        return connection.createStatement().executeUpdate(quary);
    }
    
}
