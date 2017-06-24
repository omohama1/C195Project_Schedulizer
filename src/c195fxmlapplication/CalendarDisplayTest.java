/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package c195fxmlapplication;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;

/**
 *
 * @author user
 */
public class CalendarDisplayTest {

    /**
     * @param args the command line arguments
     */
    static Connection conn;
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        //timeSpinner.setValue(newDate());
        // TODO code application logic here
        int range=7;
        SQLConnector connector = new SQLConnector();
        String [] appointmentDays= new String[30];
        LocalDateTime[] apptDates = new LocalDateTime[30];
        int weekOffset= 1;
        LocalDate dtWeekStart = LocalDate.of(2017, Month.JANUARY, 1);
        LocalDate dtWeekEnd = dtWeekStart.plusDays(6);
        
        for(int i = 0; i<53;i++){
            System.out.println("Select week: " +dtWeekStart + " through " + dtWeekEnd);
            dtWeekStart = dtWeekStart.plusWeeks(1);
            dtWeekEnd = dtWeekEnd.plusWeeks(1);
            
        }
        
        
        LocalDate dt1 = LocalDate.of(2017, Month.MAY,25);
        dt1=dt1.plusWeeks(weekOffset);
        Date [] dates = new Date[range];
        String dayNames = "";
        LocalDate[] weekDays = new LocalDate[range];
        for(int i = 0;i<6;i++){
            LocalDate x = dt1.plusDays(i);
            dayNames+="   " +x.getDayOfWeek() + " ";
            weekDays[i]=x;
            dates[i]=Date.valueOf(x);
          //  System.out.println(dates[i]);
            
        }
        
        /*
        for(int i =0;i<dates.length;i++){
            System.out.println(dates[i]);
        }
        */
        connector.connectToDB();
        System.out.println("Connected");
        String query = "SELECT start,end, title, location FROM appointment a JOIN customer c ON a.customerId = c.customerID WHERE start BETWEEN CAST('"
                +dt1 +"' AS DATE) AND CAST('" + dt1.plusDays(range) +"' AS DATE)" ;
         conn = connector.getConnection();
        System.out.println("Connection retrieved");
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        System.out.println("Query finished");
        int i=0;
    while(rs.next()){
        Timestamp ts = rs.getTimestamp("start");
        
        LocalDateTime dts = ts.toLocalDateTime();
        
        System.out.println(dts);
        String title = rs.getString("title");
        //String name= rs.getString("customerName");
        //System.out.println( name);
        System.out.println(); 
           apptDates[i] = ts.toLocalDateTime();
           appointmentDays[i]=ts.toString();
           i++;
             
        }
    /* 
    for( i =0;i< appointmentDays.length;i++){
        System.out.println(appointmentDays[i]);
    }
    for( i=0;i<weekDays.length;i++){
        System.out.println(weekDays[i]);
    }*/
    for(int k=0; k < weekDays.length-1 ; k++){
      //  System.out.println(weekDays.length);
        System.out.println("Date:  " +weekDays[k]);
        for(int j =0; j<apptDates.length;j++){
       // System.out.println(apptDates.length);
       
        if((apptDates[j]!=null/*&&weekDays[k]!=null*/)&&(j<weekDays.length)&&(weekDays[k].getDayOfYear()==apptDates[j].getDayOfYear())){
        System.out.println("Appointment:   " +apptDates[j]);
        System.out.println();

        }
                
        }
    }
    try{
        conn.close();
    }
    catch(SQLException e){
        System.out.println("SQLException encountered");
    }
    }
}


