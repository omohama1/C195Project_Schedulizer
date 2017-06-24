/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package c195fxmlapplication;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 *
 * @author user
 */
public class FXMLDocumentController implements Initializable {

    Locale location = Locale.getDefault();
    @FXML
    AnchorPane loginPane;
    @FXML
    Button enterButton;
    @FXML
    Label usernameLabel;
    @FXML
    Label passwordLabel;
    @FXML
    TextField usernameField;
    @FXML
    PasswordField passwordField;
    @FXML
    Button newUserButton;
    public static String username;
    UserLogger logger = new UserLogger();

   

    private void startNewUser() throws Exception {
        Stage stage = (Stage) newUserButton.getScene().getWindow();
        stage.close();
        Stage userStage = new Stage();
        Parent newUserPane = FXMLLoader.load(getClass().getResource("NewUser.fxml"));
        Scene scene2 = new Scene(newUserPane);
        userStage.setScene(scene2);
        userStage.show();

    }

    private void showMenu() throws Exception {
        Stage stage = (Stage) enterButton.getScene().getWindow();
        stage.close();
        Stage menuStage = new Stage();
        Parent menuPane = FXMLLoader.load(getClass().getResource("Menu.fxml"));
        Scene scene = new Scene(menuPane);
        menuStage.setScene(scene);
        menuStage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        //  loginPane.setBackground(new BackgroundFill(Color.AQUA,CornerRadii.EMPTY,Insets.Empty));
      
        if (location.toString().equals("ru_RU")) {
            usernameLabel.setText("Пользовтель");
            passwordLabel.setText("Пароль");
            enterButton.setText("Введите");
            newUserButton.setText("Новый пользователь? Нажмите суда!");
        }

    }

    public boolean confirmCredentials(ActionEvent ae) throws Exception {
        //username = usernameSub;
        //password = passwordSub;
        boolean success = false;
        String usernameSub = usernameField.getText();
        String passwordSub = passwordField.getText();
        SQLConnector sqlconn = new SQLConnector();
        try {
            if (usernameSub.equals("") || passwordSub.equals("")) {
                success = false;
                Runner runner = new Runner();
                if(location.toString().equals("en_US")){
                runner.run(() -> System.out.println("Incorrect"), "You must enter a username and password",
                        "Invalid credentials", "Invalid credentials");
                }
                else
                {
                 runner.run(() -> System.out.println("Неправильно"), "Вам дожно введить имя пользователя и пароль", "Ошибка",
                        "Или пароль или пользователь не введен");   
                }
            } else if (sqlconn.verifyCredentials(usernameSub, passwordSub)) {
                System.out.println("Correct");
                setUserName();
                loginPane.setVisible(false);
                showMenu();
                success = true;
                
            } else {
                success = false;
                Runner runner = new Runner();
                if(location.toString().equals("en_US")){
                runner.run(() -> System.out.println("Incorrect"), "Incorrect password or username", "Incorrect credentials", "Incorrect credentials");
                logger.writeToLog(username + " has logged in at " + LocalDateTime.now().toString());
                }
                else
                {
                    runner.run(() -> System.out.println("Неправильно"), "Вам дожно введить имя пользователя и пароль", "Ошибка",
                        "Или пароль или пользователь не введен"); 
                }
                }
        } catch (ClassNotFoundException e) {
            System.out.println("ClassNotFoundException");
        }

        return success;

    }

    private void setUserName() {
        username = usernameField.getText();

    }

    public String getUserName() {
        return username;
    }

    public void createNewUser(ActionEvent ae) throws Exception {
        startNewUser();

    }

}
