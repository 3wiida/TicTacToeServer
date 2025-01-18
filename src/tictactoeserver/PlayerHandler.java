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
import java.util.UUID;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;

/**
 *
 * @author 3wiida
 */
public class PlayerHandler extends Thread{
    
    private String id;
    private Socket clientSocket;
    private DataInputStream dis;
    private PrintStream ps;
    
    private Vector<PlayerHandler> players = new Vector<>();
    

    public PlayerHandler(Socket clientSocket) {
        try {
            this.clientSocket = clientSocket;
            dis =  new DataInputStream(clientSocket.getInputStream());
            ps = new PrintStream(clientSocket.getOutputStream());
            players.add(this);
            start();
        } catch (IOException ex) {
            Logger.getLogger(PlayerHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    @Override
    public void run(){
        while (true) {            
            try {
                String msg = dis.readLine();
                System.out.println(msg);
                JSONObject requestJson = new JSONObject(msg);
                String requestType = requestJson.getString("type");
                
                switch(requestType){
                    case "login" : {
                        break;
                    }
                    
                    case "register": {
                        handleRegisterRequest(requestJson);
                        break;
                    }
                    
                    case "get Avaliable users":{
                        break;
                    }
                    
                    case "logout": {
                        handleLogoutRequest();
                        break;
                    }
                    default:{
                        break;
                    }
                }
                
                
            } catch (IOException ex) {
                Logger.getLogger(PlayerHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void handleRegisterRequest(JSONObject request){
        try {
            String id = UUID.randomUUID().toString();
            String username = request.getString("username");
            String password = request.getString("password");
            Player player = new Player(id, username, password, 0, 1);
            boolean isInsertionSuccess = DataAccessObject.insertPlayer(player);
            if(isInsertionSuccess) this.id = id;
            sendRegisterResponse(isInsertionSuccess, username);
        } catch (SQLException ex) {
            Logger.getLogger(PlayerHandler.class.getName()).log(Level.SEVERE, null, ex);
            sendRegisterResponse(false, null);
        }
    }
    
    private void sendRegisterResponse(boolean isSuccess, String username){
        JSONObject registerResonse = new JSONObject();
        registerResonse.put("isOk", isSuccess);
        registerResonse.put("username", username);
        if(!isSuccess){
            registerResonse.put("error", "User is already exists");
        }
        ps.println(registerResonse.toString());
    }
    
    private void handleLogoutRequest(){
        try {
            boolean isLogoutSuccess = DataAccessObject.updateUserStatus(id, 0);
            sendLogoutResponse(isLogoutSuccess);
        } catch (SQLException ex) {
            Logger.getLogger(PlayerHandler.class.getName()).log(Level.SEVERE, null, ex);
            sendLogoutResponse(false);
        }
    }
    
    private void sendLogoutResponse(boolean isLogoutSuccess){
        JSONObject logoutResponse = new JSONObject();
        logoutResponse.put("isOk", isLogoutSuccess);
        ps.println(logoutResponse.toString());
    }
    
}
