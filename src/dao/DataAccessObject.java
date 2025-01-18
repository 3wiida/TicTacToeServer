/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.Player;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.derby.jdbc.ClientDriver;

/**
 *
 * @author 3wiida
 */
public class DataAccessObject {
    private static Connection connection;

    
    static{
        try {
            DriverManager.registerDriver(new ClientDriver());
            connection = DriverManager.getConnection("jdbc:derby://localhost:1527/TicTacToeDatabase", "root", "root");
      
        } catch (SQLException ex) {
            Logger.getLogger(DataAccessObject.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static boolean insertPlayer(Player player){
        PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO PLAYERS (NAME,PASSWORD,STATUS,SCORE) Values (?,?,?,?)");
        insertStatement.setString(1,player.getUsername());
        insertStatement.setString(2, player.getPassword());
        insertStatement.setString(3, player.getStatus());
        insertStatement.setString(4, player.getScore());
        return insertStatement.executeUpdate() > 0;
    }
    
    public static void getOnlineUsers(){
       
    }
}
