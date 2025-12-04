package fr.ece.pharmacymanagementsystem;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.util.ResourceBundle;

public class HelloController {

    @FXML
    private Label welcomeText;

    // Here to write All components in AdminPage
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
    private TextField login_username;

    @FXML
    private AnchorPane main_form;

    @FXML
    private CheckBox register_checkBox;

    @FXML
    private TextField register_email;

    @FXML
    private AnchorPane register_form;

    @FXML
    private AnchorPane register_form1;

    @FXML
    private PasswordField register_password;

    @FXML
    private TextField register_showPassword;

    @FXML
    private TextField register_username;

    @FXML
    private CheckBox reset_checkBox;

    @FXML
    private PasswordField reset_checkPassword;

    @FXML
    private TextField reset_email;

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

    @FXML
    void register_logInHere(ActionEvent event) {

    }
    @FXML
    private Hyperlink register_loginHere;

    @FXML
    void register_signUpBtn(ActionEvent event) {

    }

    public void switchForm(ActionEvent event) {

        if (event.getSource() == login_registerHere) {
            // here the registration form will show
            login_form.setVisible(false);
            register_form.setVisible(true);

        }else if (event.getSource() == register_loginHere) {
            //log in will show
            login_form.setVisible(true);
            register_form.setVisible(false);
        }


    }

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}
