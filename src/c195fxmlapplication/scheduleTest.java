/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package c195fxmlapplication;

import static java.lang.Thread.sleep;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public class scheduleTest {

  
    public scheduleTest() {
    }

    public void runQuery() throws SQLException {
        // System.out.println("Hello");

        LocalDateTime ldt1 = LocalDateTime.of(2017, 6, 1, 14, 45, 0);
        LocalDateTime ldt2 = LocalDateTime.of(2017, 6, 1, 15, 45, 0);
        ZoneId NYZoneId = ZoneId.of("America/New_York");
        ZonedDateTime zdt1 = ZonedDateTime.of(ldt1, NYZoneId);
        Timestamp zoneToTS = new Timestamp(zdt1.toInstant().getEpochSecond() * 1000L);
        System.out.println(zoneToTS);
        System.out.println(ldt1);
        System.out.println(zdt1);
        Timestamp tryStartDate = Timestamp.valueOf(ldt1);
        Timestamp tryEndDate = Timestamp.valueOf(ldt2);
        String ds = ldt1.toString();
        System.out.println(ldt1);
        SQLConnector connector = new SQLConnector();
        try {
            connector.connectToDB();
        } catch (ClassNotFoundException e) {
        }
        Connection conn = connector.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet result = stmt.executeQuery("SELECT start,end, a.customerId, customerName FROM appointment a\n"
                + "JOIN customer c ON a.customerId=c.customerID WHERE a.createdBy='CTRobot'");
        while (result.next()) {
            Timestamp startDate = result.getTimestamp("start");
            Timestamp endDate = result.getTimestamp("end");
            if ((tryStartDate.after(startDate) && tryStartDate.before(endDate)) || (tryEndDate.after(startDate) && tryStartDate.before(endDate))) {
                System.out.println("Proposed appointment start: " + tryStartDate + " Proposed appointment end " + tryEndDate);
                System.out.println("Start date: " + startDate + " End date:" + endDate);
                System.out.println("During appointment");
            } else {
                System.out.println("Before or after appointment");
            }
            System.out.println(startDate);
            System.out.println(endDate);
        }

    }

    public static void main(String[] args) throws Exception {
        scheduleTest st = new scheduleTest();
        st.runQuery();

    }
    }
