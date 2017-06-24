/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package c195fxmlapplication;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author user
 */
public class ScheduleFormController extends FXMLDocumentController implements Initializable {

    @FXML
    DatePicker appointmentDatePicker;
    @FXML
    Label dateLabel;
    @FXML
    Label timeLabel;
    @FXML
    Label customerLabel;
    @FXML
    Label startTimeLabel;
    @FXML
    Label endTimeLabel;
    @FXML
    Label apptDescLabel;
    @FXML
    Label apptLocationLabel;
    @FXML
    Label contactLabel;
    @FXML
    TextField custNameField;
    @FXML
    ComboBox startTimeField;
    @FXML
    ComboBox endTimeField;
    @FXML
    TextField descField;
    @FXML
    TextField locationField;
    @FXML
    TextField contactField;
    @FXML
    Button menuButton;
    @FXML
    Button submitButton;
    @FXML
    Button updateAppointmentButton;
    @FXML
    AnchorPane schedulePane;
    @FXML
    TextField apptTitleField;
    @FXML
    Label apptTitleLabel;
    @FXML
    Label urlLabel;
    @FXML
    TextField urlField;
    @FXML
    ComboBox customerBox;
    @FXML
    ComboBox apptBox;
    @FXML
    Button updateApptButton;
    @FXML
    Button confirmButton;
    Connection conn;
    Runner runner = new Runner();
    UserLogger logger = new UserLogger();
    SQLConnector connector = new SQLConnector();

    /*
    The takeBackToMenu method takes ActionEvent as a parameter as a click from the menuButton 
    Takes the user back to Menu.fxml
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

    public void takeToUpdateForm(ActionEvent ae) throws Exception {
        Stage currentStage = (Stage) updateAppointmentButton.getScene().getWindow();
        currentStage.close();
        Stage scheduleStage = new Stage();
        Parent schedulePane = FXMLLoader.load(getClass().getResource("ModifyAppointment.fxml"));
        Scene scene = new Scene(schedulePane);
        scheduleStage.setScene(scene);
        scheduleStage.show();
    }

    /*
    A getter method that returns a LocalDate, i.e. appointmentDate
    @return LocalDate appointmentDate
     */
    private LocalDate fetchDate() {
        LocalDate appointmentDate = appointmentDatePicker.getValue();
        return appointmentDate;
    }

    /*
    A getter method that returns a LocalTime, that is startTime
     */
    private LocalTime fetchStartTime() {
        LocalTime startTime = (LocalTime) startTimeField.getValue();
        return startTime;
    }

    /*
    A getter method that returns a LocalTime, that is endTime
     */
    private LocalTime fetchEndTime() {
        LocalTime endTime = (LocalTime) endTimeField.getValue();
        return endTime;
    }

    public void confirmAppointment(ActionEvent ae) throws SQLException {
        try{
        int apptID = fetchAppointment();
        String query = "SELECT * FROM appointment WHERE appointmentID = " + apptID;
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            apptTitleField.setText(rs.getString("title"));
            urlField.setText(rs.getString("url"));
            locationField.setText(rs.getString("location"));
            contactField.setText(rs.getString("contact"));
            descField.setText(rs.getString("description"));
        }
        }
        catch(ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    /*
    A getter method that returns a LocalDateTime, that is endTime
    Formats the value taken from LocalTime fetchEndTime() and converts it to a LocalDateTime
    (Tacks a date onto the appointment end time)
     */
    private LocalDateTime fetchAppointmentEndDateTime() {
        String date = fetchDate().toString();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("hh:mm a");
        String time = fetchEndTime().format(dtf);
        String dateTime = date + " " + time;

        DateTimeFormatter apptDate = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a");

        LocalDateTime endTime = LocalDateTime.parse(dateTime, apptDate);
        System.out.println(time);

        return endTime;
    }

    /*
    A getter method that returns a LocalDateTime, that is startTime
    Formats the value taken from LocalTime fetchStartTime() and converts it to a LocalDateTime
    (Tacks a date onto the appointment start time)
     */
    private LocalDateTime fetchAppointmentStartDateTime() {
        String date = fetchDate().toString();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("hh:mm a");
        String time = fetchStartTime().format(dtf);
        String dateTime = date + " " + time;
        DateTimeFormatter apptDate = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a");
        LocalDateTime startTime = LocalDateTime.parse(dateTime, apptDate);
        System.out.println(time);
        return startTime;
    }

    /*
    A getter method that returns a String (username)
    Fetches the String value that was input as the username from the Login screen 
    to ensure that it is the user who is accessesing the database.
    (Tacks a date onto the appointment end time)
     */
    private String fetchUser() {
        FXMLDocumentController doc = new FXMLDocumentController();
        String username = doc.getUserName();
        return username;
    }

    /*
    A getter method that returns a String (title)  from the apptTitleField 
    @return String title
     */
    private String fetchTitle() {
        String title = apptTitleField.getText();
        return title;
    }

    /*
    A getter method that returns a String (title)  from the fetchContact 
    @return String contact
     */
    private String fetchContact() {
        String contact = contactField.getText();
        return contact;
    }

    /*
    Populates the ComboBox customerBox  with customerName as listed in the customer table
    in the MySQL DB
     */
    private void populateCustomerBox() throws ClassNotFoundException {
        String query = "SELECT customerName FROM customer WHERE active =1";

        connector.connectToDB();

        conn = connector.getConnection();
        try {
            Statement stmt = conn.createStatement();
            System.out.println("Connected");
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                customerBox.getItems().add(rs.getString("customerName"));

            }
        } catch (SQLException sqle) {
            sqle.getStackTrace();
        }
    }

    /*
    fetchCustomer() gets the customer from customerBox and then verifies that the name actually exists in the customer table in
    the MySQL DB.
    Returns the customer's customerId in the database.
    @return int customerID
     */
    private int fetchCustomer() throws ClassNotFoundException {

        connector.connectToDB();
        conn = connector.getConnection();
        String customerNameStr = customerBox.getSelectionModel().getSelectedItem().toString();
        int customerID = 0;

        String query = "SELECT customerId, customerName FROM customer WHERE customerName = '" + customerNameStr + "'";
        try {
            Statement stmt = conn.createStatement();
            System.out.println("Connected");
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                customerID = rs.getInt("customerId");
                String customerName = rs.getString("customerName");
                if (customerName.equals("")) {
                    runner.run(() -> System.out.println(), "There is no customer by that name",
                            "Missing customer error", "Missing customer");

                } else {
                    System.out.println("Customer found");
                }
            }
        } catch (SQLException sqle) {
            System.out.println("SQLException encountered");
        }

        return customerID;
    }

    private String fetchDescription() {
        String desc = descField.getText();
        return desc;
    }

    private String fetchLocation() {
        String location = locationField.getText();
        return location;
    }

    private String fetchURL() {
        String url = urlField.getText();
        return url;
    }

    /*
    Connects to the MySQL Database    
    Inserts the appointment into the appointment table in the MySQL Database.
    Runs the query with the parameters taken from the ScheduleForm.
        
     */
    private void insertAppointment(int custID, String title, String description, String location, String contact,
            String url, LocalDateTime apptStart, LocalDateTime apptEnd, LocalDateTime createDate, String createBy,
            LocalDateTime update, String updateBy) throws ClassNotFoundException {

        connector.connectToDB();
        conn = connector.getConnection();
        try {
            Statement stmt = conn.createStatement();
            String query = "INSERT INTO appointment (customerID,title,description,location,contact,"
                    + "url,start,end,createDate,createdBy,lastUpdate,lastUpdateBy) VALUES ("
                    + custID + ",'" + title + "','" + description + "','" + location + "','" + contact + "','"
                    + url + "','" + apptStart + "','" + apptEnd + "','" + createDate + "','" + createBy + "','"
                    + update + "','" + updateBy + "')";

            stmt.execute(query);

            try {
                logger.writeToLog("Appointment scheduled by user " + createBy);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            System.out.println("SQLException caught duirng appointment insert");
        }
    }

    /*
    The checkScheduleConflicts() makes sure that the newly inserted appointment does not conflict with any 
    preexisting appointments.
    Calls fetchAppointmentStartDateTime()
    Calls fetchAppointmentEndDateTime()
    Sets the base time zone to New York EST.
     */
    public boolean checkScheduleConflicts() {
        boolean conflict = false;
        LocalDateTime appointmentStart = fetchAppointmentStartDateTime();
        LocalDateTime appointmentEnd = fetchAppointmentEndDateTime();
        ZoneId NYZoneId = ZoneId.of("America/New_York");
        ZonedDateTime start = appointmentStart.atZone(NYZoneId).withZoneSameInstant(NYZoneId);
        ZonedDateTime end = appointmentEnd.atZone(NYZoneId).withZoneSameInstant(NYZoneId);
        Timestamp apptStart = new Timestamp(start.toInstant().getEpochSecond() * 1000L);
        Timestamp apptEnd = new Timestamp(end.toInstant().getEpochSecond() * 1000L);

        String user = fetchUser();

        try {
            connector.connectToDB();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        conn = connector.getConnection();
        try {
            Statement stmt = conn.createStatement();
            String query = "SELECT start,end, a.customerId, customerName FROM appointment a "
                    + "JOIN customer c ON a.customerId=c.customerID WHERE a.createdBy='" + user + "'";
            //stmt.executeQuery(()->query);
            ResultSet result = stmt.executeQuery(query);
            while (result.next()) {
                Timestamp startDate = result.getTimestamp("start");
                Timestamp endDate = result.getTimestamp("end");
                if ((apptStart.after(startDate) && apptStart.before(endDate)) || (apptEnd.after(startDate)
                        && apptEnd.before(endDate))
                        || apptStart.equals(startDate) || apptEnd.equals(endDate)) {
                    System.out.println("Proposed appointment starts: " + apptStart + " Proposed appointment end " + apptEnd);
                    System.out.println("Start date: " + startDate + " End date:" + endDate);
                    System.out.println("During appointment");
                    conflict = true;
                    return conflict;
                } else {
                    System.out.println("Before or after appointment");
                    conflict = false;
                }

                System.out.println(startDate);
                System.out.println(endDate);
            }
        } catch (SQLException sqle) {
            System.out.println("SQLException encountered in check schedule conflicts method");
            sqle.printStackTrace();

        }
        return conflict;
    }

    /*
    Creates an appointment to be added into the MySQL Database in the table appointment
    Calls insertAppointment()
    Calls fetchUser()
    Calls fetchAppointmentStartDateTime()
    Calls fetchAppointmentEndDateTime()
    Calls fetchLocation(), fetchDescription(), fetchContact(), fetchTitle(), fetchURL() for
    all the parameters needed to create the appointment.
    Calls insertAppointment()
     */
    public void createAppointment(ActionEvent ae) throws ClassNotFoundException {
        LocalDateTime appointmentStart = fetchAppointmentStartDateTime();
        LocalDateTime appointmentEnd = fetchAppointmentEndDateTime();
        String customerNameStr = customerBox.getSelectionModel().getSelectedItem().toString();
        String user = fetchUser();
        String location = fetchLocation();
        String description = fetchDescription();
        String contact = fetchContact();
        String title = fetchTitle();
        String url = fetchURL();
        ZoneId NYZoneId = ZoneId.of("America/New_York");

        ZonedDateTime apptStart = appointmentStart.atZone(NYZoneId).withZoneSameInstant(NYZoneId);
        ZonedDateTime apptEnd = appointmentEnd.atZone(NYZoneId).withZoneSameInstant(NYZoneId);
        Timestamp start = new Timestamp(apptStart.toInstant().getEpochSecond() * 1000L);

        Timestamp end = new Timestamp(apptEnd.toInstant().getEpochSecond() * 1000L);
        //businessEndTime = businessEndTime.withHour(18);
        LocalDateTime createDate = LocalDateTime.now();
        LocalDateTime update = LocalDateTime.now();
        int custID = fetchCustomer();

        try {
            connector.connectToDB();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        conn = connector.getConnection();
        if (custID == 0) {
            runner.runError(() -> System.out.println(), "There is no customer by that name",
                    "Missing customer error", "Missing customer");

            //System.out.println("That is not a valid customer");
        } else if (appointmentStart.compareTo(LocalDateTime.now()) <= 0) {
            runner.runError(() -> System.out.println(), "The appointment cannot be earlier than the current time/date",
                    "Attempt to schedule before current date", "Invalid time");

            //  System.out.println("The appointment cannot be earlier than the current time.");
        } else if (checkScheduleConflicts()) {

            runner.runError(() -> System.out.println(), "There is already an appointment schedule for that time",
                    "Conflicting appointments", "Conflicting appointments");

        } else if (appointmentStart.compareTo(appointmentEnd) >= 0 || appointmentStart.compareTo(appointmentEnd) == 0) {
            runner.runError(() -> System.out.println(), "The end time for an appointment must come after the start time",
                    "Invalid time slot", "Invalid time slot");

//            System.out.println("The end time for the appointment must be at a later time than the start date");
            System.out.println(appointmentStart.toString());
            System.out.println(appointmentEnd.toString());
            System.out.println(appointmentStart.compareTo(appointmentEnd));
        } else if ((apptStart.getHour() > 18 || apptEnd.getHour() >= 18)
                || (apptStart.getHour() <= 6 || apptEnd.getHour() < 6)) {
            runner.runError(() -> System.out.println(), "The chosen time slot is not during business hours.  "
                    + "Please note that business hours are "
                    + "from 6:00 AM until 6:00 PM EST",
                    "Invalid time slot", "Invalid time slot");
            // System.out.println("That time is not during business hours.  Please choose another time.");
        } else if (appointmentStart.equals(null) || appointmentEnd.equals(null)) {
            System.out.println("You must choose a start and end time");
        } else {
            System.out.println("Appointment made by " + user + " at: ");
            System.out.println(appointmentStart.toString());
            System.out.println(appointmentEnd.toString());
            runner.run(() -> System.out.println(), "The appointment has successfully been made for "
                    + customerNameStr + " at " + appointmentStart + " until " + appointmentEnd,
                    "Appointment made", "Success!");
            System.out.println("Appointment made for customer with customerID " + custID);
            insertAppointment(custID, title, description, location, contact, url, appointmentStart,
                    appointmentEnd, createDate, user, update, user);
            apptTitleField.setText("");
            urlField.setText("");
            locationField.setText("");
            contactField.setText("");
            descField.setText("");
            startTimeField.setValue(null);
            endTimeField.setValue(null);
            custNameField.setText("");

        }
    }

    private void populateApptSelectorBox() throws ClassNotFoundException {
        connector.connectToDB();
        conn = connector.getConnection();
        String user = fetchUser();
        String query = "SELECT start, end, customerName FROM appointment a JOIN customer c ON"
                + " a.customerId = c.customerID AND a.createdBy = '" + user + "'";
        String cName = "";
        LocalDateTime startDT;
        LocalDateTime endDT;
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                cName = rs.getString("customerName");
                startDT = rs.getTimestamp("start").toLocalDateTime();
                endDT = rs.getTimestamp("end").toLocalDateTime();
                String apptStr = "Appointment for: " + cName + " at " + startDT + " until " + endDT;
                apptBox.getItems().add(apptStr);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public String fetchAppointmentName() throws ClassNotFoundException {
        String apptSelected = apptBox.getSelectionModel().getSelectedItem().toString();
        String name = "";
        String start = "";
        String end = "";
        int apptID = 0;
        String[] splitSelected = apptSelected.split(" ");
        if (splitSelected.length > 7) {
            name = splitSelected[2] + " " + splitSelected[3];

            start = splitSelected[5];
        } else {
            name = splitSelected[2];
            start = splitSelected[4];
        }
        return name;
    }

    public int fetchAppointment() throws ClassNotFoundException {
        String apptSelected = apptBox.getSelectionModel().getSelectedItem().toString();
        String name = "";
        String start = "";
        String end = "";
        int apptID = 0;
        String[] splitSelected = apptSelected.split(" ");
        if (splitSelected.length > 7) {
            name = splitSelected[2] + " " + splitSelected[3];

            start = splitSelected[5];
        } else {
            name = splitSelected[2];
            start = splitSelected[4];
        }

        System.out.println(name);

        DateTimeFormatter apptDate = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime startTime = LocalDateTime.parse(start, apptDate);

        System.out.println(
                "Parsed start = " + startTime);
        connector.connectToDB();
        conn = connector.getConnection();
        String query = "SELECT appointmentID FROM appointment a JOIN customer c ON"
                + " a.customerId = c.customerID WHERE customerName LIKE ('%" + name + "%') AND "
                + "start ='" + startTime + "'";
        String cName = "";
        LocalDateTime startDT;
        LocalDateTime endDT;

        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                apptID = rs.getInt("appointmentID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println(
                "appointmentID = " + apptID);
        return apptID;
    }

    /*
     */
    public void updateAppointment(ActionEvent ae) throws ClassNotFoundException {
        int apptID = fetchAppointment();
        String custName = fetchAppointmentName();
        LocalDateTime appointmentStart = fetchAppointmentStartDateTime();
        LocalDateTime appointmentEnd = fetchAppointmentEndDateTime();
        String user = fetchUser();
        String location = fetchLocation();
        String description = fetchDescription();
        String contact = fetchContact();
        String title = fetchTitle();
        String url = fetchURL();
        ZoneId NYZoneId = ZoneId.of("America/New_York");

        ZonedDateTime apptStart = appointmentStart.atZone(NYZoneId).withZoneSameInstant(NYZoneId);
        ZonedDateTime apptEnd = appointmentEnd.atZone(NYZoneId).withZoneSameInstant(NYZoneId);
        Timestamp start = new Timestamp(apptStart.toInstant().getEpochSecond() * 1000L);

        Timestamp end = new Timestamp(apptEnd.toInstant().getEpochSecond() * 1000L);
        //businessEndTime = businessEndTime.withHour(18);
        LocalDateTime update = LocalDateTime.now();
        try {
            connector.connectToDB();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        conn = connector.getConnection();
        if (apptID == 0) {
            runner.runError(() -> System.out.println("Appointment not selected"), "You must select an appointment before"
                    + " updating it", "No appointment selected", "No appointment selected");
        } else if (appointmentStart.compareTo(LocalDateTime.now()) <= 0) {
            runner.runError(() -> System.out.println(), "The appointment cannot be earlier than the current time/date",
                    "Attempt to schedule before current date", "Invalid time");

            //  System.out.println("The appointment cannot be earlier than the current time.");
        } else if (checkScheduleConflicts()) {

            runner.runError(() -> System.out.println(), "There is already an appointment schedule for that time",
                    "Conflicting appointments", "Conflicting appointments");
        } else if (appointmentStart.compareTo(appointmentEnd) >= 0 || appointmentStart.compareTo(appointmentEnd) == 0) {
            runner.runError(() -> System.out.println(), "The end time for an appointment must come after the start time",
                    "Invalid time slot", "Invalid time slot");

//            System.out.println("The end time for the appointment must be at a later time than the start date");
            System.out.println(appointmentStart.toString());
            System.out.println(appointmentEnd.toString());
            System.out.println(appointmentStart.compareTo(appointmentEnd));
        } else if ((apptStart.getHour() > 18 || apptEnd.getHour() >= 18)
                || (apptStart.getHour() <= 6 || apptEnd.getHour() < 6)) {
            runner.runError(() -> System.out.println(), "The chosen time slot is not during business hours.  "
                    + "Please note that business hours are "
                    + "from 6:00 AM until 6:00 PM EST",
                    "Invalid time slot", "Invalid time slot");
            // System.out.println("That time is not during business hours.  Please choose another time.");
        } else if (appointmentStart.equals(null) || appointmentEnd.equals(null)) {
            System.out.println("You must choose a start and end time");
        } else {
            updateAppointment(apptID, title, description, location, contact, url, appointmentStart, appointmentEnd, update, user);
            System.out.println("Appointment made by " + user + " at: ");
            System.out.println(appointmentStart.toString());
            System.out.println(appointmentEnd.toString());
            runner.run(() -> System.out.println(), "The appointment has successfully been updated for "
                    + custName + " at " + appointmentStart + " until " + appointmentEnd,
                    "Appointment made", "Success!");
            apptTitleField.setText("");
            urlField.setText("");
            locationField.setText("");
            contactField.setText("");
            descField.setText("");
            startTimeField.setValue(null);
            endTimeField.setValue(null);

        }
    }

    private void updateAppointment(int apptID, String title, String description, String location, String contact,
            String url, LocalDateTime apptStart, LocalDateTime apptEnd, LocalDateTime update, String updateBy) throws ClassNotFoundException {

        connector.connectToDB();
        conn = connector.getConnection();
        try {
            Statement stmt = conn.createStatement();
            String query = "UPDATE appointment SET title='" + title + "',description='" + description + "',location='" + location + "',contact='" + contact
                    + "',url='" + url + "',start='" + apptStart + "',end='" + apptEnd + "',lastUpdate='" + update
                    + "',lastUpdateBy='" + updateBy + "' WHERE appointmentID = " + apptID;

            stmt.execute(query);

            try {
                logger.writeToLog("Appointment updated by user " + updateBy);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            System.out.println("SQLException caught during appointment insert");
            e.printStackTrace();
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {
            populateApptSelectorBox();
            populateCustomerBox();
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        }
        LocalTime start = LocalTime.MIDNIGHT;
        do {
            start = start.plusMinutes(15);
            startTimeField.getItems().add(start);
            endTimeField.getItems().add(start);
        } while (start.getHour() < 23 && start.getMinute() < 59);
    }

}
