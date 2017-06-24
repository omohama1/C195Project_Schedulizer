/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package c195fxmlapplication;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author user
 */
public class NewCustomerController implements Initializable {
    
    @FXML
    AnchorPane customerPane;
    @FXML
    Button backToMenuButton;
    @FXML
    Button createCustomerButton;
    @FXML
    TextField custNameField;
    @FXML
    TextField addressField1;
    @FXML
    TextField addressField2;
    @FXML
    TextField cityField;
    @FXML
    TextField countryField;
    @FXML
    TextField postalCodeField;
    @FXML
    TextField custPhoneField;
    Connection conn;
    String country;
    String city;
    String phoneNumber;
    Runner runner = new Runner();
    UserLogger logger = new UserLogger();
    @FXML
    private Label custPhoneLabel;
    @FXML
    private Button updateButton;
    
    @FXML
    public void takeBackToMenu(ActionEvent ae) throws Exception {
        Stage currentStage = (Stage) backToMenuButton.getScene().getWindow();
        currentStage.close();
        Stage scheduleStage = new Stage();
        Parent schedulePane = FXMLLoader.load(getClass().getResource("Menu.fxml"));
        Scene scene = new Scene(schedulePane);
        scheduleStage.setScene(scene);
        scheduleStage.show();
    }
    
    private String fetchCustomerName() {
        
        String customerName = custNameField.getText();
        return customerName;
    }
    
    private String fetchAddressLine1() {
        String addressLine1 = addressField1.getText();
        return addressLine1;
    }
    
    private String fetchPhoneNumber() {
        String phoneNumber = custPhoneField.getText();
        return phoneNumber;
    }
    
    private String fetchAddressLine2() {
        String addressLine2 = addressField2.getText();
        return addressLine2;
    }
    
    private String fetchCity() {
        String city = cityField.getText();
        return city;
    }
    
    private String fetchCountry() {
        String country = countryField.getText();
        return country;
    }
    
    private String fetchPostalCode() {
        String postalCode = postalCodeField.getText();
        return postalCode;
    }
    
    public boolean checkDuplicateCustomer(String address1, String address2, String postalCode, String phoneNumber, int cityId) throws Exception {
        
        int addressEntered = getAddressID(address1, address2, postalCode, phoneNumber, cityId);
        String customerEntered = fetchCustomerName();
        
        boolean duplicate = false;
        Statement stmt = conn.createStatement();
        String duplicateQuery = "SELECT customerName, addressId FROM customer";
        ResultSet rsQuery = stmt.executeQuery(duplicateQuery);
        while (rsQuery.next()) {
            String name = rsQuery.getString("customerName");
            int addressID = rsQuery.getInt("addressId");
            System.out.println(name);
            System.out.println(addressID);
            
            if (addressEntered == addressID && customerEntered.equals(name)) {
                runner.run(() -> System.out.println(), "That customer already exists!", "Duplicate customer", "Duplicate customer");
                System.out.println("There is already a customer with that name at that address.");
                duplicate = true;
                //return duplicate;
            } else {
                System.out.println("Entering customer");
                duplicate = false;
                // return duplicate;
            }
        }
        //  conn.close();
        return duplicate;
    }
    
    public boolean checkDuplicateCity(String cityEntered) throws Exception {
        cityEntered = fetchCity();
        boolean duplicate = false;
        Statement stmt = conn.createStatement();
        String duplicateQuery = "SELECT * FROM city";
        ResultSet rsQuery = stmt.executeQuery(duplicateQuery);
        while (rsQuery.next()) {
            String city = rsQuery.getString("city");
            if (cityEntered.equals(city)) {
                System.out.println("That city is present in the database");
                duplicate = true;
                // return duplicate;
            } else {
                System.out.println("Entering city");
                duplicate = false;
            }
            
        }
        return duplicate;
        
    }
    
    public String getUser() {
        FXMLDocumentController doc = new FXMLDocumentController();
        String username = doc.getUserName();
        return username;
    }
    
    public boolean checkDuplicateCountry(String countryEntered) throws Exception {
        
        countryEntered = fetchCountry();
        boolean duplicate = false;
        Statement stmt = conn.createStatement();
        String duplicateQuery = "SELECT * FROM country";
        ResultSet rsQuery = stmt.executeQuery(duplicateQuery);
        while (rsQuery.next()) {
            String country = rsQuery.getString("country");
            
            if (countryEntered.equals(country)) {
                System.out.println("That country is present in the database.");
                duplicate = true;
                // return duplicate;
            } else {
                System.out.println("Inserting country");
                duplicate = false;
            }
            
        }
        return duplicate;
    }
    
    public void createAddress(String addressLine1, String addressLine2, String postalCode, String phoneNumber, String city) throws Exception {
        addressLine1 = fetchAddressLine1();
        addressLine2 = fetchAddressLine2();
        phoneNumber = fetchPhoneNumber();
        String user = getUser();
        Timestamp createDate = getCreateDate();
        postalCode = fetchPostalCode();
        int cityId = getCityId(city);
        String insertQuery = "INSERT INTO address (address, address2, cityId, postalCode, "
                + "phone, createDate, createdBy, lastUpdate,lastUpdateBy) VALUES ('" + addressLine1 + "','" + addressLine2 + "','"
                + cityId + "','" + postalCode + "','" + phoneNumber + "','" + getCreateDate()
                + "','" + getUser() + "','" + getCreateDate() + "','" + getUser() + "')";
        Statement addressStatement = conn.createStatement();
        addressStatement.executeUpdate(insertQuery);
        
    }/*
    else{
    }*/
    
    public int getAddressID(String addressLine1, String addressLine2, String postalCode, String phoneNumber, int cityId) throws Exception {
        int addressId = 0;
        String retrieveIDQuery = "SELECT addressID FROM address WHERE address='" + addressLine1 + "' AND address2= '"
                + addressLine2 + "' AND postalCode='" + postalCode + "' AND cityId = " + cityId;
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(retrieveIDQuery);
        while (rs.next()) {
            addressId = rs.getInt("addressID");
            return addressId;
        }
        return addressId;
    }
    
    public void createCity(String city) throws Exception {
        String country = fetchCountry();
        city = fetchCity();
        int countryId = getCountryId(country);
        String user = getUser();
        Timestamp createDate = getCreateDate();
        if (!checkDuplicateCity(city) && countryId != 0) {
            String createdBy = getUser();
            String insertQuery = "INSERT INTO city (city, countryId, createDate, createdBy, lastUpdate,lastUpdateBy) VALUES ('"
                    + city + "'," + countryId + ",'" + createDate + "','" + user + "','" + createDate + "','" + user + "')";
            Statement cityStatement = conn.createStatement();
            cityStatement.executeUpdate(insertQuery);
        } else if (countryId == 0) {
            createCountry(country);
        } else {
        }
        
    }
    
    public Timestamp getCreateDate() {
        LocalDateTime ldt1 = LocalDateTime.now();
        ZonedDateTime zdt1 = ZonedDateTime.of(ldt1, ZoneId.of("America/New_York"));
        Timestamp createDate = new Timestamp(zdt1.toInstant().getEpochSecond() * 1000L);
        return createDate;
    }
    
    public void createCountry(String country) throws Exception {
        // country = fetchCountry();
        if (!checkDuplicateCountry(country)) {
            String createdBy = getUser();
            String insertQuery = "INSERT INTO country (country, createDate, createdBy, lastUpdate,lastUpdateBy) VALUES ('"
                    + country + "','" + getCreateDate() + "','" + getUser() + "','" + getCreateDate() + "','" + getUser() + "')";
            Statement countryStatement = conn.createStatement();
            countryStatement.executeUpdate(insertQuery);
        } else {
        }
    }
    
    public int getCountryId(String country) throws Exception {
        country = fetchCountry();
        int countryId = 0;
        if (checkDuplicateCountry(country)) {
            String retrieveIDQuery = "SELECT countryId FROM country WHERE country='" + country + "'";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(retrieveIDQuery);
            while (rs.next()) {
                countryId = rs.getInt("countryId");
                if (countryId != 0) {
                    return countryId;
                } else {
                    System.out.println("Country must be created");
                }
            }
        }
        return countryId;
    }
    
    public int getCityId(String city) throws Exception {
        city = fetchCity();
        int cityId = 0;
        if (checkDuplicateCity(city)) {
            String retrieveIDQuery = "SELECT cityId FROM city WHERE city='" + city + "'";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(retrieveIDQuery);
            while (rs.next()) {
                
                cityId = rs.getInt("cityId");
                return cityId;
            }
        } else {
            //   createCity(city);
            // getCountryId(city);
        }
        return cityId;
        
    }
    
    @FXML
    public void createCustomer(ActionEvent ae) {
        try {
            SQLConnector connector = new SQLConnector();
            connector.connectToDB();
            conn = connector.getConnection();
            String customerName = fetchCustomerName();
            String addressLine1 = fetchAddressLine1();
            String addressLine2 = fetchAddressLine2();
            
            country = fetchCountry();
            int countryId = getCountryId(country);
            System.out.println("Getting countryId");
            System.out.println(countryId);
            
            city = fetchCity();
            int cityId = getCityId(city);
            System.out.println("Getting cityId");
            System.out.println(cityId);
            
            String postalCode = fetchPostalCode();
            String phoneNumber = fetchPhoneNumber();
            Timestamp createDate = getCreateDate();
            
            int addressID = getAddressID(addressLine1, addressLine2, postalCode, phoneNumber, cityId);
            
            Statement stmt = conn.createStatement();
            // String duplicateQuery = "SELECT * FROM customer";
            // ResultSet rsQuery =stmt.executeQuery(duplicateQuery);

            if (countryId == 0) {
                createCountry(country);
            }
            if (cityId == 0) {
                createCity(city);
            }
            if (addressID == 0) {
                createAddress(addressLine1, addressLine2, postalCode, phoneNumber, city);
                addressID = getAddressID(addressLine1, addressLine2, postalCode, phoneNumber, cityId);
            }
            
            if (checkDuplicateCustomer(addressLine1, addressLine2, postalCode, phoneNumber, cityId)) {
                System.out.println("Customer already present for that address");
            } else if (customerName.equals("") || customerName.equals(null)) {
                runner.run(() -> System.out.println(), "You must enter a customer name", "Missing customer info", "Missing information");
            } else if (addressLine1.equals(null) || addressLine1.equals("")) {
                runner.run(() -> System.out.println(), "You must enter an address line", "Missing customer info", "Missing information");
            } else if (postalCode.equals(null) || postalCode.equals("")) {
                runner.run(() -> System.out.println(), "You must enter a postal code", "Missing customer info", "Missing information");
            } else if (city.equals(null) || city.equals("")) {
                runner.run(() -> System.out.println(), "You must enter a city", "Missing customer info", "Missing information");
            } else if (country.equals(null) || country.equals("")) {
                runner.run(() -> System.out.println(), "You must enter a country", "Missing customer info", "Missing information");
            } else if (phoneNumber.equals(null) || phoneNumber.equals("")) {
                runner.run(() -> System.out.println(), "You must enter a phone number", "Missing customer info", "Missing information");
            } else if (addressID != 0 && cityId != 0 && countryId != 0) {
                
                System.out.println(customerName + " \n" + addressLine1 + " \n" + addressLine2
                        + " \n" + city + ", " + country + " " + postalCode + "\n");
                
                stmt.executeUpdate("INSERT INTO customer (customerName,addressId, active, createDate, createdBy,lastUpdate, lastUpdateBy)"
                        + " VALUES ('" + customerName + "','" + addressID + "'," + 1 + ",'" + createDate + "','" + getUser() + "','"
                        + createDate + "','" + getUser() + "')");
                runner.run(() -> System.out.println(), "Customer created!", "Success!", "Customer successfully created!");
                custNameField.setText("");
                addressField1.setText("");
                addressField2.setText("");
                cityField.setText("");
                countryField.setText("");
                custPhoneField.setText("");
                postalCodeField.setText("");
                logger.writeToLog("New customer created by user " + getUser());
                
            } else {
                System.out.println("Customer information incomplete");
            }
            
        } catch (Exception e) {
            System.out.println("Exception occurred");
        } finally {
            try {
                conn.close();
            } catch (SQLException sqle) {
                System.out.println("SQLException caught while trying to close connection");
                sqle.printStackTrace();
            }
            
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

    }
    
}
