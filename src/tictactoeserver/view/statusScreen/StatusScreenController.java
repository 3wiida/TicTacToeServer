package tictactoeserver.view.statusScreen;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
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
import tictactoeserver.dao.DataAccessObject;
import tictactoeserver.view.homeScreen.HomeScreenController;

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

    private boolean stopUpdating = false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        updatePieChart();
        new Thread(()->{
                try {
                    while (!stopUpdating) {
                        Thread.sleep(3000); 
                        Platform.runLater(() -> this.updatePieChart());
                    }
                } catch (InterruptedException e) {
                    Logger.getLogger(StatusScreenController.class.getName()).log(Level.SEVERE, null, e);
                }
            
        }).start();
    }

    private void updatePieChart() {
        int[] playerStats = DataAccessObject.getPlayersStatistics();
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("ONLINE PLAYERS", playerStats[0]),
                new PieChart.Data("OFFLINE PLAYERS", playerStats[1])
        );

        pieChart.setData(pieChartData);
        onlineNumlbl.setText(String.valueOf(playerStats[0]));
        offlineNumlbl.setText(String.valueOf(playerStats[1]));
    }

    @FXML
    private void onStopClicked(ActionEvent event) {
        stopUpdating = true; 
        HomeScreenController.stopServer();
        navigateToHomeScreen(event);
    }

    private void navigateToHomeScreen(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tictactoeserver/view/homeScreen/HomeScreen.fxml"));
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
