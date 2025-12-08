package fr.ece.pharmacymanagementsystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javafx.event.ActionEvent;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import static fr.ece.pharmacymanagementsystem.getData.admin_ID;
import static fr.ece.pharmacymanagementsystem.getData.admin_username;

public class UserDashboardControl implements Initializable {

    @FXML
    private Label ID;

    @FXML
    private TableView<EmployeeData> addEmployee_tableView;

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
    private TableColumn<EmployeeData, String> employee_fullName;

    @FXML
    private TableColumn<EmployeeData, String> employee_id;

    @FXML
    private Button employee_importBtn;

    @FXML
    private AnchorPane employee_page;

    @FXML
    private TextField employee_search;

    @FXML
    private ComboBox<String> employee_searchByType;

    @FXML
    private Button employee_updateBtn;

    @FXML
    private TableColumn<EmployeeData, String> employee_username;

    @FXML
    private Button product_ClearBtn;
    @FXML
    private TableView<ProductsData> addProduct_tableView;

    @FXML
    private TableColumn<ProductsData,String> product_Price;

    @FXML
    private TableColumn<ProductsData,String> product_Quantity;

    @FXML
    private Button product_addBtn;

    @FXML
    private TableColumn<ProductsData,String> product_brandName;

    @FXML
    private TableColumn<ProductsData,String> product_category;

    @FXML
    private Button product_deleteBtn;

    @FXML
    private TableColumn<ProductsData,String> product_expiryDate;

    @FXML
    private Button product_importBtn;

    @FXML
    private TableColumn<ProductsData,String> product_medcineId;

    @FXML
    private AnchorPane product_page;

    @FXML
    private TableColumn<ProductsData,String> product_productName;

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
    private Label minimizePage;

    @FXML
    private Label ClosePage;

    @FXML
    private ComboBox<?> product_selectByCategory;

    @FXML
    private ComboBox<String> product_selectCategory;

    @FXML
    private DatePicker product_selectDate;

    @FXML
    private TableColumn<ProductsData,String> product_status;

    @FXML
    private Button product_updateBtn;

    @FXML
    private ImageView signout_icon;

    @FXML
    private Hyperlink signout_link;

    @FXML
    private ImageView user_logo;


    @FXML
    private AnchorPane main_form;



    @FXML
    private Label username;

    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;


    public ObservableList<ProductsData> addProductsData(){

        String sql = "SELECT * FROM product";

        ObservableList<ProductsData> listData = FXCollections.observableArrayList();

        connect = DataBase.connectDB();

        try{
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            ProductsData prodData;
            while(result.next()){


                prodData = new ProductsData(result.getInt("product_id"), result.getString("brand_name")
                , result.getString("product_name"), result.getString("category"), result.getString("status")
                , result.getInt("quantity"), result.getDouble("price"), result.getDate("expiry_Date"));

                listData.add(prodData);
            }


        }catch(Exception e){
            e.printStackTrace();
        }
        return listData;
    }

    private ObservableList<ProductsData> addProductsList;
    public void addProductsShowListData(){
        addProductsList = addProductsData();

        product_medcineId.setCellValueFactory(new PropertyValueFactory<>("medcineId"));
        product_brandName.setCellValueFactory(new PropertyValueFactory<>("brandName"));
        product_productName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        product_category.setCellValueFactory(new PropertyValueFactory<>("category"));
        product_status.setCellValueFactory(new PropertyValueFactory<>("status"));
        product_Quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        product_Price.setCellValueFactory(new PropertyValueFactory<>("price"));
        product_expiryDate.setCellValueFactory(new PropertyValueFactory<>("expiry_Date"));

        addProduct_tableView.setItems(addProductsList);



    }

    private double x = 0;
    private double y = 0;

    public void logout() {
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Message");
            alert.setHeaderText(null);
            alert.setContentText("Are You Sure You Want To Logout");
            Optional<ButtonType> option = alert.showAndWait();

            if (option.get() == ButtonType.OK) {

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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void minimize(){

       Stage stage = (Stage)main_form.getScene().getWindow();
       stage.setIconified(true);

    }

    public void close(){
        System.exit(0);
    }


    public void switchForm(ActionEvent event) {

        if (event.getSource() == dashboard_btn || event.getSource() == dashboard_salesRevenueBtn) {

            dashboard.setVisible(true);
            client_page.setVisible(false);
            employee_page.setVisible(false);
            product_page.setVisible(false);

        } else if (event.getSource() == dashboard_manageClientsBtn) {

            dashboard.setVisible(false);
            client_page.setVisible(true);
            employee_page.setVisible(false);
            product_page.setVisible(false);

        } else if (event.getSource() == dashboard_manageEmployeesBtn) {

            dashboard.setVisible(false);
            client_page.setVisible(false);
            employee_page.setVisible(true);
            product_page.setVisible(false);

        } else if (event.getSource() == dashboard_manageProductsBtn) {

            dashboard.setVisible(false);
            client_page.setVisible(false);
            employee_page.setVisible(false);
            product_page.setVisible(true);
        }
    }

 /*   public void displayUsernameAndId(){

        String sql = "SELECT * FROM administrator WHERE username='"
                + admin_ID +  "'";

         connect = DataBase.connectDB();

         try{
             prepare = connect.prepareStatement(sql);
             result = prepare.executeQuery();

             if(result.next()){
                 ID.setText(result.getString("administrator_id"));
                 String TempUsername = result.getString("username");
                 TempUsername = TempUsername.substring(0, 1).toUpperCase() + TempUsername.substring(1);
                 username.setText(result.getString(TempUsername));
             }


         }catch(Exception e){
             e.printStackTrace();
         }

    }*/


    private final ObservableList<String> categoryList = FXCollections.observableArrayList(
            "Painkillers", "Vitamins", "Antibiotics", "Supplements", "Cosmetics"
    );

    @FXML
    public void CategoryBox() {
        product_selectCategory.setItems(categoryList);
    }


    public ObservableList<EmployeeData> addEmployeesData() {

        String sql = "SELECT * FROM admin";
        ObservableList<EmployeeData> listData = FXCollections.observableArrayList();

        connect = DataBase.connectDB();

        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            EmployeeData empData;
            while (result.next()) {
                empData = new EmployeeData(
                        result.getString("email"),
                        result.getString("username"),
                        result.getString("password"),
                        result.getString("full_name"),
                        result.getDate("date")
                );
                listData.add(empData);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }


    private ObservableList<EmployeeData> addEmployeesList;

    // Méthode pour afficher les employés dans le TableView
    public void addEmployeesShowListData() {
        addEmployeesList = addEmployeesData();

        employee_email.setCellValueFactory(new PropertyValueFactory<>("email"));
        employee_username.setCellValueFactory(new PropertyValueFactory<>("username"));
        employee_fullName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        employee_date.setCellValueFactory(new PropertyValueFactory<>("date"));

        addEmployee_tableView.setItems(addEmployeesList); // ton TableView pour les employés
    }




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //displayUsernameAndId();

        addProductsShowListData();
        CategoryBox();
        addEmployeesShowListData();
    }
}