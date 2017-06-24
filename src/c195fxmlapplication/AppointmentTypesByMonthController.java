/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package c195fxmlapplication;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

/**
 * FXML Controller class
 *
 * @author user
 */
public class AppointmentTypesByMonthController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    ComboBox monthSelectorBox;
    @FXML
    Button fetchDataButton;
    @FXML
    TextArea numberArea;
    Connection conn;

    //populateMonthBox() fills in the choices for the monthSelectorBox ComboBox
    private void populateMonthBox() {
        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        for (int i = 0; i < months.length; i++) {
            monthSelectorBox.getItems().add(months[i]);
        }

    }
    /*showAppointmentNumber() shows the appointment types by month in the TextArea numberArea
    by talking to the MySQL database and fetching data from the "appointments" table.  
    This is displayed in numberArea as text when the fetchDataButton is clicked
    */
    public void showAppointmentNumber(ActionEvent ae) throws Exception {
        String apptDesc = "";
        String accumulate = "";
        ArrayList<String> apptTypes = new ArrayList<String>();

        String monthStr = monthSelectorBox.getSelectionModel().getSelectedItem().toString();
        SQLConnector connector = new SQLConnector();
        connector.connectToDB();
        conn = connector.getConnection();
        String monthName = "";
        ArrayList<String> months = new ArrayList<String>();
        int apptCount = 0;
        try {
            connector.connectToDB();
            conn = connector.getConnection();
            String weekQueryStr
                    = "SELECT count(*) AS apptCount,description, MONTHNAME(start) as monthName FROM appointment "
                    + "WHERE MONTHNAME(start)= '"+
                     monthStr+ "' GROUP BY description, MONTHNAME(start)";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(weekQueryStr);
            while (rs.next()) {
                apptCount = rs.getInt("apptCount");
                apptDesc = rs.getString("description");
                monthName = rs.getString("monthName");
                months.add(monthName);
                System.out.println("Appointment type: " + apptDesc  +"Appointments: "+ apptCount );
                if (monthName.equals(monthStr)) {
                    apptTypes.add("Appointment type: " + apptDesc + " : " + "Count: " + apptCount);
                  //  System.out.println("Should appear: " + apptDesc + " : " + apptCount);
                   // System.out.println(monthStr);
                   // System.out.println(apptTypes.indexOf(apptDesc + " : " + apptCount));

                } else {

                }

            }

            for (int i = 0; i < apptTypes.size(); i++) {
                if (months.get(i).contains(monthStr)) {

                    accumulate += apptTypes.get(i) + '\n';
                } else {
                }
            }
          //  System.out.println("Appears: " + accumulate);
            numberArea.setText(accumulate);
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        populateMonthBox();
        // TODO
    }

}
