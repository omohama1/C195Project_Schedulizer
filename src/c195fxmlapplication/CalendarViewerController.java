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
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author user
 */
public class CalendarViewerController implements Initializable {

    @FXML
    ComboBox monthBox;
    @FXML
    ComboBox yearBox;
    @FXML
    ComboBox fromWeekBox;
    @FXML
    ComboBox toWeekBox;
    @FXML
    Button menuButton;
    @FXML
    TableView apptTable;
    @FXML
    Button viewButton;
    @FXML
    Button yearConfirmButton;
    @FXML
    Button monthButton;
    @FXML
    Button weekRangeButton;
    @FXML
    TableColumn titleColumn;
    @FXML
    TableColumn custColumn;
    @FXML
    TableColumn startColumn;
    @FXML
    TableColumn endColumn;
    @FXML
    TableColumn descColumn;
    @FXML
    TableColumn locColumn;
    @FXML
    TableColumn dayColumn;
    @FXML
    Button viewByMonth;

    int year;
    Connection conn;
    LocalDate startDate;

    /*
    The takeBackToMenu(ActionEvent) method is the logic behind the menuButton that 
    takes the user back to the main menu.
    It takes the ActionEvent (a mouse click) as the paraemeter
    It returns nothing.
     */
    public void takeBackToMenu(ActionEvent ae) throws Exception {
        Stage currentStage = (Stage) menuButton.getScene().getWindow();
        currentStage.close();
        Stage scheduleStage = new Stage();
        Parent schedulePane = FXMLLoader.load(getClass().getResource("Menu.fxml"));
        Scene scene = new Scene(schedulePane);
        scheduleStage.setScene(scene);
        scheduleStage.show();
    }

    /*
    The fetchUser() method returns the username of the user logged in.
    @return usernames
     */
    private String fetchUser() {
        FXMLDocumentController doc = new FXMLDocumentController();
        String username = doc.getUserName();
        return username;
    }

    /*
    Shows appointments for the current week.
    Takes a mouse click as a parameter and returns nothing
    @param ActionEvent ae
    @return nothing
     */
    public void printAppointmentsByWeek(ActionEvent ae) throws Exception {
        //String endWeek = toWeekBox.getSelectionModel().getSelectedItem().toString();
        //String startWeek = fromWeekBox.getSelectionModel().getSelectedItem().toString();
        //LocalDate startDate = LocalDate.parse(startWeek);
        //LocalDate endDate = LocalDate.parse(endWeek);
        SQLConnector connector = new SQLConnector();
        String user = fetchUser();
        //   ArrayList<String> appointments = new ArrayList<String>();
        final ObservableList<Appointment> appts = FXCollections.observableArrayList();
        try {
            connector.connectToDB();
            conn = connector.getConnection();
            String weekQueryStr
                    = "SELECT customerName, title,description,location,start,end, DAYNAME(start) AS dayname "
                    + "FROM appointment a JOIN customer c ON c.customerID = a.customerId \n "
                    + "WHERE YEARWEEK(start) = YEARWEEK(NOW()) "
                    + "AND a.createdBy ='" + user + "' ORDER BY start";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(weekQueryStr);
            while (rs.next()) {
                Timestamp startTS = rs.getTimestamp("start");
                Timestamp endTS = rs.getTimestamp("end");
                String title = rs.getString("title");
                String customer = rs.getString("customerName");
                String location = rs.getString("location");
                String description = rs.getString("description");
                String dowStr = rs.getString("dayname");
                Appointment appointment = new Appointment(title, customer, description, location, startTS, endTS, dowStr);
                appts.add(appointment);
                System.out.println(appointment.printAppt());
            }

            apptTable.setItems(appts);

            titleColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("title"));
            custColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("customer"));
            startColumn.setCellValueFactory(new PropertyValueFactory<Appointment, Timestamp>("startTime"));
            endColumn.setCellValueFactory(new PropertyValueFactory<Appointment, Timestamp>("endTime"));
            locColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("location"));
            dayColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("DOW"));
            descColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("description"));

            apptTable.getColumns().setAll(dayColumn, titleColumn, custColumn, startColumn, endColumn, descColumn, locColumn);

        } catch (SQLException sqle) {
            System.out.println("SQLException encountered at print appointmentByWeek method");
            System.out.print(sqle.fillInStackTrace());
        } catch (ClassNotFoundException e) {
            System.out.println("ClassNotFoundException encountered at printAppointmentsByWeek method");
        } finally {
            conn.close();
        }

    }

        /*
    Shows appointments for the current month.
    Takes a mouse click as a parameter and returns nothing
    @param ActionEvent ae
    @return nothing
     */
    
    public void printAppointmentsByMonth(ActionEvent ae) throws Exception {
        SQLConnector connector = new SQLConnector();
        ArrayList<String> appointments = new ArrayList<String>();
        String user = fetchUser();
        final ObservableList<Appointment> appts = FXCollections.observableArrayList();
        try {
            connector.connectToDB();
            conn = connector.getConnection();
            String monthQueryStr
                    = "SELECT customerName, title,description,location,start,end, DAYNAME(start) AS dayname "
                    + "FROM appointment a JOIN customer c ON c.customerID = a.customerId "
                    + "WHERE MONTH(start) = MONTH(NOW())"
                    + " AND a.createdBy ='" + user + "' ORDER BY start";
            Statement stmt = conn.createStatement();
            //      System.out.println(startDate + " " + endDate);
            ResultSet rs = stmt.executeQuery(monthQueryStr);
            while (rs.next()) {
                Timestamp startTS = rs.getTimestamp("start");
                Timestamp endTS = rs.getTimestamp("end");
                String title = rs.getString("title");
                String customer = rs.getString("customerName");
                String location = rs.getString("location");
                String description = rs.getString("description");
                String dowStr = rs.getString("dayname");
                Appointment appointment = new Appointment(title, customer, description, location, startTS, endTS, dowStr);
                appts.add(appointment);
                System.out.println(appointment.printAppt());
            }

            apptTable.setItems(appts);

            titleColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("title"));
            custColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("customer"));
            startColumn.setCellValueFactory(new PropertyValueFactory<Appointment, Timestamp>("startTime"));
            endColumn.setCellValueFactory(new PropertyValueFactory<Appointment, Timestamp>("endTime"));
            locColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("location"));
            dayColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("DOW"));
            descColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("description"));

            apptTable.getColumns().setAll(dayColumn, titleColumn, custColumn, startColumn, endColumn, descColumn, locColumn);

        } catch (SQLException sqle) {
            System.out.println("SQLException encountered at print appointmentByWeek method");
            System.out.print(sqle.fillInStackTrace());
        } catch (ClassNotFoundException e) {
            System.out.println("ClassNotFoundException encountered at printAppointmentsByWeek method");
        } finally {
            conn.close();
        }

    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

    }

}
