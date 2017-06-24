/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package c195fxmlapplication;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

/**
 *
 * @author user
 */

/*
The appointment class holds details for each appointment to be displayed in the 
CalendarViewer.  
Global variables: 
String title - The title of the appointment
String customer - The cusotmer namer for the appointment
String desc -- Appointment description
String location - Appointment location
Timestamp startTime - The appointment starting time
Timestamp endTime - The appointment ending time
String dow - The day of the week for the appointment
 */
public class Appointment {

    String title;
    String customer;
    String desc;
    String location;
    Timestamp startTime;
    Timestamp endTime;
    String dow;

    /*
    Appointment constructor creates an instance of an appointment
    @param String titleStr, String customerStr, String descriptionStr,
     String locationStr, Timestamp start, Timestamp end, String dowStr
     */
    public Appointment(String titleStr, String customerStr, String descriptionStr,
            String locationStr, Timestamp start, Timestamp end, String dowStr) {
        title = titleStr;
        customer = customerStr;
        desc = descriptionStr;
        location = locationStr;
        startTime = start;
        endTime = end;
        dow = dowStr;
    }

    /*
    Getter method for the day of the week
    @return String dow (Day of week for the appointment
     */
    public String getDOW() {
        return dow;
    }

    /*
  Getter method for the start of the appointment
    @return String start time of the appointment
     */
    public String getStartStr() {
        DateTimeFormatter dtf = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
        LocalDateTime ldt = startTime.toLocalDateTime();
        String startStr = ldt.format(dtf);
        return startStr;
    }

    /*
  Getter method for the end of the appointment
    @return String end time of the appointment
     */
    public String getEndStr() {
        DateTimeFormatter dtf = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
        LocalDateTime ldt = endTime.toLocalDateTime();
        String endStr = ldt.format(dtf);
        return endStr;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /*
  Getter method for the title of the appointment
    @return String tile for the title of the appointment
     */
    public String getTitle() {
        return title;
    }

    public void setDescription(String description) {
        this.desc = description;
    }

    /*
  Getter method for the description of the appointment
    @return String tile for the description of the appointment
     */
    public String getDescription() {
        return desc;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    /*
  Getter method for the customer of the appointment
    @return String customer for the customer of the appointment
     */
    public String getCustomer() {
        return customer;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    /*
  Getter method for the location of the appointment
    @return String location for the location of the appointment
     */
    public String getLocation() {
        return location;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    /*
  Getter method for the title of the appointment
    @return Timestamp startTime for the start time of the appointment
     */
    public Timestamp getStartTime() {
        return startTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    /*
  Getter method for the end time of the appointment
    @return Timestamp  endTime for the end time of the appointment
     */
    public Timestamp getEndTime() {
        return endTime;
    }

    /*
  printAppts() method to print the entire appointment
    @return String for all of the values of the appointment
     */

    public String printAppt() {
        String startStr = startTime.toString();
        String endStr = endTime.toString();
        String titleStr = title.toString();
        String customerStr = customer.toString();
        String locationStr = location.toString();
        String descStr = desc.toString();
        String apptStr
                = titleStr + "\n" + customerStr + "\n" + locationStr + "\n" + descStr + "\n" + startStr + "\n" + endStr + "\n" + dow;

        return apptStr;
    }

}
