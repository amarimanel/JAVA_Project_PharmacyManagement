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
import javafx.stage.StageStyle;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static fr.ece.pharmacymanagementsystem.Users.user;


public class AdministratorPageControl implements Initializable {


    @FXML
    private ComboBox<String> comboBox;

    @FXML
    private TextField login_administratorID;

    @FXML
    private CheckBox login_checkBox;

    @FXML
    private Hyperlink login_forgotPassword;

    @FXML
    private AnchorPane login_form;

    @FXML
    private Button login_loginBtn;

    @FXML
    private PasswordField login_password;

    @FXML
    private Hyperlink login_registerHere;

    @FXML
    private TextField login_showPassword;

    @FXML
    private ComboBox<?> login_user;

    @FXML
    private AnchorPane main_form;

    @FXML
    private TextField register_AdminID;

    @FXML
    private CheckBox register_checkBox;

    @FXML
    private TextField register_email;

    @FXML
    private AnchorPane register_form;

    @FXML
    private TextField register_fullName;

    @FXML
    private Hyperlink register_loginHere;

    @FXML
    private PasswordField register_password;

    @FXML
    private TextField register_showPassword;

    @FXML
    private Button register_signupBtn;

    @FXML
    private TextField reset_adminID;

    @FXML
    private CheckBox reset_checkBox;

    @FXML
    private PasswordField reset_checkPassword;

    @FXML
    private AnchorPane reset_form;

    @FXML
    private TextField reset_fullName;

    @FXML
    private Hyperlink reset_loginHere;

    @FXML
    private PasswordField reset_password;

    @FXML
    private Button reset_resetBtn;

    @FXML
    private TextField reset_resetCheckPassword;

    @FXML
    private TextField reset_resetShowPassword;


    // pour la base de donnée
    private Connection connect ;
    private PreparedStatement prepare ;
    private ResultSet result ;

    private final AlertMessage alert = new AlertMessage();

    private int x = 0;
    private int y = 0;
    @FXML
    void loginAccount() {


        if(login_administratorID.getText().isEmpty() ||
        login_password.getText().isEmpty()){
            alert.errorMessage("Please fill all the fields");
        }else {

            String sql = "select * from administrator where administrator_id = ? and password = ? ";
            connect = DataBase.connectDB();

            try{
                if (!login_showPassword.isVisible()){

                    if (!login_password.getText().equals(login_showPassword.getText())){

                        login_showPassword.setText(login_password.getText());
                    }

                }else{

                    if (!login_showPassword.getText().equals(login_password.getText())){

                        login_password.setText(login_showPassword.getText());
                    }

                }

                prepare = connect.prepareStatement(sql);
                prepare.setString(1, login_administratorID.getText());
                prepare.setString(2, login_password.getText());
                result = prepare.executeQuery();

                if (result.next()){
                    getData.admin_username = login_administratorID.getText();

                    // if correct username and password
                    Parent root = FXMLLoader.load(getClass().getResource("MainForm.fxml"));
                    Stage stage = new Stage();

                    stage.setTitle("Pharmacy Management System");

                    stage.setMinHeight(500);
                    stage.setMinWidth(350);

                    stage.setScene( new Scene(root));
                    stage.show();

                    login_user.getScene().getWindow().hide();
                    alert.successMessage("Login Successful");

                }else{
                    alert.errorMessage("Incorrect Admin ID or Password");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }



    @FXML
    void loginShowPassword() {
        if (login_checkBox.isSelected()) {
            login_showPassword.setText(login_password.getText());
            login_showPassword.setVisible(true);
            login_password.setVisible(false);

        }else {
            login_password.setText(login_showPassword.getText());
            login_showPassword.setVisible(false);
            login_password.setVisible(true);
        }


    }

    @FXML
    void registerAccount() {

        if(register_email.getText().isEmpty() ||
                register_fullName.getText().isEmpty() ||
                register_password.getText().isEmpty() ||
                register_AdminID.getText().isEmpty()
        ) {
            alert.errorMessage("Please fill all the fields");

        }else {

            String checkAdminID= " SELECT * FROM administrator WHERE administrator_id = '" + "" +
                    register_AdminID.getText() + "'";
            connect = DataBase.connectDB();

            try{

                if(!register_showPassword.isVisible()){
                    if(register_showPassword.getText().equals(register_password.getText())){

                        register_showPassword.setText(register_password.getText());
                    }
                }else{

                    if (!register_showPassword.getText().equals(register_password.getText())) {

                        register_password.setText(register_showPassword.getText());

                    }
                }


                prepare = connect.prepareStatement(checkAdminID);
                result = prepare.executeQuery();

                if ( result.next() ) {

                    alert.errorMessage(register_AdminID.getText() + "is already taken");
                }else if( register_password.getText().length() < 8 ) {

                    alert.errorMessage(" Invalid Password, at least 8 characters needed for a valid password");

                }else {

                    String insrtData = "INSERT INTO administrator (full_name , email , administrator_id, password , date)"
                            + "VALUES (?,?,?,?,?)";

                    prepare =  connect.prepareStatement(insrtData);

                    Date date =  new Date();
                    java.sql.Date sqlDate = new java.sql.Date(date.getTime());

                    prepare.setString(1, register_fullName.getText());
                    prepare.setString(2, register_email.getText());
                    prepare.setString(3, register_AdminID.getText());
                    prepare.setString(4, register_password.getText());
                    prepare.setString(5,String.valueOf(sqlDate));

                    prepare.executeUpdate();


                    alert.successMessage("Successfully registered");


                }

            }catch (Exception e){e.printStackTrace();}
        }

    }

    @FXML
    void registerShowPassword() {

        if ( register_checkBox.isSelected() ) {
            register_showPassword.setText(register_password.getText());
            register_showPassword.setVisible(true);
            register_password.setVisible(false);

        }else {
            register_password.setText(register_showPassword.getText());
            register_showPassword.setVisible(false);
            register_password.setVisible(true);
        }
    }

    @FXML
    void resetPassword(ActionEvent event) {

        String fullName = reset_fullName.getText().trim();
        String newPassword = reset_password.getText();
        String confirmPassword = reset_checkPassword.getText();


        if(fullName.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            alert.errorMessage("Please fill all the fields");
            return;
        }

        if(!newPassword.equals(confirmPassword)) {
            alert.errorMessage("Passwords do not match");
            return;
        }


        if (newPassword.length() < 8) {
            alert.errorMessage("Invalid Password, at least 8 characters needed");
            return;
        }

        // 3. Attention aux noms des colonnes (full_name vs fullName)
        String checkFullName = "SELECT * FROM administrator WHERE full_name = ?";
        String updatePassword = "UPDATE administrator SET password = ? WHERE full_name = ?";

        connect = DataBase.connectDB();

        try {
            // Vérifier si le nom existe
            prepare = connect.prepareStatement(checkFullName);
            prepare.setString(1, fullName);
            result = prepare.executeQuery();

            if(result.next()) {

                prepare = connect.prepareStatement(updatePassword);
                prepare.setString(1, newPassword);
                prepare.setString(2, fullName);

                int rowsAffected = prepare.executeUpdate();

                if(rowsAffected > 0) {
                    alert.successMessage("Password updated successfully");

                } else {
                    alert.errorMessage("Error: Password not updated");
                }
            } else {
                alert.errorMessage("Name not found in database for: '" + fullName + "'");
            }

        } catch(Exception e) {
            e.printStackTrace();
            alert.errorMessage("An error occurred: " + e.getMessage());
        }
    }

    @FXML
    void resetShowPassword(ActionEvent event) {

        if(reset_checkBox.isSelected()){
            reset_resetShowPassword.setText(reset_password.getText());
            reset_resetShowPassword.setVisible(true);
            reset_password.setVisible(false);

        }else {
            reset_password.setText(reset_resetShowPassword.getText());
            reset_resetShowPassword.setVisible(false);
            reset_password.setVisible(true);
        }
    }

    public void registerAdminID(){
        int tempID = 0;
        String adminID = "AID-";
        String sql = "SELECT MAX(id) FROM administrator";

        connect = DataBase.connectDB();

        try {

            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            if(result.next()) {
                tempID = result.getInt("MAX(id)");
            }

            if ( tempID == 0 ) {
                tempID += 1;
                adminID += tempID;
            }else {
                adminID += (tempID+1);
            }
            register_AdminID.setText(adminID);
            register_AdminID.setDisable(true);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }



    @FXML
    void switchForm(ActionEvent event) {

        if(event.getSource()== register_loginHere) {
            login_form.setVisible(true);
            register_form.setVisible(false);
            reset_form.setVisible(false);
        }else if(event.getSource()== login_registerHere) {
            login_form.setVisible(false);
            register_form.setVisible(true);
            reset_form.setVisible(false);

        }else if (event.getSource() == login_forgotPassword){
            login_form.setVisible(false);
            register_form.setVisible(false);
            reset_form.setVisible(true);
        }else if (event.getSource() == reset_loginHere){
        login_form.setVisible(true);
        register_form.setVisible(false);
        reset_form.setVisible(false);
    }

    }
    public void switchPage(){

        if(login_user.getSelectionModel().getSelectedItem() == "Admin Portal"){

            try{
                Parent root = FXMLLoader.load(getClass().getResource("AdministratorPage.fxml"));
                Stage stage = new Stage();

                stage.setTitle("Pharmacy Management System");

                stage.setMinHeight(500);
                stage.setMinWidth(350);

                stage.setScene( new Scene(root));
                stage.show();

                login_user.getScene().getWindow().hide();

            }catch(Exception e){e.printStackTrace();}

        }else if (login_user.getSelectionModel().getSelectedItem() == "Employee portal"){
            try{
                Parent root = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
                Stage stage = new Stage();

                stage.setTitle("Pharmacy Management System");

                stage.setMinHeight(500);
                stage.setMinWidth(350);

                stage.setScene( new Scene(root));
                stage.show();
                login_user.getScene().getWindow().hide();

            }catch(Exception e){e.printStackTrace();}

        }else if (login_user.getSelectionModel().getSelectedItem() == "Client portal"){
            try{
                Parent root = FXMLLoader.load(getClass().getResource("ClientPage.fxml"));
                Stage stage = new Stage();

                stage.setTitle("Pharmacy Management System");

                stage.setMinHeight(500);
                stage.setMinWidth(350);

                stage.setScene( new Scene(root));
                stage.show();
                login_user.getScene().getWindow().hide();

            }catch(Exception e){e.printStackTrace();}
        }


    }

    @FXML
    public void userList(){

        List<String> listU = new ArrayList<>();

        for (String data : Users.user) {
            listU.add(data);


        }

        ObservableList listData = FXCollections.observableList(listU);
        login_user.setItems(listData);

    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        registerAdminID();
        userList();
    }
}
