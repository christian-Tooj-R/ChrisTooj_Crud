/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package connect;

/**
 *
 * @author Christian
 */

import java.sql.*;


public class ConnectToDatabase {
    private String username;
    private String password;
    private String database;

    public ConnectToDatabase(String username, String password, String database) {
        this.username = username;
        this.password = password;
        this.database = database;
    }
  public Connection getConnectionPostgres() throws Exception {
    Class.forName("org.postgresql.Driver");
    String connect = "jdbc:postgresql://localhost:5432/" + database + "?user=" + username + "&password=" + password;
    Connection con = DriverManager.getConnection(connect);
    return con;
  }
  
  public Connection getConnectionMySql()throws Exception{
    Class.forName("com.mysql.cj.jdbc.Driver");
    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + database + "?user=" + username + "&password=" + password);
    return con;
  }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }
  

}
