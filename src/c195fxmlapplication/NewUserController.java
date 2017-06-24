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
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author user
 */
public class NewUserController implements Initializable {
    @FXML
    AnchorPane newUserPane;
    @FXML 
    AnchorPane loginPane;
    @FXML
    Label usernameLabel;
    @FXML
    Label passwordLabel1;
    @FXML
    Label passwordLabel2;
    @FXML
    TextField usernameField;
    @FXML
    PasswordField passwordField1;
    @FXML
    PasswordField passwordField2;
    @FXML
    Button confirmButton;
    @FXML 
    Button loginButton;
    
    String username;
    String password;
    SQLConnector sqlConnect = new SQLConnector();
    Connection conn;
    /**
     * Initializes the controller class.
     */
    private String fetchUserName() {
        username = usernameField.getText();
        return username;
    }

    private boolean comparePasswords() {
        boolean match = false;
        String pw1 = passwordField1.getText();
        String pw2 = passwordField2.getText();
        if (pw1.equals(pw2)) {
            match = true;
        } else {
            match = false;
        }
        return match;
    }

    private String fetchPassword() {
        if (comparePasswords()) {
            password = passwordField1.getText();
        } else {
            System.out.println("Passwords do not match!");
        }
        return password;
    }
    private boolean duplicateUsernameCheck(){
        boolean duplicate = false;
        if (fetchPassword() != null && fetchUserName() != null) {
            String passwordSub = fetchPassword();
            String usernameSub = fetchUserName();
            try{
                sqlConnect.connectToDB();
                conn = sqlConnect.getConnection();
                Statement stmt = conn.createStatement();
                 ResultSet result = stmt.executeQuery("select username,password from user where username = '" + usernameSub +"'");
            while (result.next()) {
                String username = new String(result.getString("username"));
             
                if (username != null) {
                    duplicate=true;
                }
                else {
                duplicate = false;}

            }
            }
             catch (SQLException e)
             {
                 System.out.println("SQLException encountered");
             }
            catch (ClassNotFoundException cnfex)
            {
                System.out.println("ClassNotFoundException encountered");
            }
        }
        return duplicate;
    }
    public void fetchValues(ActionEvent ae) throws IOException{
        if (fetchPassword().toString().length()>8 && 
                fetchUserName().toString().length()>5) {
            String passwordSub = fetchPassword();
            String usernameSub = fetchUserName();
            try{
                sqlConnect.connectToDB();
                conn = sqlConnect.getConnection();
                Statement stmt = conn.createStatement();
                if (!duplicateUsernameCheck()){
                stmt.execute("insert into user (username,password,active,createBy,createDate,lastUpdatedBy) VALUES "
                        + "('"+usernameSub + "','" + passwordSub + "',1,'self-registration',NOW(),'NA')");
                System.out.println("Registration successful");
                Alert successAlert = new Alert(AlertType.CONFIRMATION);
                successAlert.setTitle("Success!");
                successAlert.setHeaderText("Success!");
                successAlert.setContentText("Registration successful!  You can log in now.");
                successAlert.showAndWait();
                Stage stage = (Stage) confirmButton.getScene().getWindow();
                stage.close();
               Parent loginPane = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
                Stage loginStage = new Stage();
                Scene scene = new Scene(loginPane);
                loginStage.setScene(scene);
                loginStage.show();
               
                
                }
                else{
                    
                    Alert usernameInUseAlert = new Alert(AlertType.INFORMATION);
                    usernameInUseAlert.setTitle("Username in Use");
                    usernameInUseAlert.setHeaderText("Try another username");
                    usernameInUseAlert.setContentText("That username is already in use.  Please try another.");
                    usernameInUseAlert.showAndWait();
                    System.out.println("There is already a user with that username.  Please choose another.");
                    
                }

            }
            catch (SQLException e)
             {
                 System.out.println("SQLException encountered");
             }
            catch (ClassNotFoundException cnfex)
            {
                System.out.println("ClassNotFoundException encountered");
            }
           
        } else {
            System.out.println("All fields required");
            Alert fieldsRequiredAlert = new Alert(AlertType.INFORMATION);
            fieldsRequiredAlert.setTitle("All fields required");
            fieldsRequiredAlert.setHeaderText("All fields required");
            fieldsRequiredAlert.setContentText("All fields all required!  Ensure that you choose username longer than 5 characters"
                    + "and a password longer than 8 characters");
            fieldsRequiredAlert.showAndWait();
        }
    }
    public void goToLoginPage(ActionEvent ae) throws Exception{
        Stage loginStage = new Stage();
        Parent loginPane = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        Stage currentStage = (Stage) loginButton.getScene().getWindow();
        currentStage.close();
        Scene scene = new Scene(loginPane);
        loginStage.setScene(scene);
        loginStage.show();
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
