/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package c195fxmlapplication;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author user
 */
public class ReportPrinterController implements Initializable {

    @FXML
    Label selectorLabel;
    @FXML
    Button menuButton;
    @FXML
    TableView reportTable;
    @FXML
    ComboBox reportChoiceBox;
    @FXML 
    Button reportButton;
            
    /**
     * Initializes the controller class.
     */
    public void takeBackToMenu(ActionEvent ae) throws Exception {
        Stage currentStage = (Stage) menuButton.getScene().getWindow();
        currentStage.close();
        Stage menuStage = new Stage();
        Parent menuPane = FXMLLoader.load(getClass().getResource("Menu.fxml"));
        Scene scene = new Scene(menuPane);
        menuStage.setScene(scene);
        menuStage.show();
    }
    
    private void populateReportChoices(){
        String[] choices = {"Number of appointment types by month","Consultant schedule","Number of appointments per customer"};
        reportChoiceBox.getItems().addAll(choices);
    }
    
    public void fetchReport(ActionEvent ae) throws Exception
    {
        String choice = reportChoiceBox.getValue().toString();
        System.out.println(choice);
        String report ="";
        switch(choice)
        {
            case "Number of appointment types by month": report="appointmentTypesByMonth.fxml"; break;
            case "Consultant schedule": report="consultantScheduleReport.fxml"; break;
            case "Number of appointments per customer": report="customerAppointmentReport.fxml"; break;
        }
       Stage reportStage = new Stage();
        Parent reportPane = FXMLLoader.load(getClass().getResource(report));
        Scene scene = new Scene(reportPane);
        reportStage.setScene(scene);
        reportStage.show();
        
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        populateReportChoices();
        // TODO
    }    
    
}
