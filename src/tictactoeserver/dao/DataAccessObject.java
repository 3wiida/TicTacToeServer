/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoeserver.dao;

import tictactoeserver.dto.Player;
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
    
    public static boolean insertPlayer(Player player) throws SQLException{
        PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO PLAYERS (ID,NAME,PASSWORD,STATUS,SCORE) Values (?,?,?,?,?)");
        insertStatement.setString(1,player.getId());
        insertStatement.setString(2,player.getUsername());
        insertStatement.setString(3, player.getPassword());
        insertStatement.setInt(4, 1);
        insertStatement.setInt(5, 0);
        return insertStatement.executeUpdate() > 0;
    }
    
    public static void getOnlineUsers(){
       
    }
    
    public static boolean updateUserStatus(String id, int status) throws SQLException{
        PreparedStatement updateStatement = connection.prepareStatement("UPDATE PLAYERS SET STATUS = ? WHERE ID = ?");
        updateStatement.setInt(1, status);
        updateStatement.setString(2, id);
        return updateStatement.executeUpdate() > 0;
    }
}
