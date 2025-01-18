/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoeserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author 3wiida
 */
public class TicTacToeServer extends Application implements Runnable{
    
    
    private ServerSocket serverSocket;
    private Thread thread;
    
    @Override
    public void start(Stage primaryStage) {
        
        try {
            serverSocket = new ServerSocket(5005);
            thread = new Thread(this);
            thread.start();
        } catch (IOException ex) {
            Logger.getLogger(TicTacToeServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        StackPane root = new StackPane();
        
        Scene scene = new Scene(root, 300, 250);
        
        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
        
    }

   
    
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void run() {
        while(true){
            try {
                Socket clientSocket = serverSocket.accept();
                new PlayerHandler(clientSocket);
            } catch (IOException ex) {
                Logger.getLogger(TicTacToeServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    
}
