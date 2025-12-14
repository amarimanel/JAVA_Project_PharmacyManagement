package fr.ece.pharmacymanagementsystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ClientPageControl implements Initializable {

    @FXML private CheckBox login_checkBox;
    @FXML private Hyperlink login_forgotPassword;
    @FXML private AnchorPane login_form;

    // We keep this variable for FXML linkage, but we won't use it for login validation
    @FXML private TextField login_fullName;

    @FXML private Button login_loginBtn;
    @FXML private PasswordField login_password;
    @FXML private Hyperlink login_registerHere;
    @FXML private TextField login_showPassword;
    @FXML private TextField login_securityNumber;
    @FXML private ComboBox<String> login_user; // Changed to <String> for safety
    @FXML private AnchorPane main_form;

    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;

    // Ensure AlertMessage class exists in your package, otherwise use standard Alert
    private AlertMessage alert = new AlertMessage();

    /*@FXML
    void loginAccount(ActionEvent event) {
        // 1. Check ONLY Security Number and Password
        if (login_securityNumber.getText().isEmpty() || login_password.getText().isEmpty()) {
            alert.errorMessage("Please fill all the fields");
        } else {
            // 2. Query to check credentials
            String sql = "SELECT * FROM client WHERE security_number = ? AND password = ?";
            connect = DataBase.connectDB();

            try {
                // Handle Password Visibility Toggling Logic (Sync text fields)
                if (login_showPassword.isVisible()) {
                    login_password.setText(login_showPassword.getText());
                } else {
                    login_showPassword.setText(login_password.getText());
                }

                prepare = connect.prepareStatement(sql);
                prepare.setString(1, login_securityNumber.getText());
                prepare.setString(2, login_password.getText());
                result = prepare.executeQuery();

                if (result.next()) {

                    String status = result.getString("status");

                    if ("Confirm".equalsIgnoreCase(status)) {
                        getData.client_id = login_securityNumber.getText();
                        alert.successMessage("Login Successful");


                        login_loginBtn.getScene().getWindow().hide();
                        Parent root = FXMLLoader.load(getClass().getResource("ClientForm.fxml"));
                        Stage stage = new Stage();
                        stage.setTitle("Pharmacy Management System");
                        stage.setScene(new Scene(root));
                        stage.show();

                    } else {

                        alert.errorMessage("Login Failed: Please wait for Admin confirmation.");
                    }
                } else {
                    // Credentials Wrong
                    alert.errorMessage("Incorrect Security Number or Password");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }*/

    //with the hash

    @FXML
    void loginAccount(ActionEvent event) {

        String passwordInput;
        if (login_showPassword.isVisible()) {
            passwordInput = login_showPassword.getText();
        } else {
            passwordInput = login_password.getText();
        }

        // 2. Validate Empty Fields
        if (login_securityNumber.getText().isEmpty() || passwordInput.isEmpty()) {
            alert.errorMessage("Please fill all the fields");
            return;
        }

        String sql = "SELECT * FROM client WHERE security_number = ?";

        connect = DataBase.connectDB();

        try {
            prepare = connect.prepareStatement(sql);
            prepare.setString(1, login_securityNumber.getText());
            result = prepare.executeQuery();

            if (result.next()) {
                // User Exists. Now we get the Hashed Password from the DB
                String storedHash = result.getString("password");
                String status = result.getString("status");

                if (PasswordUtils.verifyPassword(passwordInput, storedHash)) {



                    if ("Confirm".equalsIgnoreCase(status)) {
                        getData.client_id = login_securityNumber.getText();
                        alert.successMessage("Login Successful");
                        login_loginBtn.getScene().getWindow().hide();

                        // Load Client Dashboard
                        Parent root = FXMLLoader.load(getClass().getResource("ClientForm.fxml"));
                        Stage stage = new Stage();
                        stage.setTitle("Pharmacy Management System");
                        stage.setScene(new Scene(root));
                        stage.show();

                    } else {
                        alert.errorMessage("Login Failed: Please wait for Admin confirmation.");
                    }

                } else {
                    alert.errorMessage("Incorrect Password");
                }

            } else {
                alert.errorMessage("Incorrect Security Number");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    @FXML
    void loginShowPassword(ActionEvent event) {
        if (login_checkBox.isSelected()) {
            login_showPassword.setText(login_password.getText());
            login_showPassword.setVisible(true);
            login_password.setVisible(false);
        } else {
            login_password.setText(login_showPassword.getText());
            login_showPassword.setVisible(false);
            login_password.setVisible(true);
        }
    }

    @FXML
    void switchPage(ActionEvent event) {

        String selected = login_user.getSelectionModel().getSelectedItem();

        if (selected == null) return;

        try {
            if (selected.equals("Admin Portal")) {
                loadPage("AdministratorPage.fxml");
            } else if (selected.equals("Employee portal")) {
                loadPage("hello-view.fxml"); // Or EmployeePage.fxml
            } else if (selected.equals("Client portal")) {
                // Already here, or reload
                loadPage("ClientPage.fxml");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private void loadPage(String fxmlFile) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
        Stage stage = new Stage();
        stage.setTitle("Pharmacy Management System");
        stage.setScene(new Scene(root));
        stage.show();
        login_user.getScene().getWindow().hide();
    }

    public void userList() {
        List<String> listU = new ArrayList<>();
        for (String data : Users.user) {
            listU.add(data);
        }
        ObservableList<String> listData = FXCollections.observableArrayList(listU);
        login_user.setItems(listData);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userList();
    }
}