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
public class ReportAppointment {

    String title;
    String customer;
    String desc;
    String location;
    Timestamp startTime;
    Timestamp endTime;
    String type;
    String consultant;

    public ReportAppointment(String titleStr, String customerStr, String descriptionStr,
            String locationStr, Timestamp start, Timestamp end, String consultantStr) {
        title = titleStr;
        customer = customerStr;
        desc = descriptionStr;
        location = locationStr;
        startTime = start;
        endTime = end;
        consultant = consultantStr;
       // type = typeStr;

    }
/*
    public String getType() {
        return type;
    }*/

    public String getConsultant() {
        return consultant;

    }

    public String getStartStr() {
        DateTimeFormatter dtf = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
        LocalDateTime ldt = startTime.toLocalDateTime();
        String startStr = ldt.format(dtf);
        return startStr;
    }

    public String getEndStr() {
        DateTimeFormatter dtf = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
        LocalDateTime ldt = endTime.toLocalDateTime();
        String endStr = ldt.format(dtf);
        return endStr;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setDescription(String description) {
        this.desc = description;
    }

    public String getDescription() {
        return desc;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getCustomer() {
        return customer;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public String printAppt() {
        String startStr = startTime.toString();
        String endStr = endTime.toString();
        String titleStr = title.toString();
        String customerStr = customer.toString();
        String locationStr = location.toString();
        String descStr = desc.toString();
        String apptStr
                = titleStr + "\n" + customerStr + "\n" + locationStr + "\n" + descStr + "\n" + startStr + "\n" + endStr + "\n";

        return apptStr;
    }

}
