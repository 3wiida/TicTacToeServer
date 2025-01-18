/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoeserver;

import dao.DataAccessObject;
import dto.Player;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;

/**
 *
 * @author 3wiida
 */
public class PlayerHandler extends Thread{
    
    private int id;
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
                        
                    }
                    
                    case "get Avaliable users":{
                        
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
    
}
