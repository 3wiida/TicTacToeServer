/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.homeScreen;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import tictactoeserver.PlayerHandler;
import tictactoeserver.TicTacToeServer;

/**
 * FXML Controller class
 *
 * @author Khaled
 */
public class HomeScreenController implements Initializable, Runnable {

    @FXML
    private Label homelbl;
    @FXML
    private Button startbtn;
    @FXML
    private Label serverlbl;

    /**
     * Initializes the controller class.
     */
    public static ServerSocket serverSocket;
    public static Thread thread;
    public static boolean isRunning = false;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void onStartClicked(ActionEvent event) {
         try {
            serverSocket = new ServerSocket(5005);
            System.out.println("connection success!");
            isRunning = true;
            thread = new Thread(this);
            thread.start();
            navigateToStatusScreen(event);
        } catch (IOException ex) {
             System.out.println("error in start button");
           // Logger.getLogger(TicTacToeServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void run() {
            try {
                while(isRunning){
                    Socket clientSocket = serverSocket.accept();
                    new PlayerHandler(clientSocket);
                }   
            } catch (IOException ex) {
                if(isRunning) System.out.println("error in run Home screen ");
               // Logger.getLogger(TicTacToeServer.class.getName()).log(Level.SEVERE, null, ex);
            }finally{
                stopServer();
            }
    }
    
    private void navigateToStatusScreen(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/statusScreen/StatusScreen.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            System.out.println("error in navigate to status func");
           // Logger.getLogger(HomeScreenController.class.getName()).log(Level.SEVERE, "Failed to navigate to status screen", ex);
        }
    }
    
    public static void stopServer(){
        isRunning = false;
        if(serverSocket != null){
            try {
                serverSocket.close();
                System.out.println("stop server");
            } catch (IOException ex) {
                System.out.println("error in stop server func");
               // Logger.getLogger(HomeScreenController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if(thread != null){
            thread.stop();
        }
        
    }

    
}
