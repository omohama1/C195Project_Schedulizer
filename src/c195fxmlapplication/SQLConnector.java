/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package c195fxmlapplication;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author user
 */
public class SQLConnector {

    Connection conn;
    String driver;
    String db;
    String url;
    String user;
    String pass;

    public SQLConnector() {
        conn = null;
        driver = "com.mysql.jdbc.Driver";
        db = "U03UVa";
        url = "jdbc:mysql://52.206.157.109/" + db;
        user = "U03UVa";
        pass = "53688085771";
    }

    protected boolean connectToDB() throws ClassNotFoundException {
        System.out.println("Connecting stage 1");
        boolean success = false;

        try 
        {
            System.out.println("Connecting stage 2");
            Class.forName(driver);
            //System.out.println("Connecting");
            conn = DriverManager.getConnection(url, user, pass);
            System.out.println("Connected to database : " + db);
            success = true;
        } catch (SQLException e) 
        {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
        System.out.println(success);
        return success;

    }

    protected boolean verifyCredentials(String usernameSub, String passwordSub) throws ClassNotFoundException {
        boolean verified = false;
        Map<String, String> nameToPasswordMap = new HashMap<>();
        connectToDB();
        try {
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery("select username,password from user where username = '" + usernameSub + "'"
                    + "and password ='" + passwordSub + "'");
            while (result.next()) {
                String username = new String(result.getString("username"));
                String password = new String(result.getString("password"));
                nameToPasswordMap.put(username, password);
                if (!username.equals(null)&& !password.equals(null)) {
                    System.out.println(username + " " + password);
                    verified = true;
                }
            

            }
        } catch (SQLException e) {
            System.out.println("Caught SQLException");
        }
        finally {
            try{
        conn.close();
            }
            catch(SQLException sqle){
                System.out.println("Caught SQLException");
            }
        }
        return verified;
    }
    public Connection getConnection(){
        return conn;
        
    }

}
