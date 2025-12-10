package fr.ece.pharmacymanagementsystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class HelloController implements Initializable {


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
    private AnchorPane reset_form;

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
    private Button register_signupBtn;

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
    // pour les database roots
    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;

    private final AlertMessage alert = new  AlertMessage();

    private double x = 0;
    private double y = 0;

    public void loginAccount() {

        if (login_username.getText().isEmpty() || login_password.getText().isEmpty()) {
            alert.errorMessage("Incorrect Username or Password");
        } else {

            String sql = "SELECT * FROM admin WHERE username = ? AND password = ?";
            connect = DataBase.connectDB();

            try {
                // Logic to sync password field and show password field
                if (!login_showPassword.isVisible()) {
                    if (!login_password.getText().equals(login_showPassword.getText())) {
                        login_showPassword.setText(login_password.getText());
                    }
                } else {
                    if (!login_showPassword.getText().equals(login_password.getText())) {
                        login_password.setText(login_showPassword.getText());
                    }
                }

                prepare = connect.prepareStatement(sql);
                prepare.setString(1, login_username.getText());
                prepare.setString(2, login_password.getText());
                result = prepare.executeQuery();

                if (result.next()) {
                    // --- LOGIN SUCCESSFUL LOGIC STARTS HERE ---

                    // 1. Save the username to the Session (Global variable)
                    // This allows the Employee Dashboard to know who logged in
                    getData.admin_username = login_username.getText();

                    alert.successMessage("Login Successful");

                    // 2. Hide the Login Window
                    // Note: Ensure 'login_username' or your login button is linked in FXML
                    login_username.getScene().getWindow().hide();

                    // 3. Load the Employee Dashboard
                    Parent root = FXMLLoader.load(getClass().getResource("EmployeeForm.fxml"));
                    Stage stage = new Stage();
                    Scene scene = new Scene(root);

                    // Optional: Dragging logic (if you want the window to be draggable)
                    root.setOnMousePressed((event) -> {
                        x = event.getSceneX();
                        y = event.getSceneY();
                    });
                    root.setOnMouseDragged((event) -> {
                        stage.setX(event.getScreenX() - x);
                        stage.setY(event.getScreenY() - y);
                    });

                    stage.initStyle(StageStyle.TRANSPARENT);
                    stage.setScene(scene);
                    stage.show();

                } else {
                    alert.errorMessage("Incorrect Username or Password");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void registerAccount(){

        if(register_email.getText().isEmpty()||register_username.getText().isEmpty()||register_password.getText().isEmpty()){

            // we create an alert message
            alert.errorMessage("please fill all the fields");

        }else {
            // pour voir si le username deja entrée existe deja dans notre base de donnée
            String checkUsername = "Select * FROM admin Where username = ' " + register_username.getText() + "'";

            connect = DataBase.connectDB();

            try{

                if(!register_showPassword.isVisible()){

                    if(!register_showPassword.getText().equals(register_password.getText())){

                        register_showPassword.setText(register_password.getText());

                    }
                } else{

                    if (!register_showPassword.getText().equals(register_password.getText())) {

                        register_password.setText(register_showPassword.getText());

                    }

                }

                prepare = connect.prepareStatement(checkUsername);
                result = prepare.executeQuery();

                if(result.next()){
                    alert.errorMessage(register_username.getText()+"exist déja");
                }else if (register_password.getText().length() < 8) {

                    alert.errorMessage("Invalid Password, at least 8 characters needed");


                } else {
                    String insertData = "INSERT INTO admin (email, username, password, date) VALUES (?, ?, ?,?)";
                    // pour ajouter une date dans notre base de donnée
                    Date date = new Date();
                    java.sql.Date sqlDate = new java.sql.Date(date.getTime());

                    prepare = connect.prepareStatement(insertData);
                    prepare.setString(1, register_email.getText());
                    prepare.setString(2, register_username.getText());
                    prepare.setString(3, register_password.getText());
                    prepare.setString(4,String.valueOf(sqlDate));

                    prepare.executeUpdate();
                    alert.successMessage(" Registered Successfully");
                    registerClear();

                    // switch to log in form apres l enregistrement
                    login_form.setVisible(true);
                    register_form.setVisible(false);
                }

            }catch(Exception e){e.printStackTrace();}

        }

    }

    public void resetPassword() {
        String email = reset_email.getText();
        String newPassword = reset_password.getText();
        String confirmPassword = reset_checkPassword.getText(); // champ pour confirmer le mot de passe

        if(email.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            alert.errorMessage("Please fill all the fields");
            return;
        }

        if(!newPassword.equals(confirmPassword)) {
            alert.errorMessage("Passwords do not match");
            return;
        }

        String checkEmail = "SELECT * FROM admin WHERE email = ?";
        String updatePassword = "UPDATE admin SET password = ? WHERE email = ?";

        connect = DataBase.connectDB();

        try {

            if (!reset_resetShowPassword.isVisible() || !reset_resetCheckPassword.isVisible()) {

                if (!reset_password.getText().equals(reset_resetShowPassword.getText()) || !reset_checkPassword.getText().equals(reset_resetCheckPassword.getText() )){

                    reset_resetShowPassword.setText(reset_password.getText());
                    reset_resetCheckPassword.setText(reset_checkPassword.getText());
                }

            }else{

                if (!login_showPassword.getText().equals(login_password.getText())){

                    login_password.setText(login_showPassword.getText());
                }

            }

            // Vérifier si l'email existe
            prepare = connect.prepareStatement(checkEmail);
            prepare.setString(1, email);
            result = prepare.executeQuery();

            if(result.next()) {
                // Email existe, on peut mettre à jour le mot de passe
                prepare = connect.prepareStatement(updatePassword);
                prepare.setString(1, newPassword);
                prepare.setString(2, email);

                int rowsAffected = prepare.executeUpdate();
                if(rowsAffected > 0) {
                    alert.successMessage("Password updated successfully");
                }else if (register_password.getText().length() < 8) {

                    alert.errorMessage("Invalid Password, at least 8 characters needed");


                } else {
                    alert.errorMessage("Error: Password not updated");
                }

            } else {
                alert.errorMessage("Email not found");
            }

        } catch(Exception e) {
            e.printStackTrace();
            alert.errorMessage("An error occurred");
        }
    }

    public void registerClear(){
        register_email.clear();
        register_username.clear();
        register_password.clear();
        register_showPassword.clear();

    }

    public void registerShowPassword() {

        if (register_checkBox.isSelected()) {

            register_showPassword.setText(register_password.getText());
            register_showPassword.setVisible(true);
            register_password.setVisible(false);
        }else {
            register_password.setText(register_showPassword.getText());
            register_showPassword.setVisible(false);
            register_password.setVisible(true);

        }

    }

    public void loginShowPassword() {

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

    public void resetShowPassword() {

        if (reset_checkBox.isSelected()) {

            reset_resetShowPassword.setText(reset_password.getText());
            reset_resetShowPassword.setVisible(true);
            reset_password.setVisible(false);

            reset_resetCheckPassword.setText(reset_checkPassword.getText());
            reset_resetCheckPassword.setVisible(true);
            reset_checkPassword.setVisible(false);

        }else {
            reset_password.setText(reset_resetShowPassword.getText());
            reset_resetShowPassword.setVisible(false);
            reset_password.setVisible(true);

            reset_checkPassword.setText(reset_resetCheckPassword.getText());
            reset_resetCheckPassword.setVisible(false);
            reset_checkPassword.setVisible(true);

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


        } else if (login_user.getSelectionModel().getSelectedItem() == "Client portal"){
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




    public void switchForm(ActionEvent event) {

        if (event.getSource() == login_registerHere) {
            // here the registration form will show
            login_form.setVisible(false);
            register_form.setVisible(true);
            reset_form.setVisible(false);

        }else if (event.getSource() == login_forgotPassword) {
            //reset will show
            login_form.setVisible(false);
            register_form.setVisible(false);
            reset_form.setVisible(true);
        }
        else if (event.getSource() == register_loginHere) {
            //log in will show
            login_form.setVisible(true);
            register_form.setVisible(false);
            reset_form.setVisible(false);
        }else if (event.getSource() == reset_loginHere) {
            // get back to login from reset
            login_form.setVisible(true);
            register_form.setVisible(false);
            reset_form.setVisible(false);
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

    @FXML
    protected void onHelloButtonClick() {

    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userList();
    }


}
