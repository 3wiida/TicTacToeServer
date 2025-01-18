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
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import view.homeScreen.HomeScreenController;
import view.statusScreen.StatusScreenController;

/**
 *
 * @author 3wiida
 */
public class TicTacToeServer extends Application{
    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/homeScreen/HomeScreen.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setOnCloseRequest((e)->{
                try {
                    HomeScreenController.serverSocket.close();
                    HomeScreenController.thread.stop();
                } catch (IOException ex) {
                    System.out.println("error on set on close request");
                }
            });
            primaryStage.show();
        } catch (IOException ex) {
            System.out.println("error in main file");
        }
        

    }

   
    
    public static void main(String[] args) {
        launch(args);
    }
    
}
