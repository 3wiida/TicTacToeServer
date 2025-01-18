/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.statusScreen;

import dao.DataAccessObject;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import view.homeScreen.HomeScreenController;

/**
 * FXML Controller class
 *
 * @author Khaled
 */
public class StatusScreenController implements Initializable {
    @FXML
    private Label headerlbl1;
    @FXML
    private Label headerlbl2;
    @FXML
    private Button stopbtn;
    @FXML
    private PieChart pieChart;
    @FXML
    private Label onlineStatuslbl;
    @FXML
    private Label offlineStatuslbl;
    @FXML
    private Label onlineNumlbl;
    @FXML
    private Label offlineNumlbl;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        int playerStat[] = DataAccessObject.getPlayersStatistics();
        ObservableList<PieChart.Data> pieChartData;
        if(playerStat[0] == 0 && playerStat[1] == 0){
            pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data("ONLINE PLAYERS", 5),//
                        new PieChart.Data("OFFLINE PLAYERS",5)//
                );
        }else{
                 pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data("ONLINE PLAYERS", playerStat[0]),//
                        new PieChart.Data("OFFLINE PLAYERS",playerStat[1])//
                );
        }
        
        pieChart.getData().addAll(pieChartData);
        onlineNumlbl.setText(String.valueOf(playerStat[0]));
        offlineNumlbl.setText(String.valueOf(playerStat[1]));
    }
    @FXML
    private void onStopClicked(ActionEvent event) {
        HomeScreenController.stopServer();
        navigateToHomeScreen(event);
    }
    private void navigateToHomeScreen(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/homeScreen/HomeScreen.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(StatusScreenController.class.getName()).log(Level.SEVERE, "Failed to navigate to home screen", ex);
        }
    }
}
