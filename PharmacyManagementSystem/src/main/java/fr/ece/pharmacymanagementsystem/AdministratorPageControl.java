package fr.ece.pharmacymanagementsystem;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

import static fr.ece.pharmacymanagementsystem.Users.user;


public class AdministratorPageControl implements Initializable {


    @FXML
    private ComboBox<String> comboBox;

    @FXML
    private TextField login_AdministratorID;

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


    @FXML
    void loginAccount(ActionEvent event) {

    }

    @FXML
    void loginShowPassword(ActionEvent event) {



    }

    @FXML
    void registerAccount(ActionEvent event) {

        if(register_email.getText().isEmpty() ||
                register_fullName.getText().isEmpty() ||
                register_password.getText().isEmpty()||
                register_showPassword.getText().isEmpty()) {
            alert.errorMessage("Please fill all the fields");

        }else {

            String checkAdminID= " SELECT * FROM administrator WHERE administrator_id = '" + "" +
                    register_AdminID.getText() + "'";
            connect = DataBase.connectDB();

            try{

                prepare = connect.prepareStatement(checkAdminID);
                result = prepare.executeQuery();

                if ( result.next() ) {

                    alert.errorMessage(register_AdminID.getText() + "is already taken");
                }else if( register_password.getText().length() < 8 ) {

                    alert.errorMessage(" Invalid Password, at least 8 characters needed for a valid password");

                }else {

                    String insrtData = "INSERT INTO administrator (full_name , email , administrator_id, password , date)"
                            + "VALUES (?,?,?,?)";

                    prepare =  connect.prepareStatement(insrtData);

                    Date date =  new Date();
                    java.sql.Date sqlDate = new java.sql.Date(date.getTime());

                    prepare.setString(1, register_fullName.getText());
                    prepare.setString(2, register_email.getText());
                    prepare.setString(3, register_password.getText());
                    prepare.setString(4,String.valueOf(sqlDate));

                    prepare.executeUpdate();

                    alert.successMessage("Successfully registered");


                }

            }catch (Exception e){e.printStackTrace();}
        }

    }

    @FXML
    void registerShowPassword(ActionEvent event) {

    }

    @FXML
    void resetPassword(ActionEvent event) {

    }

    @FXML
    void resetShowPassword(ActionEvent event) {

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

        }else if (event.getSource() == reset_form){
            login_form.setVisible(false);
            register_form.setVisible(false);
            reset_form.setVisible(true);
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

        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
