/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package c195fxmlapplication;

import java.time.LocalDateTime;
import java.util.Locale;

/**
 *
 * @author user
 */
public class TimeZoneTest {
    
    public static void main(String [] args){
    Locale currentLocation = Locale.getDefault();
    
    LocalDateTime dt1 =  LocalDateTime.now();
    Locale location3 = new Locale("fr","FR" );
    System.out.println(location3);
    }
}

