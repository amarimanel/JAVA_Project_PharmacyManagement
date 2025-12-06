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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.*;


public class ClientPageControl implements Initializable {


    @FXML
    private CheckBox login_checkBox;

    @FXML
    private Hyperlink login_forgotPassword;

    @FXML
    private AnchorPane login_form;

    @FXML
    private TextField login_fullName;

    @FXML
    private Button login_loginBtn;

    @FXML
    private PasswordField login_password;

    @FXML
    private Hyperlink login_registerHere;

    @FXML
    private TextField login_showPassword;


    @FXML
    private TextField login_securityNumber;

    @FXML
    private ComboBox<?> login_user;

    @FXML
    private AnchorPane main_form;

    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;

    private AlertMessage alert = new AlertMessage();


    @FXML
    void loginAccount(ActionEvent event) {
        if(login_fullName.getText().isEmpty() ||
                login_password.getText().isEmpty() ||
        login_securityNumber.getText().isEmpty()){
            alert.errorMessage("Please fill all the fields");
        }else {

            String sql = "SELECT * FROM client WHERE security_number = ? AND password = ?";
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

                String checkStatus = "SELECT * FROM client WHERE  = '"
                        + login_securityNumber.getText() + "' AND password = '"
                        + login_password.getText() + "' AND status = 'Confirm'";

                prepare = connect.prepareStatement(checkStatus);
                result = prepare.executeQuery();

                if(result.next()){
                    if (!login_showPassword.isVisible()){

                        if (!login_password.getText().equals(login_showPassword.getText())){

                            login_showPassword.setText(login_password.getText());
                        }

                    }else{

                        if (!login_showPassword.getText().equals(login_password.getText())){

                            login_password.setText(login_showPassword.getText());
                        }

                    }
                    alert.errorMessage("Need Confirmation of Admin");
                }  else{
                    prepare = connect.prepareStatement(sql);
                    prepare.setString(1, login_fullName.getText());
                    prepare.setString(2, login_securityNumber.getText());
                    result = prepare.executeQuery();

                    if (result.next()){
                        // if correct username and password

                        alert.successMessage("Login Successful");
                    }else{
                        alert.errorMessage("Incorrect Name or Password or Security Number");
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @FXML
    void loginShowPassword(ActionEvent event) {

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
    void switchForm(ActionEvent event) {

    }

    @FXML
    void switchPage(ActionEvent event) {

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
        userList();
    }
}
