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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.derby.jdbc.ClientDriver;

/**
 *
 * @author 3wiida
 */
public class DataAccessObject {
    private static Connection connection;
//    static ResultSet onlineResult;
//    static ResultSet offlineResult;
    static{
        try {
            DriverManager.registerDriver(new ClientDriver());
            connection = DriverManager.getConnection("jdbc:derby://localhost:1527/TicTacToeDatabase", "root", "root");
             /* عشان الابديت بتاع ال pie chart */
//            String onlinePlayerQuery = "SELECT COUNT(*) AS ONLINE_COUNT FROM PLAYERS WHERE STATUS IS NOT NULL AND STATUS = 1 ";
//            String offlinePlayersQuery = "SELECT COUNT(*) AS OFFLINE_COUNT FROM PLAYERS WHERE STATUS IS NOT NULL AND STATUS = 0";
//            PreparedStatement onlineStat = connection.prepareStatement(onlinePlayerQuery,ResultSet.TYPE_SCROLL_INSENSITIVE , ResultSet.CONCUR_UPDATABLE);
//            PreparedStatement offlineStat = connection.prepareStatement(offlinePlayersQuery,ResultSet.TYPE_SCROLL_INSENSITIVE , ResultSet.CONCUR_UPDATABLE);
//            onlineResult = onlineStat.executeQuery();
//            offlineResult = offlineStat.executeQuery();
      
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
        insertStatement.setInt(5, 500);
        return insertStatement.executeUpdate() > 0;
    }
    
    public static ArrayList<Player> getOnlineUsers() throws SQLException {
        ArrayList<Player> onlineUsers = new ArrayList<>();
        String query = "SELECT * FROM PLAYERS WHERE STATUS = 1";

        try (PreparedStatement stmnt = connection.prepareStatement(query)) {
            ResultSet rs = stmnt.executeQuery();

            while (rs.next()) {
                
                String id = rs.getString("ID");
                String name = rs.getString("NAME");
                String password = rs.getString("PASSWORD");
                int status = rs.getInt("STATUS");
                int score = rs.getInt("SCORE");
                onlineUsers.add(new Player(id,name, password, status, score));
            }
        }

        return onlineUsers;
    }
    
    public static boolean updateUserStatusById(String id, int status) throws SQLException{
        PreparedStatement updateStatement = connection.prepareStatement("UPDATE PLAYERS SET STATUS = ? WHERE ID = ?");
        updateStatement.setInt(1, status);
        updateStatement.setString(2, id);
        return updateStatement.executeUpdate() > 0;
    }
    
    public static boolean updateUserStatusByUsername(String username, int status) throws SQLException{
        PreparedStatement updateStatement = connection.prepareStatement("UPDATE PLAYERS SET STATUS = ? WHERE NAME = ?");
        updateStatement.setInt(1, status);
        updateStatement.setString(2, username);
        return updateStatement.executeUpdate() > 0;
    }
    
    public static boolean updatePlayerScore(String username, int amount) throws SQLException{
        PreparedStatement updateStatement = connection.prepareStatement("UPDATE PLAYERS SET SCORE = SCORE + ? WHERE NAME = ?");
        updateStatement.setInt(1, amount);
        updateStatement.setString(2, username);
        return updateStatement.executeUpdate() > 0;
    }
  
  
    
    public static int[] getPlayersStatistics() {
        int onlinePlayers = 0, offlinePlayers = 0;
        if (connection != null) {
            try {
                String onlineQuery = "SELECT COUNT(*) AS ONLINE_COUNT FROM PLAYERS WHERE STATUS = 1 OR STATUS = 2";
                String offlineQuery = "SELECT COUNT(*) AS OFFLINE_COUNT FROM PLAYERS WHERE STATUS = 0";

                try (PreparedStatement onlineStat = connection.prepareStatement(onlineQuery);
                     PreparedStatement offlineStat = connection.prepareStatement(offlineQuery)) {
                    ResultSet onlineResult = onlineStat.executeQuery();
                    ResultSet offlineResult = offlineStat.executeQuery();

                    if (onlineResult.next()) {
                        onlinePlayers = onlineResult.getInt("ONLINE_COUNT");
                    }
                    if (offlineResult.next()) {
                        offlinePlayers = offlineResult.getInt("OFFLINE_COUNT");
                    }
                }
            } catch (SQLException ex) {
                System.err.println("Error in getPlayersStatistics");
            }
        }
        return new int[]{onlinePlayers, offlinePlayers};
    }
    
    public static void updateScoreByUserNameAndID(String name, String id) {
        if (connection != null) {
            try {
                String SQL = "UPDATE PLAYERS SET SCORE = SCORE + 10 WHERE NAME = ? AND ID = ?";
                PreparedStatement updateStatement = connection.prepareStatement(SQL);
                updateStatement.setString(1, name);
                updateStatement.setString(2, id);
                int result = updateStatement.executeUpdate();

                if (result > 0) {
                    System.out.println("Score updated successfully.");
                } else {
                    System.out.println("user not found.");
                }
            } catch (SQLException e) {
                System.out.println("Cannot update score: ");
            }
        } else {
            System.out.println("error in database Database connection in update score function.");
        }
    }

    public static Player authenticateUser(String username, String password) throws SQLException {
        String query = "SELECT * FROM PLAYERS WHERE NAME = ? AND PASSWORD = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String id = rs.getString("ID");
                String name = rs.getString("NAME");
                String userPassword = rs.getString("PASSWORD");
                int status = rs.getInt("STATUS");
                int score = rs.getInt("SCORE");
                return new Player(id, name, userPassword, status, score);
            }
        }
        return null;
    }
    
    public static void decreaseScoreByNameAndId(String name, String id){
        try {
            String sql = "UPDATE PLAYERS SET SCORE = SCORE - 10 WHERE NAME = ? AND ID = ?";
            PreparedStatement statement = connection.prepareCall(sql);
            statement.setString(1, name);
            statement.setString(2, id);
            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DataAccessObject.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
   
}
