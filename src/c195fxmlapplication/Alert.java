/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package c195fxmlapplication;

import static java.lang.Thread.sleep;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.Container;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import javafx.application.Platform;

/**
 *
 * @author user
 */

/*
The Alert class exists to trigger popup notifications to inform users of upcoming appointments
It has four static varaibles that have to do with the time and description of the appointment.
 */

public class Alert {

    static LocalDateTime testTime = LocalDateTime.now();
    static LocalDateTime apptTime;
    static LocalDateTime alarmTime;
    static LocalDateTime currentTime;
    int done;

    private String fetchUser() {
        FXMLDocumentController doc = new FXMLDocumentController();
        String username = doc.getUserName();
        return username;
    }

    /*
    the fetchAppointments() takes no parameters
    It reaches out to MySQL tables appointments and customers to get the customerName, start, and end of the 
    appointment.
    It retrieves information from MySQL relevant for appointments starting within
    fifteen minutes from the current Eastern Standard Time (US)
    It gets that information and adds it to an ArrayList <String> and returns that list.
    @return ArrayList<String> apptStrings
     */
    
    private ArrayList fetchAppointments() {
        ArrayList<LocalDateTime> dates = new ArrayList<LocalDateTime>();
        SQLConnector connector = new SQLConnector();
        ArrayList<String> customerNames = new ArrayList<String>();
        ArrayList<String> apptStrings = new ArrayList<String>();
        String username = fetchUser();
        try {
            connector.connectToDB();
            Connection conn = connector.getConnection();

            Statement stmt = conn.createStatement();
            String query = "SELECT start, end, customerName FROM appointment a "
                    + "JOIN customer c ON c.customerID = a.customerId "
                    + "WHERE a.createdBy = '" + username
                    + "' AND TIMESTAMPDIFF(HOUR,DATE_SUB(NOW(),INTERVAL 4 HOUR), start)<15 "
                    + "AND TIMESTAMPDIFF(MINUTE,DATE_SUB(NOW(),INTERVAL 4 HOUR), start) >0";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                Timestamp date = rs.getTimestamp("start");
                System.out.println(date);
                dates.add(rs.getTimestamp("start").toLocalDateTime());
                customerNames.add(rs.getString("customerName"));
                apptStrings.add("Appointment at: " + rs.getTimestamp("start").toString() + " with customer: " + rs.getString("customerName"));

            }

        } catch (SQLException sqle) {
            System.out.println("SQLException caught");
            sqle.printStackTrace();;
        } catch (ClassNotFoundException e) {
            System.out.println("ClassNotFoundException encountered");
        }

        return apptStrings;

    }

    /*
    The triggerAlarm() method returns nothing and takes not parameters.
    It simply triggers the popup alert containing appointment information (star time and customer)
     */
    
    public void triggerAlarm() {
        ArrayList<LocalDateTime> dates = fetchAppointments();

        String appts = showDates();
        Runner runner = new Runner();
        if (dates.isEmpty()) {
            runner.run(() -> System.out.println("No appointments"),
                    "You don't seem to have any appointments anytime soon.\n\nMust be nice...", 
                    "Appointments soon!", "No appointments yet");
        } else {
            runner.run(() -> System.out.println("Appointments"), appts, 
                    "Appointments soon!", "You have appointments soon");
        }
    }
    
    /*
    showDates() fills in the popup alert text from the fetchAppointments() method
    @return String apptStr
    */
    
    private String showDates() {
        String apptStr = "You have the following appointments in less than fifteen minutes:\n";
        ArrayList<String> dates = fetchAppointments();
        for (String date : dates) {
            apptStr += date + "\n";
            System.out.println(date);
        }
        return apptStr;
    }

}
