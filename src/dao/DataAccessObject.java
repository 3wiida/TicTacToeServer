/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
    static ResultSet onlineResult;
    static ResultSet offlineResult;    
    static{
        try {
            DriverManager.registerDriver(new ClientDriver());
            connection = DriverManager.getConnection("jdbc:derby://localhost:1527/TicTacToeDatabase", "root", "root");
            /* عشان الابديت بتاع ال pie chart */
            String onlinePlayerQuery = "SELECT COUNT(*) AS ONLINE_COUNT FROM PLAYERS WHERE STATUS IS NOT NULL AND STATUS = 1 ";
            String offlinePlayersQuery = "SELECT COUNT(*) AS OFFLINE_COUNT FROM PLAYERS WHERE STATUS IS NOT NULL AND STATUS = 0";
            PreparedStatement onlineStat = connection.prepareStatement(onlinePlayerQuery,ResultSet.TYPE_SCROLL_INSENSITIVE , ResultSet.CONCUR_UPDATABLE);
            PreparedStatement offlineStat = connection.prepareStatement(offlinePlayersQuery,ResultSet.TYPE_SCROLL_INSENSITIVE , ResultSet.CONCUR_UPDATABLE);
            onlineResult = onlineStat.executeQuery();
            offlineResult = offlineStat.executeQuery();
        } catch (SQLException ex) {
            System.out.println("error in database");
         //   Logger.getLogger(DataAccessObject.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static int[] getPlayersStatistics(){
        int onlinePlayers = 0, offlinePlayers = 0;
        if(connection != null){
            try {
                if(onlineResult.next()){
                    onlinePlayers = onlineResult.getInt("ONLINE_COUNT");
                }
                if(offlineResult.next()){
                    offlinePlayers = offlineResult.getInt("OFFLINE_COUNT");
                }
            } catch (SQLException ex) {
                System.err.println("Error in access getPlayersStatistics ");
               //Logger.getLogger(DataAccessObject.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return new int[]{onlinePlayers, offlinePlayers};
    }
}
