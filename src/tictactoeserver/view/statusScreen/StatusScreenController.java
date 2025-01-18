/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoeserver.view.statusScreen;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

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
    private Label statuslbl;
    @FXML
    private Label numlbl;
    @FXML
    private PieChart pieChart;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void onStopClicked(ActionEvent event) {
    }
    
}
