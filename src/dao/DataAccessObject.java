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

    static {
        try {
            DriverManager.registerDriver(new ClientDriver());
            connection = DriverManager.getConnection("jdbc:derby://localhost:1527/TicTacToeDatabase", "root", "root");

        } catch (SQLException ex) {
            Logger.getLogger(DataAccessObject.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static boolean insertPlayer(Player player) throws SQLException {
        PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO PLAYERS (NAME,PASSWORD,STATUS,SCORE) Values (?,?,?,?)");
        insertStatement.setString(1, player.getUsername());
        insertStatement.setString(2, player.getPassword());
        insertStatement.setInt(3, player.getStatus());
        insertStatement.setInt(4, player.getScore());
        return insertStatement.executeUpdate() > 0;
    }

    public static ArrayList<Player> getOnlineUsers() throws SQLException {
        ArrayList<Player> onlineUsers = new ArrayList<>();
        String query = "SELECT * FROM PLAYERS WHERE STATUS = 1";

        try (PreparedStatement stmnt = connection.prepareStatement(query)) {
            ResultSet rs = stmnt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("ID");
                String name = rs.getString("NAME");
                String password = rs.getString("PASSWORD");
                int status = rs.getInt("STATUS");
                int score = rs.getInt("SCORE");
                onlineUsers.add(new Player(name, password, status, score));
            }
        }

        return onlineUsers;
    }
}
