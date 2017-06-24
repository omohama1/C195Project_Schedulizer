/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package c195fxmlapplication;

import static c195fxmlapplication.Alert.testTime;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import static java.lang.Thread.sleep;
import java.net.URL;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author user
 */
public class MenuController implements Initializable {

    @FXML
    Button logoutButton;
    @FXML
    Button generateRportsButton;
    @FXML
    Button addCustomerButton;
    @FXML
    Button viewAppointmentsButton;
    @FXML
    Button setAppointmentButton;
    @FXML Button updateCustButton;
   // File LOG_FILE = new File("userlog.txt");
   // static String log;
    //FileWriter fw = null;
   // BufferedWriter bw = null;
    Connection conn;
    UserLogger logger = new UserLogger();
    //static int done;
 

    /**
     * Initializes the controller class.
     *
     */
    

    protected static String fetchUser() {
        FXMLDocumentController doc = new FXMLDocumentController();
        String username = doc.getUserName();
        return username;
    }

    public void showCalendarViewer(ActionEvent ae) throws Exception {
        Stage stage = (Stage) viewAppointmentsButton.getScene().getWindow();
        stage.close();
        Stage calendarStage = new Stage();
        Parent schedulePane = FXMLLoader.load(getClass().getResource("CalendarViewer.fxml"));
        Scene scene = new Scene(schedulePane);
        calendarStage.setScene(scene);
        calendarStage.show();
        logger.writeToLog("View schedule button clicked");

    }

    public void showCalendarStage(ActionEvent ae) throws Exception {
        Stage stage = (Stage) setAppointmentButton.getScene().getWindow();
        stage.close();
        Stage calendarStage = new Stage();
        Parent schedulePane = FXMLLoader.load(getClass().getResource("scheduleForm.fxml"));
        Scene scene = new Scene(schedulePane);
        calendarStage.setScene(scene);
        calendarStage.show();
        logger.writeToLog("Set appointment button clicked");
    }

    public void logout(ActionEvent ae) throws Exception {
        Stage stage = (Stage) logoutButton.getScene().getWindow();
        stage.close();
        SQLConnector connector = new SQLConnector();
        connector.connectToDB();
         conn = connector.getConnection();
        conn.close();
        logger.writeToLog("User: " +fetchUser() + " has logged out at " + LocalDateTime.now().toString());
    }

    public void createNewCustomer(ActionEvent ae) throws Exception {

        Stage custStage = new Stage();
        Parent customerPane = FXMLLoader.load(getClass().getResource("NewCustomer.fxml"));
        Stage currentStage = (Stage) addCustomerButton.getScene().getWindow();
        currentStage.close();
        Scene scene = new Scene(customerPane);
        custStage.setScene(scene);
        custStage.show();
        logger.writeToLog("New customer button clicked");

    }

    
       public void updateCustomer(ActionEvent ae) throws Exception {

        Stage custStage = new Stage();
        Parent customerPane = FXMLLoader.load(getClass().getResource("updateCustomer.fxml"));
        Stage currentStage = (Stage) updateCustButton.getScene().getWindow();
        currentStage.close();
        Scene scene = new Scene(customerPane);
        custStage.setScene(scene);
        custStage.show();
        logger.writeToLog("Update customer button clicked");

    }
    
    public void generateReports(ActionEvent ae) throws Exception {
        Stage custStage = new Stage();
        Parent customerPane = FXMLLoader.load(getClass().getResource("ReportPrinter.fxml"));
        Stage currentStage = (Stage) addCustomerButton.getScene().getWindow();
        currentStage.close();
        Scene scene = new Scene(customerPane);
        custStage.setScene(scene);
        custStage.show();
        logger.writeToLog("Generate reports button clicked");

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Alert alert = new Alert();
        alert.triggerAlarm();
        LocalDateTime today = LocalDateTime.now();
        try {
            logger.writeToLog("The user has returned to the menu "  + today.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
      
    }
}