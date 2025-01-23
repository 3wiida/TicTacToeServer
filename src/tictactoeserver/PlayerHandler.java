/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoeserver;

import tictactoeserver.dao.DataAccessObject;
import tictactoeserver.dto.Player;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author 3wiida
 */
public class PlayerHandler extends Thread {

    private String id;
    private String username;
    private Socket clientSocket;
    private DataInputStream dis;
    private PrintStream ps;

    private static Vector<PlayerHandler> players = new Vector<>();
    
    private static HashMap<String,PlayerHandler> playerMapping = new HashMap<>();
    public PlayerHandler(Socket clientSocket) {
        try {
            this.clientSocket = clientSocket;
            dis = new DataInputStream(clientSocket.getInputStream());
            ps = new PrintStream(clientSocket.getOutputStream());
            players.add(this);
            start();
        } catch (IOException ex) {
            Logger.getLogger(PlayerHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                String msg = dis.readLine();
                System.out.println(msg);
                JSONObject requestJson = new JSONObject(msg);
                String requestType = requestJson.getString("type");

                switch (requestType) {
                    case "login": {
                        break;
                    }

                    case "register": {
                        handleRegisterRequest(requestJson);
                        break;
                    }

                    case "get Avaliable users": {
                        handleGetAvilablePlayersRequest();
                        break;
                    }

                    case "invite": {
                        handleInvitationRequest(requestJson);
                        break;
                    }

                    case "invitationRejected": {
                        handleInvitationRejectedRequest(requestJson);
                        break;
                    }

                    case "invitationAccecpted": {
                        handleInvitationAccecptedRequest(requestJson);
                        break;
                    }

                    case "invitationCanceled": {
                        handleInvitationCanceled(requestJson);
                        break;
                    }

                    case "logout": {
                        handleLogoutRequest();
                        break;
                    }
                    
                    case "move": 
                        handlePlayerMove(requestJson);
                        break;
                        
                    case "withdrawal":{
                        handleWithdrawalRequest(requestJson);
                        break;
                    }    
                    
                    default: {
                        break;
                    }
                }

            } catch (IOException ex) {
                Logger.getLogger(PlayerHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /* Register Operations */
    private void handleRegisterRequest(JSONObject request) {
        try {
            String id = UUID.randomUUID().toString();
            String username = request.getString("username");
            String password = request.getString("password");
            Player player = new Player(id, username, password, 0, 1);
            boolean isInsertionSuccess = DataAccessObject.insertPlayer(player);
            if (isInsertionSuccess) {
                this.id = id;
                this.username = username;
                playerMapping.put(username, this);
            }
            sendRegisterResponse(isInsertionSuccess, username);
        } catch (SQLException ex) {
            Logger.getLogger(PlayerHandler.class.getName()).log(Level.SEVERE, null, ex);
            sendRegisterResponse(false, null);
        }
    }

    private void sendRegisterResponse(boolean isSuccess, String username) {
        JSONObject registerResonse = new JSONObject();
        registerResonse.put("isOk", isSuccess);
        registerResonse.put("username", username);
        registerResonse.put("id", id);
        if (!isSuccess) {
            registerResonse.put("error", "User is already exists");
        }
        ps.println(registerResonse.toString());
    }

    /* Logout Operations */
    private void handleLogoutRequest() {
        try {
            boolean isLogoutSuccess = DataAccessObject.updateUserStatusById(id, 0);
            System.out.println("logout => " + isLogoutSuccess);
            sendLogoutResponse(isLogoutSuccess);
            if(isLogoutSuccess){
                players.remove(this);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PlayerHandler.class.getName()).log(Level.SEVERE, null, ex);
            sendLogoutResponse(false);
        }
    }

    private void sendLogoutResponse(boolean isLogoutSuccess) {
        JSONObject logoutResponse = new JSONObject();
        logoutResponse.put("isOk", isLogoutSuccess);
        ps.println(logoutResponse.toString());
    }
    
    private void handleGetAvilablePlayersRequest(){
        try {
            ArrayList<Player> onlinePlayers = DataAccessObject.getOnlineUsers();
            JSONObject response = new JSONObject();
            JSONArray onlineUsers = new JSONArray();

            for (Player player : onlinePlayers) {
                if (id .equals(player.getId())) continue;
                    JSONObject playerJson = new JSONObject();
                    playerJson.put("username", player.getUsername());
                    playerJson.put("password", player.getPassword());
                    playerJson.put("status", player.getStatus());
                    playerJson.put("score", player.getScore());
                    onlineUsers.put(playerJson);
                }
                response.put("type", "onlinePlayers");
                response.put("onlinePlayers", onlineUsers);
                System.out.println(response.toString());
                ps.println(response.toString());
        } catch (SQLException ex) {
            Logger.getLogger(PlayerHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void handlePlayerMove(JSONObject requestJson){
        System.out.println("enter handle player move");
        String toOpponentUser = requestJson.getString("to");
        String fromUser = requestJson.getString("from");
        
        for (Map.Entry<String, PlayerHandler> me : playerMapping.entrySet()) {
         
            if(me.getKey().equals(toOpponentUser)){
                System.out.println("to => "+ toOpponentUser + "  from => "+ fromUser);
                System.out.println("found the player to send move");
                JSONObject moveJSON = new JSONObject();
                moveJSON.put("type", "move");
                moveJSON.put("turn",requestJson.getString("turn"));
                moveJSON.put("row", requestJson.getInt("row"));
                moveJSON.put("col", requestJson.getInt("col"));
                moveJSON.put("from", requestJson.getString("from"));
                System.out.println("move json => " + moveJSON);
                me.getValue().ps.println(moveJSON.toString());
            }
        }
    }
    
    
    /* Invitation System */
    private void handleInvitationRequest(JSONObject invitation) {
        String opponentUsername = invitation.getString("opponentUsername");
        for (PlayerHandler player : players) {
            System.out.println(player.username);
            if (player.username.equals(opponentUsername)) {
                sendInvitationToOpponent(this, player);
                break;
            }
        }
    }

    private void sendInvitationToOpponent(PlayerHandler host, PlayerHandler opponent) {
        JSONObject invitation = new JSONObject();
        invitation.put("type", "invitationRecieved");
        invitation.put("hostUsername", host.username);
        invitation.put("hostId", host.id);
        invitation.put("hostScore", 350);
        System.out.println("invitation is => " + invitation.toString());
        opponent.ps.println(invitation.toString());
    }

    private void handleInvitationRejectedRequest(JSONObject rejection) {
        String opponent = rejection.getString("from");
        String host = rejection.getString("to");
        for (PlayerHandler player : players) {
            if (player.username.equals(host)) {
                sendRejectionToHost(opponent, player);
                break;
            }
        }
    }

    private void sendRejectionToHost(String opponent, PlayerHandler host) {
        JSONObject rejection = new JSONObject();
        rejection.put("type", "invitationRejected");
        rejection.put("from", opponent);
        host.ps.println(rejection.toString());
    }

    private void handleInvitationAccecptedRequest(JSONObject confirmation) {
        String host = confirmation.getString("to");
        JSONObject opponentData = confirmation.getJSONObject("opponent");
        for (PlayerHandler player : players) {
            if (player.username.equals(host)) {
                sendAcceptanceToHost(opponentData, player);
                break;
            }
        }
        try {
            String opponentUsername = opponentData.getString("opponentUsername");
            DataAccessObject.updateUserStatusByUsername(opponentUsername, 2);
            DataAccessObject.updateUserStatusByUsername(host, 2);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
    }

    private void sendAcceptanceToHost(JSONObject opponentData, PlayerHandler host) {
        JSONObject accecptance = new JSONObject();
        accecptance.put("type", "invitationAccecpted");
        accecptance.put("opponent", opponentData);
        host.ps.println(accecptance.toString());
    }

    private void handleInvitationCanceled(JSONObject cancelation) {
        String opponent = cancelation.getString("to");
        for (PlayerHandler player : players) {
            if (player.username.equals(opponent)) {
                sendCancelToOpponent(player);
                break;
            }
        }
    }

    private void sendCancelToOpponent(PlayerHandler player) {
        JSONObject cancelation = new JSONObject();
        cancelation.put("type", "invitationCanceled");
        player.ps.println(cancelation.toString());
    }
    
    private void handleWithdrawalRequest(JSONObject withdrawal){
        String opponentUsername = withdrawal.getString("to");
        try {
            DataAccessObject.updateUserStatusByUsername(username, 1);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        for(PlayerHandler player : players){
            if(player.username.equals(opponentUsername)){
                sendWithdrawalToOpponent(player);
                try {
                    DataAccessObject.updateUserStatusByUsername(opponentUsername, 1);
                } catch (SQLException ex) {
                   ex.printStackTrace();
                }
            }
        }
    }
    
    private void sendWithdrawalToOpponent(PlayerHandler player){
        JSONObject withdrawal = new JSONObject();
        withdrawal.put("type", "withdrawal");
        player.ps.println(withdrawal.toString());
    }

    public static void broadcastMessage(String message) {
        for (PlayerHandler player : players) {
            player.ps.println(message);
        }
    }

    public static void closeAllConnections() {
        for (PlayerHandler player : players) {
            player.closeConnection();
            System.err.println("u are down");
        }
        players.clear();
    }

    public void closeConnection() {
        try {
            if (ps != null) {
                ps.close();
            }
            if (dis != null) {
                dis.close();
            }
            if (clientSocket != null) {
                clientSocket.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(PlayerHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
