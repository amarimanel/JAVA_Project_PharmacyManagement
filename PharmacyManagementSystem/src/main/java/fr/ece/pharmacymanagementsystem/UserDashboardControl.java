package fr.ece.pharmacymanagementsystem;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class UserDashboardControl implements Initializable {

    @FXML
    private Label ID;

    @FXML
    private Button client_ClearBtn;

    @FXML
    private Button client_DeleteBtn;

    @FXML
    private TextField client_FillPassword;

    @FXML
    private Button client_addBtn;

    @FXML
    private TableColumn<?, ?> client_allergies;

    @FXML
    private CheckBox client_allergiesCheckBox;

    @FXML
    private TableColumn<?, ?> client_date;

    @FXML
    private TextField client_fillFullName;

    @FXML
    private TextField client_fillId;

    @FXML
    private TextField client_fillNumber;

    @FXML
    private TextField client_fillStatus;

    @FXML
    private TableColumn<?, ?> client_fullName;

    @FXML
    private TextField client_fullSecurityNumber;

    @FXML
    private TableColumn<?, ?> client_id;

    @FXML
    private Button client_importBtn;

    @FXML
    private TableColumn<?, ?> client_modifyDate;

    @FXML
    private TableColumn<?, ?> client_number;

    @FXML
    private AnchorPane client_page;

    @FXML
    private TableColumn<?, ?> client_password;

    @FXML
    private TextField client_search;

    @FXML
    private ComboBox<?> client_searchByType;

    @FXML
    private TableColumn<?, ?> client_securityNumber;

    @FXML
    private TableColumn<?, ?> client_status;

    @FXML
    private TableColumn<?, ?> client_totalSpent;

    @FXML
    private Button client_updateBtn;

    @FXML
    private AnchorPane dashboard;

    @FXML
    private Label dashboard_availableMed;

    @FXML
    private Button dashboard_btn;

    @FXML
    private AreaChart<?, ?> dashboard_chart;

    @FXML
    private Button dashboard_manageClientsBtn;

    @FXML
    private Button dashboard_manageEmployeesBtn;

    @FXML
    private Button dashboard_manageProductsBtn;

    @FXML
    private Button dashboard_salesRevenueBtn;

    @FXML
    private Label dashboard_totalClients;

    @FXML
    private Label dashboard_totalIncome;

    @FXML
    private DatePicker employee_FillDate;

    @FXML
    private TextField employee_FillUsername;

    @FXML
    private Button employee_addBtn;

    @FXML
    private Button employee_clearBtn;

    @FXML
    private TableColumn<?, ?> employee_date;

    @FXML
    private Button employee_deleteBtn;

    @FXML
    private TableColumn<?, ?> employee_email;

    @FXML
    private TextField employee_fillEmail;

    @FXML
    private TextField employee_fillFullName;

    @FXML
    private TextField employee_fillId;

    @FXML
    private TableColumn<?, ?> employee_fullName;

    @FXML
    private TableColumn<?, ?> employee_id;

    @FXML
    private Button employee_importBtn;

    @FXML
    private AnchorPane employee_page;

    @FXML
    private TextField employee_search;

    @FXML
    private ComboBox<?> employee_searchByType;

    @FXML
    private Button employee_updateBtn;

    @FXML
    private TableColumn<?, ?> employee_username;

    @FXML
    private Button product_ClearBtn;

    @FXML
    private TableColumn<?, ?> product_Price;

    @FXML
    private TableColumn<?, ?> product_Quantity;

    @FXML
    private Button product_addBtn;

    @FXML
    private TableColumn<?, ?> product_brandName;

    @FXML
    private TableColumn<?, ?> product_category;

    @FXML
    private Button product_deleteBtn;

    @FXML
    private TableColumn<?, ?> product_expiryDate;

    @FXML
    private Button product_importBtn;

    @FXML
    private TableColumn<?, ?> product_medcineId;

    @FXML
    private AnchorPane product_page;

    @FXML
    private TableColumn<?, ?> product_productName;

    @FXML
    private TextField product_search;

    @FXML
    private TextField product_searchBrandName;

    @FXML
    private TextField product_searchMedcineId;

    @FXML
    private TextField product_searchPrice;

    @FXML
    private TextField product_searchQuantity;

    @FXML
    private TextField product_searchproductName;

    @FXML
    private ComboBox<?> product_selectByCategory;

    @FXML
    private ComboBox<?> product_selectCategory;

    @FXML
    private DatePicker product_selectDate;

    @FXML
    private TableColumn<?, ?> product_status;

    @FXML
    private Button product_updateBtn;

    @FXML
    private ImageView signout_icon;

    @FXML
    private Hyperlink signout_link;

    @FXML
    private ImageView user_logo;

    @FXML
    private Label username;

    private double x = 0;
    private double y = 0;

    public void logout(){
        try{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Message");
            alert.setHeaderText(null);
            alert.setContentText("Are You Sure You Want To Logout");
            Optional<ButtonType> option = alert.showAndWait();

            if (option.get() == ButtonType.OK){

                //pour farmer dahsboard
                signout_link.getScene().getWindow().hide();

                //login
                Parent root = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
                Stage stage = new Stage();
                Scene scene = new Scene(root);

                root.setOnMousePressed((event) -> {
                    stage.setX(event.getScreenX() - x);
                    stage.setY(event.getScreenY() - y);

                    stage.setOpacity(0.8);

                });
                root.setOnMouseReleased((event) -> {
                    stage.setOpacity(1);
                });

                stage.initStyle(StageStyle.TRANSPARENT);

                stage.setScene(scene);
                stage.show();



            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }




    public void switchForm(ActionEvent event) {
        // 1. Logic to Switch Forms based on the button clicked
        // We check which button triggered the event using event.getSource()

        if (event.getSource() == dashboard_btn || event.getSource() == dashboard_salesRevenueBtn) {
            // Show Dashboard, Hide others
            dashboard.setVisible(true);
            client_page.setVisible(false);
            employee_page.setVisible(false);
            product_page.setVisible(false);

            // Optional: Reload dashboard specific data here
            // dashboardChart();

        } else if (event.getSource() == dashboard_manageClientsBtn) {
            // Show Clients, Hide others
            dashboard.setVisible(false);
            client_page.setVisible(true);
            employee_page.setVisible(false);
            product_page.setVisible(false);

            // Optional: Load client data
            // showClientData();

        } else if (event.getSource() == dashboard_manageEmployeesBtn) {
            // Show Employees, Hide others
            dashboard.setVisible(false);
            client_page.setVisible(false);
            employee_page.setVisible(true);
            product_page.setVisible(false);

            // Optional: Load employee data
            // showEmployeeData();

        } else if (event.getSource() == dashboard_manageProductsBtn) {
            // Show Products, Hide others
            dashboard.setVisible(false);
            client_page.setVisible(false);
            employee_page.setVisible(false);
            product_page.setVisible(true);

            // Optional: Load product data
            // showProductData();
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
