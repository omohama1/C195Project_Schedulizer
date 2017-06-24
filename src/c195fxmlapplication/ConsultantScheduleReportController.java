/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package c195fxmlapplication;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author user
 */
public class ConsultantScheduleReportController implements Initializable {

    @FXML
    ComboBox consultantBox;
    @FXML
    Button confirmButton;
    @FXML
    TableView scheduleTable;

    @FXML
    TableColumn titleColumn;
    @FXML
    TableColumn customerColumn;
    @FXML
    TableColumn startColumn;
    @FXML
    TableColumn endColumn;
    @FXML
    TableColumn descColumn;
    @FXML
    TableColumn locColumn;

    Connection conn;

    /**
     * Initializes the controller class.
     */
    private void populateConsultantBox() throws ClassNotFoundException {
        String query = "SELECT userName FROM user";
        SQLConnector connector = new SQLConnector();
        connector.connectToDB();
        conn = connector.getConnection();
        try {
            Statement stmt = conn.createStatement();
            System.out.println("Connected");
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                consultantBox.getItems().add(rs.getString("userName"));

            }
        } catch (SQLException sqle) {
            sqle.getStackTrace();
        }
    }

    public void showConsultantReport(ActionEvent ae) throws Exception {
        //String endWeek = toWeekBox.getSelectionModel().getSelectedItem().toString();
        //String startWeek = fromWeekBox.getSelectionModel().getSelectedItem().toString();
        //LocalDate startDate = LocalDate.parse(startWeek);
        //LocalDate endDate = LocalDate.parse(endWeek);
        SQLConnector connector = new SQLConnector();

        String userName = consultantBox.getSelectionModel().getSelectedItem().toString();
        System.out.println(userName);
        ArrayList<String> appointments = new ArrayList<String>();
        final ObservableList<ReportAppointment> appts = FXCollections.observableArrayList();
        try {
            connector.connectToDB();
            conn = connector.getConnection();
            String weekQueryStr
                    = "SELECT customerName, title,description, location,start,end, c.createdBy "
                    + "FROM appointment a JOIN customer c ON c.customerID = a.customerId \n "
                    + "WHERE a.createdBy = '" + userName
                    + "' ORDER BY start";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(weekQueryStr);
            while (rs.next()) {
                Timestamp startTS = rs.getTimestamp("start");
                Timestamp endTS = rs.getTimestamp("end");
                String title = rs.getString("title");
                String customer = rs.getString("customerName");
                String location = rs.getString("location");
                String description = rs.getString("description");
                // String type = rs.getString("type");
                String consultant = rs.getString("createdBy");
                //ReportAppointment(String titleStr, String customerStr, String descriptionStr, String locationStr, Timestamp start, Timestamp end, String consultantStr, String typeStr)
                ReportAppointment appointment = new ReportAppointment(title, customer, description, location, startTS, endTS, consultant);
                appts.add(appointment);
                System.out.println(appointment.printAppt());
            }

            scheduleTable.setItems(appts);

            titleColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("title"));
            startColumn.setCellValueFactory(new PropertyValueFactory<Appointment, Timestamp>("startTime"));
            endColumn.setCellValueFactory(new PropertyValueFactory<Appointment, Timestamp>("endTime"));
            locColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("location"));
            customerColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("Customer"));
            descColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("description"));

            scheduleTable.getColumns().setAll(customerColumn, titleColumn, startColumn, endColumn, descColumn, locColumn);

        } catch (SQLException sqle) {
            System.out.println("SQLException encountered at print appointmentByWeek method");
            System.out.print(sqle.fillInStackTrace());
        } catch (ClassNotFoundException e) {
            System.out.println("ClassNotFoundException encountered at printAppointmentsByWeek method");
        } finally {
            conn.close();
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        try {
            populateConsultantBox();
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        }

    }

}
