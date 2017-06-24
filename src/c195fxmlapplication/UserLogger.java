/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package c195fxmlapplication;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author user
 */
public class UserLogger {

    File LOG_FILE = new File("userlog.txt");
    static String log;
    FileWriter fw = null;
    BufferedWriter bw = null;
    Runner runner = new Runner();

    public UserLogger() {

    }

    public void writeToLog(String content) throws IOException {

        String user = MenuController.fetchUser();
        log += "\n Starting log for user: " + user;
        try {
            log += content + "\n";
            fw = new FileWriter(LOG_FILE, true);
            bw = new BufferedWriter(fw);
            bw.newLine();
            bw.write(log);
            bw.newLine();
            bw.flush();
            System.out.println("Log file started");
        }
        catch(FileNotFoundException fnfe)
        {
            runner.runError(()->System.out.println("FileNotFoundException encountered"), "That file location was not found!", "Error", "Error");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally
        {
            bw.close();
        }

    }

}
