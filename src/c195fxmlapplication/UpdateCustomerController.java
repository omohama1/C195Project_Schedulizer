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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author user
 */
public class UpdateCustomerController implements Initializable {

    @FXML
    AnchorPane customerPane;
    @FXML
    Button backToMenuButton;
    @FXML
    Button createCustomerButton;
    @FXML
    ComboBox custNameField;
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
    SQLConnector connector = new SQLConnector();
    Runner runner = new Runner();
    UserLogger logger = new UserLogger();
    @FXML
    private Label custPhoneLabel;
    @FXML
    private Button updateButton;
    @FXML
    ComboBox activeBox;
    @FXML
    Label activeLabel;
    @FXML
    TextField custNameText;
    @FXML
    Button confirmButton;

    /*
    takeBackToMenu takes the user back to the main menu
    Throws IOException
    @param ActionEvent (mouse click)
    @return void
     */
    public void takeBackToMenu(ActionEvent ae) throws IOException {
        Stage currentStage = (Stage) backToMenuButton.getScene().getWindow();
        currentStage.close();
        Stage scheduleStage = new Stage();
        Parent schedulePane = FXMLLoader.load(getClass().getResource("Menu.fxml"));
        Scene scene = new Scene(schedulePane);
        scheduleStage.setScene(scene);
        scheduleStage.show();
    }

    /*
    populateCustomerField populates the custNameField ComboBox with
    customerName from table customer 
    Throws ClassNotFoundException
    @param void
    @return void
     */
    private void populateCustomerField() throws ClassNotFoundException {
        connector.connectToDB();
        conn = connector.getConnection();
        String query
                = "SELECT customerID, customerName FROM customer c "
                + "JOIN address a ON c.addressId= a.addressID JOIN city ci ON ci.cityID = a.cityId "
                + "JOIN country co ON co.countryId = ci.countryId";
        String cName = "";
        int custID = 0;
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                cName = rs.getString("customerName");
                custID = rs.getInt("customerID");
                String apptStr = "Customer: " + cName + " CustomerID: " + custID;
                custNameField.getItems().add(apptStr);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
    populateActiveField populates the activeBox ComboBox with
    Two options: "Yes" or "No" 
    Throws ClassNotFoundException
    @param void
    @return void
     */
    private void populateActiveField() throws ClassNotFoundException {
        activeBox.getItems().add("Yes");
        activeBox.getItems().add("No");

    }

    /*
    Getter method getActiveField() 
    Returns an int - either 1 or 0 - based on whether the user selects "Yes" or "No"
    Returns 1 if "Yes" or 0 if "No"
    Determines whether customer is still active or not
    @param void
    @return int active
     */
    private int getActiveField() {
        int active = -1;
        String activeStr = activeBox.getSelectionModel().getSelectedItem().toString();
        if (activeStr.equals("Yes")) {
            active = 1;
        } else {
            active = 0;
        }
        return active;
    }

    /*
    Getter method fetchCustomerID() 
    Returns an int - custID - the customerID according to the value fetched from the custNameField (which was derived from the customer table)
    @param void
    @return int custID
     */
    private int fetchCustomerID() {
        String name = "";
        int custID = 0;
        String customerName = custNameField.getSelectionModel().getSelectedItem().toString();
        String[] splitCustomer = customerName.split(" ");
        System.out.println("Length of customer array: " + splitCustomer.length);
        if (splitCustomer.length < 5) {
            name = splitCustomer[1];
            custID = Integer.parseInt(splitCustomer[3]);
        } else {
            name = splitCustomer[1] + " " + splitCustomer[2];
            custID = Integer.parseInt(splitCustomer[4]);

        }
        System.out.println("Customer selected is " + name + " Customer ID " + custID);
        return custID;
    }

    private String fetchCustomerName() {
        String name = custNameText.getText();
        return name;
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

    public boolean checkDuplicateCity(String cityEntered) throws Exception {
        cityEntered = fetchCity();
        boolean duplicate = false;
        connector.connectToDB();
        conn = connector.getConnection();
        Statement stmt = conn.createStatement();
        String duplicateQuery = "SELECT * FROM city WHERE city ='" + cityEntered + "'";
        ResultSet rsQuery = stmt.executeQuery(duplicateQuery);
        while (rsQuery.next()) {
            String city = rsQuery.getString("city");
            if (cityEntered.equals(city)) {
              //  System.out.println("That city is present in the database");
                duplicate = true;
                // return duplicate;
            } else {
              //  System.out.println("Entering city");
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
        connector.connectToDB();
        conn = connector.getConnection();
        Statement stmt = conn.createStatement();
        String duplicateQuery = "SELECT * FROM country WHERE country ='" +countryEntered +"'";
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

    public int confirmCustomer(ActionEvent ae) throws SQLException {
        addressField1.setText("");
        addressField2.setText("");
        cityField.setText("");
        countryField.setText("");
        postalCodeField.setText("");
        custPhoneField.setText("");
        custNameText.setText("");
        int custID = fetchCustomerID();
        try {
            connector.connectToDB();
            conn = connector.getConnection();

            String query = "SELECT address,address2, city, country, customerName, postalCode, phone,active FROM customer c "
                    + "JOIN address a ON c.addressId= a.addressID JOIN city ci ON ci.cityID = a.cityId "
                    + "JOIN country co ON co.countryId = ci.countryId WHERE c.customerID = " + custID;

            conn = connector.getConnection();

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                addressField1.setText(rs.getString("address"));
                addressField2.setText(rs.getString("address2"));
                cityField.setText(rs.getString("city"));
                countryField.setText(rs.getString("country"));
                postalCodeField.setText(rs.getString("postalCode"));
                custPhoneField.setText(rs.getString("phone"));
                custNameText.setText(rs.getString("customerName"));
                int active = rs.getInt("active");
                if (active == 0) {
                    activeBox.setValue("No");
                } else {
                    activeBox.setValue("Yes");
                }

            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();

        }

        return custID;
    }

    public void createAddress(String addressLine1, String addressLine2, String postalCode, String phoneNumber, String city) throws Exception {
        addressLine1 = fetchAddressLine1();
        addressLine2 = fetchAddressLine2();
        phoneNumber = fetchPhoneNumber();
        String user = getUser();
        Timestamp createDate = getCreateDate();
        postalCode = fetchPostalCode();
        int cityId = getCityId(city);
        if (postalCode.equals(null) || postalCode.equals("")) {
            runner.run(() -> System.out.println(), "You must enter a postal code or the code is too long", "Missing customer info", "Missing information");
        } else if (postalCode.length() > 10) {
            runner.run(() -> System.out.println(), "That postal code is too long or improperly formatted", "Missing customer info", "Missing information");
        } else {
            String insertQuery = "INSERT INTO address (address, address2, cityId, postalCode, "
                    + "phone, createDate, createdBy, lastUpdate,lastUpdateBy) VALUES ('" + addressLine1 + "','" + addressLine2 + "','"
                    + cityId + "','" + postalCode + "','" + phoneNumber + "','" + getCreateDate()
                    + "','" + getUser() + "','" + getCreateDate() + "','" + getUser() + "')";
            Statement addressStatement = conn.createStatement();
            addressStatement.executeUpdate(insertQuery);
        }

    }

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

    public void updateCustomer(ActionEvent ae) {
        try {
            connector.connectToDB();
            conn = connector.getConnection();
            int customerID = fetchCustomerID();
            String custName = fetchCustomerName();
            String addressLine1 = fetchAddressLine1();
            String addressLine2 = fetchAddressLine2();
            int active = getActiveField();
            country = fetchCountry();
            int countryId = getCountryId(country);
            System.out.println("Getting countryId");
            System.out.println(countryId);
            city = fetchCity();
            int cityId = getCityId(city);
            System.out.println("Getting cityId");
            System.out.println(cityId);
            String query = "";
            String postalCode = fetchPostalCode();
            String phoneNumber = fetchPhoneNumber();
            Timestamp createDate = getCreateDate();

            int addressID = getAddressID(addressLine1, addressLine2, postalCode, phoneNumber, cityId);

            Statement stmt = conn.createStatement();
            // String duplicateQuery = "SELECT * FROM customer";
            // ResultSet rsQuery =stmt.executeQuery(duplicateQuery);

            if (countryId == 0) {
                createCountry(country);
            }  if (cityId == 0) {
                createCity(city);
            }  if (addressID == 0) {
                createAddress(addressLine1, addressLine2, postalCode, phoneNumber, city);
                addressID = getAddressID(addressLine1, addressLine2, postalCode, phoneNumber, cityId);
            }  if (customerID == 0) {
                runner.run(() -> System.out.println(), "You must select a customer", "Missing customer info", "Missing information");
            }  if (addressLine1.equals(null) || addressLine1.equals("")) {

                runner.run(() -> System.out.println(), "You must enter an address line", "Missing customer info", "Missing information");
            } else if (active < 0) {

                runner.run(() -> System.out.println(), "Please select whether or not the customer is active", "Missing customer info", "Missing information");
            } else if (city.equals(null) || city.equals("")) {
                runner.run(() -> System.out.println(), "You must enter a city", "Missing customer info", "Missing information");
            } else if (country.equals(null) || country.equals("")) {
                runner.run(() -> System.out.println(), "You must enter a country", "Missing customer info", "Missing information");
            } else if (phoneNumber.equals(null) || phoneNumber.equals("")) {
                runner.run(() -> System.out.println(), "You must enter a phone number", "Missing customer info", "Missing information");
            } else /*(addressID != 0 && cityId != 0 && countryId != 0)*/ {

                // System.out.println(customerName + " \n" + addressLine1 + " \n" + addressLine2
                //       + " \n" + city + ", " + country + " " + postalCode + "\n");
                stmt.executeUpdate("UPDATE customer SET customerName = '" + custName + "', addressID =" + addressID + ", active=" + active + ", lastUpdate='" + createDate
                        + "', lastUpdateBy='" + getUser() + "' WHERE customerID =" + customerID);
                runner.run(() -> System.out.println(), "Customer updated!", "Success!", "Customer successfully updated!");
                custNameField.setValue(null);
                custNameText.setText("");
                addressField1.setText("");
                addressField2.setText("");
                cityField.setText("");
                countryField.setText("");
                custPhoneField.setText("");
                postalCodeField.setText("");
                logger.writeToLog("New customer created by user " + getUser());

            }

        } catch (Exception e) {
            System.out.println("Exception occurred");
            e.printStackTrace();
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
        try {
            populateCustomerField();
            populateActiveField();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

}
