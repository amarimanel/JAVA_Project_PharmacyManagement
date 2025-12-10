package fr.ece.pharmacymanagementsystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

public class AdministratorDashboardControl implements Initializable {

    @FXML private Label ID;
    @FXML private Label username;
    @FXML private AnchorPane main_form;
    @FXML private Button dashboard_btn;
    @FXML private Button dashboard_manageClientsBtn;
    @FXML private Button dashboard_manageEmployeesBtn;
    @FXML private Button dashboard_manageProductsBtn;
    @FXML private Button dashboard_salesRevenueBtn;
    @FXML private Hyperlink signout_link;
    @FXML private ImageView signout_icon;
    @FXML private ImageView user_logo;
    @FXML private Label minimizePage;
    @FXML private Label ClosePage;
    @FXML private AnchorPane dashboard;
    @FXML private Label dashboard_availableMed;
    @FXML private Label dashboard_totalClients;
    @FXML private Label dashboard_totalIncome;
    @FXML private AreaChart<String, Number> dashboard_chart;
    @FXML private AnchorPane client_page;
    @FXML private Button client_addBtn;
    @FXML private Button client_ClearBtn;
    @FXML private Button client_DeleteBtn;
    @FXML private Button client_updateBtn;
    @FXML private Button client_importBtn;
    @FXML private TextField client_fillId;
    @FXML private TextField client_fillFullName;
    @FXML private TextField client_fillNumber;
    @FXML private TextField client_fillStatus;
    @FXML private TextField client_FillPassword;
    @FXML private TextField client_fullSecurityNumber;
    @FXML private TextField client_search;
    @FXML private CheckBox client_allergiesCheckBox;
    @FXML private ComboBox<?> client_searchByType;
    @FXML private TableView<ClientData> client_tableView;
    @FXML private TableColumn<ClientData, Integer> client_id;
    @FXML private TableColumn<ClientData, String> client_clientId;
    @FXML private TableColumn<ClientData, String> client_fullName;
    @FXML private TableColumn<ClientData, String> client_number;
    @FXML private TableColumn<ClientData, String> client_status;
    @FXML private TableColumn<ClientData, String> client_password;
    @FXML private TableColumn<ClientData, String> client_securityNumber;
    @FXML private TableColumn<ClientData, String> client_allergies;
    @FXML private TableColumn<ClientData, java.util.Date> client_date;
    @FXML private TableColumn<ClientData, Double> client_totalSpent;
    @FXML private AnchorPane employee_page;
    @FXML private TableView<EmployeeData> addEmployee_tableView;
    @FXML private TextField employee_fillId;
    @FXML private TextField employee_fillFullName;
    @FXML private TextField employee_fillEmail;
    @FXML private TextField employee_FillUsername;
    @FXML private DatePicker employee_FillDate;
    @FXML private TextField employee_search;
    @FXML private ComboBox<String> employee_searchByType;
    @FXML private Button employee_addBtn;
    @FXML private Button employee_updateBtn;
    @FXML private Button employee_deleteBtn;
    @FXML private Button employee_clearBtn;
    @FXML private Button employee_importBtn;
    @FXML private TableColumn<EmployeeData, String> employee_id;
    @FXML private TableColumn<EmployeeData, String> employee_fullName;
    @FXML private TableColumn<EmployeeData, String> employee_email;
    @FXML private TableColumn<EmployeeData, String> employee_username;
    @FXML private TableColumn<EmployeeData, String> employee_date;
    @FXML private AnchorPane product_page;
    @FXML private TableView<ProductsData> addProduct_tableView;
    @FXML private TextField product_searchMedcineId;
    @FXML private TextField product_searchBrandName;
    @FXML private TextField product_searchproductName;
    @FXML private TextField product_searchPrice;
    @FXML private TextField product_searchQuantity;
    @FXML private ComboBox<String> product_selectCategory; // Input for Category
    @FXML private ComboBox<String> product_statusInput;    // Input for Status
    @FXML private DatePicker product_selectDate;

    @FXML private TextField product_search; // Main Search Bar
    @FXML private ComboBox<String> product_selectByCategory; // Filter for Search
    @FXML private Button product_addBtn;
    @FXML private Button product_updateBtn;
    @FXML private Button product_deleteBtn;
    @FXML private Button product_ClearBtn;
    @FXML private Button product_importBtn;
    @FXML private TableColumn<ProductsData, String> product_medcineId;
    @FXML private TableColumn<ProductsData, String> product_brandName;
    @FXML private TableColumn<ProductsData, String> product_productName;
    @FXML private TableColumn<ProductsData, String> product_category;
    @FXML private TableColumn<ProductsData, String> product_status;
    @FXML private TableColumn<ProductsData, String> product_Quantity;
    @FXML private TableColumn<ProductsData, String> product_Price;
    @FXML private TableColumn<ProductsData, String> product_expiryDate;



    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;
    private Statement statement;

    private double x = 0;
    private double y = 0;

    private ObservableList<ProductsData> addProductsList = FXCollections.observableArrayList();
    private ObservableList<EmployeeData> addEmployeesList = FXCollections.observableArrayList();

    // =============================================================

    private ObservableList<ClientData> addClientsList = FXCollections.observableArrayList();

    public ObservableList<ClientData> addClientsData() {
        ObservableList<ClientData> listData = FXCollections.observableArrayList();
        String sql = "SELECT * FROM client"; // Nom exact de ta table

        connect = DataBase.connectDB();

        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            ClientData clientD;

            while (result.next()) {
                clientD = new ClientData(
                        result.getInt("id"),
                        result.getString("client_id"),
                        result.getString("password"),
                        result.getString("full_name"),
                        result.getString("mobile_number"),
                        result.getString("allergies"),
                        result.getString("security_number"),
                        result.getDouble("total_spent"),
                        result.getDate("date"),
                        result.getString("status")
                );
                listData.add(clientD);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }

    public void addClientsShowListData() {

        ObservableList<ClientData> freshData = addClientsData();

        addClientsList.clear();
        addClientsList.addAll(freshData);

        client_id.setCellValueFactory(new PropertyValueFactory<>("id"));

        client_fullName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        client_number.setCellValueFactory(new PropertyValueFactory<>("mobileNumber"));
        client_status.setCellValueFactory(new PropertyValueFactory<>("status"));
        client_password.setCellValueFactory(new PropertyValueFactory<>("password"));
        client_securityNumber.setCellValueFactory(new PropertyValueFactory<>("securityNumber"));
        client_allergies.setCellValueFactory(new PropertyValueFactory<>("allergies"));


        client_totalSpent.setCellValueFactory(new PropertyValueFactory<>("totalSpent"));
        client_date.setCellValueFactory(new PropertyValueFactory<>("date"));

        client_tableView.setItems(addClientsList);
    }


//management produit

    public ObservableList<ProductsData> addProductsData() {
        ObservableList<ProductsData> listData = FXCollections.observableArrayList();
        String sql = "SELECT * FROM product";
        connect = DataBase.connectDB();
        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            ProductsData prodData;
            while (result.next()) {
                prodData = new ProductsData(
                        result.getInt("product_id"),
                        result.getString("brand_name"),
                        result.getString("product_name"),
                        result.getString("category"),
                        result.getString("status"),
                        result.getInt("quantity"),
                        result.getDouble("price"),
                        result.getDate("expiry_date")
                );
                listData.add(prodData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }

    public void addProductsShowListData() {
        ObservableList<ProductsData> freshData = addProductsData();

        // Use the class-level list
        addProductsList.clear();
        addProductsList.addAll(freshData);

        product_medcineId.setCellValueFactory(new PropertyValueFactory<>("medcineId"));
        product_brandName.setCellValueFactory(new PropertyValueFactory<>("brandName"));
        product_productName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        product_category.setCellValueFactory(new PropertyValueFactory<>("category"));
        product_status.setCellValueFactory(new PropertyValueFactory<>("status"));
        product_Quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        product_Price.setCellValueFactory(new PropertyValueFactory<>("price"));
        product_expiryDate.setCellValueFactory(new PropertyValueFactory<>("expiryDate"));

        addProduct_tableView.setItems(addProductsList);

        // Re-initialize search logic whenever list is reloaded
        productSearchLogic();
    }

    public void productAdd() {
        String sql = "INSERT INTO product (product_id, brand_name, product_name, category, status, quantity, price, expiry_date) "
                + "VALUES(?,?,?,?,?,?,?,?)";
        connect = DataBase.connectDB();
        try {
            Alert alert;
            if (product_searchMedcineId.getText().isEmpty()
                    || product_searchBrandName.getText().isEmpty()
                    || product_searchproductName.getText().isEmpty()
                    || product_selectCategory.getSelectionModel().getSelectedItem() == null
                    || product_statusInput.getSelectionModel().getSelectedItem() == null
                    || product_searchQuantity.getText().isEmpty()
                    || product_searchPrice.getText().isEmpty()
                    || product_selectDate.getValue() == null) {

                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            } else {
                String checkData = "SELECT product_id FROM product WHERE product_id = '"
                        + product_searchMedcineId.getText() + "'";
                statement = connect.createStatement();
                result = statement.executeQuery(checkData);

                if (result.next()) {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setContentText("Product ID: " + product_searchMedcineId.getText() + " already exists!");
                    alert.showAndWait();
                } else {
                    prepare = connect.prepareStatement(sql);
                    prepare.setString(1, product_searchMedcineId.getText());
                    prepare.setString(2, product_searchBrandName.getText());
                    prepare.setString(3, product_searchproductName.getText());
                    prepare.setString(4, product_selectCategory.getSelectionModel().getSelectedItem());
                    prepare.setString(5, product_statusInput.getSelectionModel().getSelectedItem());
                    prepare.setString(6, product_searchQuantity.getText());
                    prepare.setString(7, product_searchPrice.getText());
                    prepare.setString(8, String.valueOf(product_selectDate.getValue()));

                    prepare.executeUpdate();

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setContentText("Successfully Added!");
                    alert.showAndWait();

                    addProductsShowListData();
                    productClear();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void productUpdate() {
        String sql = "UPDATE product SET brand_name = ?, product_name = ?, category = ?, status = ?, quantity = ?, price = ?, expiry_date = ? WHERE product_id = ?";
        connect = DataBase.connectDB();
        try {
            Alert alert;
            if (product_searchMedcineId.getText().isEmpty() || product_selectDate.getValue() == null) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setContentText("Please select the item first");
                alert.showAndWait();
            } else {
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setContentText("Are you sure you want to UPDATE Product ID: " + product_searchMedcineId.getText() + "?");
                Optional<ButtonType> option = alert.showAndWait();

                if (option.get().equals(ButtonType.OK)) {
                    prepare = connect.prepareStatement(sql);
                    prepare.setString(1, product_searchBrandName.getText());
                    prepare.setString(2, product_searchproductName.getText());
                    prepare.setString(3, product_selectCategory.getSelectionModel().getSelectedItem());
                    prepare.setString(4, product_statusInput.getSelectionModel().getSelectedItem());
                    prepare.setString(5, product_searchQuantity.getText());
                    prepare.setString(6, product_searchPrice.getText());
                    prepare.setString(7, String.valueOf(product_selectDate.getValue()));
                    prepare.setString(8, product_searchMedcineId.getText());

                    prepare.executeUpdate();

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setContentText("Successfully Updated!");
                    alert.showAndWait();

                    addProductsShowListData();
                    productClear();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void productDelete() {
        String sql = "DELETE FROM product WHERE product_id = ?";
        connect = DataBase.connectDB();
        try {
            Alert alert;
            if (product_searchMedcineId.getText().isEmpty()) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setContentText("Please select the item first");
                alert.showAndWait();
            } else {
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setContentText("Are you sure you want to DELETE Product ID: " + product_searchMedcineId.getText() + "?");
                Optional<ButtonType> option = alert.showAndWait();

                if (option.get().equals(ButtonType.OK)) {
                    prepare = connect.prepareStatement(sql);
                    prepare.setString(1, product_searchMedcineId.getText());
                    prepare.executeUpdate();

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setContentText("Successfully Deleted!");
                    alert.showAndWait();

                    addProductsShowListData();
                    productClear();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void productClear() {
        product_searchMedcineId.setText("");
        product_searchBrandName.setText("");
        product_searchproductName.setText("");
        product_searchQuantity.setText("");
        product_searchPrice.setText("");
        product_selectCategory.getSelectionModel().clearSelection();
        product_statusInput.getSelectionModel().clearSelection();
        product_selectDate.setValue(null);
    }

    public void productSelect() {
        ProductsData prodData = addProduct_tableView.getSelectionModel().getSelectedItem();
        int num = addProduct_tableView.getSelectionModel().getSelectedIndex();

        if ((num - 1) < -1) return;

        product_searchMedcineId.setText(String.valueOf(prodData.getMedcineId()));
        product_searchBrandName.setText(prodData.getBrandName());
        product_searchproductName.setText(prodData.getProductName());
        product_searchQuantity.setText(String.valueOf(prodData.getQuantity()));
        product_searchPrice.setText(String.valueOf(prodData.getPrice()));
        product_selectDate.setValue(LocalDate.parse(String.valueOf(prodData.getExpiryDate())));
        // Select combo box values roughly matching
        product_selectCategory.getSelectionModel().select(prodData.getCategory());
        product_statusInput.getSelectionModel().select(prodData.getStatus());
    }

    public void productSearchLogic() {
        FilteredList<ProductsData> filteredData = new FilteredList<>(addProductsList, b -> true);

        product_search.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(product -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                String type = (product_selectByCategory.getValue() != null) ? product_selectByCategory.getValue() : "";

                if (type.equals("Medicine ID") && String.valueOf(product.getMedcineId()).toLowerCase().contains(lowerCaseFilter)) return true;
                else if (type.equals("Brand Name") && product.getBrandName().toLowerCase().contains(lowerCaseFilter)) return true;
                else if (type.equals("Category") && product.getCategory().toLowerCase().contains(lowerCaseFilter)) return true;
                else if (type.equals("Status") && product.getStatus().toLowerCase().contains(lowerCaseFilter)) return true;
                else return product.getProductName().toLowerCase().contains(lowerCaseFilter); // Default
            });
        });

        SortedList<ProductsData> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(addProduct_tableView.comparatorProperty());
        addProduct_tableView.setItems(sortedData);
    }

//management employée

    public ObservableList<EmployeeData> addEmployeesData() {
        ObservableList<EmployeeData> listData = FXCollections.observableArrayList();
        String sql = "SELECT * FROM admin";
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

    public void addEmployeesShowListData() {
        ObservableList<EmployeeData> freshData = addEmployeesData();
        addEmployeesList.clear();
        addEmployeesList.addAll(freshData);

        employee_email.setCellValueFactory(new PropertyValueFactory<>("email"));
        employee_username.setCellValueFactory(new PropertyValueFactory<>("username"));
        employee_fullName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        employee_date.setCellValueFactory(new PropertyValueFactory<>("date"));

        addEmployee_tableView.setItems(addEmployeesList);
        employeeSearchLogic();
    }

    public void employeeAdd() {
        String sql = "INSERT INTO admin (email, username, password, full_name, date) VALUES(?,?,?,?,?)";
        connect = DataBase.connectDB();
        try {
            Alert alert;
            if (employee_fillEmail.getText().isEmpty() || employee_FillUsername.getText().isEmpty()
                    || employee_fillFullName.getText().isEmpty() || employee_FillDate.getValue() == null) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            } else {
                prepare = connect.prepareStatement(sql);
                prepare.setString(1, employee_fillEmail.getText());
                prepare.setString(2, employee_FillUsername.getText());
                prepare.setString(3, "DefaultPass123");
                prepare.setString(4, employee_fillFullName.getText());
                prepare.setString(5, String.valueOf(employee_FillDate.getValue()));
                prepare.executeUpdate();

                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Successfully Added!");
                alert.showAndWait();
                addEmployeesShowListData();
                employeeClear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void employeeUpdate() {
        String sql = "UPDATE admin SET email = ?, full_name = ?, date = ? WHERE username = ?";
        connect = DataBase.connectDB();
        try {
            if (employee_FillUsername.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Please select an employee");
                alert.showAndWait();
            } else {
                prepare = connect.prepareStatement(sql);
                prepare.setString(1, employee_fillEmail.getText());
                prepare.setString(2, employee_fillFullName.getText());
                prepare.setString(3, String.valueOf(employee_FillDate.getValue()));
                prepare.setString(4, employee_FillUsername.getText());
                prepare.executeUpdate();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Successfully Updated!");
                alert.showAndWait();
                addEmployeesShowListData();
                employeeClear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void employeeDelete() {
        String sql = "DELETE FROM admin WHERE username = ?";
        connect = DataBase.connectDB();
        try {
            if (employee_FillUsername.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Please select an employee");
                alert.showAndWait();
            } else {
                prepare = connect.prepareStatement(sql);
                prepare.setString(1, employee_FillUsername.getText());
                prepare.executeUpdate();
                addEmployeesShowListData();
                employeeClear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void employeeClear() {
        employee_fillEmail.setText("");
        employee_FillUsername.setText("");
        employee_fillFullName.setText("");
        employee_FillDate.setValue(null);
    }

    public void employeeSelect() {
        EmployeeData empData = addEmployee_tableView.getSelectionModel().getSelectedItem();
        int num = addEmployee_tableView.getSelectionModel().getSelectedIndex();
        if ((num - 1) < -1) return;

        employee_FillUsername.setText(empData.getUsername());
        employee_fillFullName.setText(empData.getFullName());
        employee_fillEmail.setText(empData.getEmail());
        // Handle date if necessary
        // employee_FillDate.setValue(LocalDate.parse(String.valueOf(empData.getDate())));
    }

    public void employeeSearchLogic() {
        FilteredList<EmployeeData> filteredData = new FilteredList<>(addEmployeesList, e -> true);

        employee_search.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(employee -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String searchKey = newValue.toLowerCase();

                if (employee.getFullName().toLowerCase().contains(searchKey)) return true;
                else if (employee.getUsername().toLowerCase().contains(searchKey)) return true;
                else if (employee.getEmail().toLowerCase().contains(searchKey)) return true;
                else return false;
            });
        });

        SortedList<EmployeeData> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(addEmployee_tableView.comparatorProperty());
        addEmployee_tableView.setItems(sortedData);
    }


    public void switchForm(ActionEvent event) {
        if (event.getSource() == dashboard_btn) {
            dashboard.setVisible(true);
            client_page.setVisible(false);
            employee_page.setVisible(false);
            product_page.setVisible(false);

            dashboardDisplayTotalAvailable();
            dashboardDisplayTotalIncome();
            dashboardDisplayTotalClients();
            dashboardChart();

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
            addEmployeesShowListData();
        } else if (event.getSource() == dashboard_manageProductsBtn) {
            dashboard.setVisible(false);
            client_page.setVisible(false);
            employee_page.setVisible(false);
            product_page.setVisible(true);
            addProductsShowListData();
        }
    }

    public void logout() {
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Message");
            alert.setHeaderText(null);
            alert.setContentText("Are You Sure You Want To Logout?");
            Optional<ButtonType> option = alert.showAndWait();
            if (option.get() == ButtonType.OK) {
                signout_link.getScene().getWindow().hide();
                Parent root = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
                Stage stage = new Stage();
                Scene scene = new Scene(root);
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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void minimize() {
        Stage stage = (Stage) main_form.getScene().getWindow();
        stage.setIconified(true);
    }

    public void close() {
        System.exit(0);
    }

    private String[] categoryList = {"Painkillers", "Antibiotics", "Vitamins", "Supplements", "Cosmetics"};
    private String[] statusList = {"Available", "Not Available"};
    private String[] searchTypeList = {"Medicine ID", "Brand Name", "Product Name", "Category", "Status"};

    public void productCategoryList() {
        // Input Box
        ObservableList<String> listData = FXCollections.observableArrayList(categoryList);
        product_selectCategory.setItems(listData);

        // Status Box (This caused the previous crash)
        ObservableList<String> listStatus = FXCollections.observableArrayList(statusList);
        product_statusInput.setItems(listStatus);

        // Search Filter Box
        ObservableList<String> listType = FXCollections.observableArrayList(searchTypeList);
        product_selectByCategory.setItems(listType);
    }



    //---------------------------------------------------------------------------

    public void dashboardDisplayTotalAvailable() {
        String sql = "SELECT COUNT(product_id) FROM product WHERE status = 'Available'";
        connect = DataBase.connectDB();
        int count = 0;
        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            if (result.next()) {
                count = result.getInt(1);
            }
            dashboard_availableMed.setText(String.valueOf(count));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dashboardDisplayTotalIncome() {
        String sql = "SELECT SUM(total_spent) FROM client";
        connect = DataBase.connectDB();
        double sum = 0;
        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            if (result.next()) {
                sum = result.getDouble(1);
            }
            dashboard_totalIncome.setText("$" + sum);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dashboardDisplayTotalClients() {
        String sql = "SELECT COUNT(id) FROM client";
        connect = DataBase.connectDB();
        int count = 0;
        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            if (result.next()) {
                count = result.getInt(1);
            }
            dashboard_totalClients.setText(String.valueOf(count));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dashboardChart() {
        dashboard_chart.getData().clear();

        // fixed
        String sql = "SELECT date, SUM(total_spent) FROM client GROUP BY date ORDER BY date LIMIT 9";

        connect = DataBase.connectDB();
        XYChart.Series chart = new XYChart.Series();
        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            while (result.next()) {
                chart.getData().add(new XYChart.Data(result.getString(1), result.getDouble(2)));
            }
            dashboard_chart.getData().add(chart);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void displayUsernameAndId() {
        // On récupère "id123" stocké depuis le login
        String idInput = getData.admin_username;

        String sql = "SELECT * FROM administrator WHERE administrator_id = ?";

        connect = DataBase.connectDB();

        try {
            prepare = connect.prepareStatement(sql);
            prepare.setString(1, idInput);

            result = prepare.executeQuery();

            if (result.next()) {

                String adminID = result.getString("administrator_id");
                String adminName = result.getString("full_name");

                // Affichage dans les Labels
                ID.setText(adminID);
                username.setText(adminName);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // CLIENT MANAGEMENT LOGIC

    public void clientAdd() {
        String sql = "INSERT INTO client (client_id, password, full_name, mobile_number, allergies, security_number, total_spent, date, status) "
                + "VALUES(?,?,?,?,?,?,?,?,?)";

        connect = DataBase.connectDB();

        try {
            Alert alert;
            if (client_fillId.getText().isEmpty()  || client_fillFullName.getText().isEmpty()  || client_fillNumber.getText().isEmpty()
                    || client_fullSecurityNumber.getText().isEmpty()  || client_FillPassword.getText().isEmpty()
                    || client_fillStatus.getText().isEmpty()) {

                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            } else {
                String checkData = "SELECT client_id FROM client WHERE client_id = '" + client_fillId.getText() + "'";
                statement = connect.createStatement();
                result = statement.executeQuery(checkData);

                if (result.next()) {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setContentText("Client ID: " + client_fillId.getText() + " already exists!");
                    alert.showAndWait();
                } else {
                    prepare = connect.prepareStatement(sql);
                    prepare.setString(1, client_fillId.getText());
                    prepare.setString(2, client_FillPassword.getText());
                    prepare.setString(3, client_fillFullName.getText());
                    prepare.setString(4, client_fillNumber.getText());
                    String allergiesStatus = client_allergiesCheckBox.isSelected() ? "Yes" : "No";
                    prepare.setString(5, allergiesStatus);
                    prepare.setString(6, client_fullSecurityNumber.getText());
                    prepare.setDouble(7, 0.0);
                    java.util.Date date = new java.util.Date();
                    java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                    prepare.setString(8, String.valueOf(sqlDate));
                    prepare.setString(9, client_fillStatus.getText());

                    prepare.executeUpdate();

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setContentText("Successfully Added!");
                    alert.showAndWait();

                    addClientsShowListData();
                    clientClear();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clientUpdate() {
        String sql = "UPDATE client SET password = ?, full_name = ?, mobile_number = ?, allergies = ?, security_number = ?, status = ? WHERE client_id = ?";
        connect = DataBase.connectDB();

        try {
            if (client_fillId.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setContentText("Please select the client first");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setContentText("Are you sure you want to UPDATE Client ID: " + client_fillId.getText() + "?");
                Optional<ButtonType> option = alert.showAndWait();

                if (option.get().equals(ButtonType.OK)) {
                    prepare = connect.prepareStatement(sql);
                    prepare.setString(1, client_FillPassword.getText());
                    prepare.setString(2, client_fillFullName.getText());
                    prepare.setString(3, client_fillNumber.getText());

                    String allergiesStatus = client_allergiesCheckBox.isSelected() ? "Yes" : "No";
                    prepare.setString(4, allergiesStatus);

                    prepare.setString(5, client_fullSecurityNumber.getText());
                    prepare.setString(6, client_fillStatus.getText());
                    prepare.setString(7, client_fillId.getText());

                    prepare.executeUpdate();

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setContentText("Successfully Updated!");
                    alert.showAndWait();

                    addClientsShowListData();
                    clientClear();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clientDelete() {
        String sql = "DELETE FROM client WHERE client_id = ?";
        connect = DataBase.connectDB();

        try {
            if (client_fillId.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setContentText("Please select the client first");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setContentText("Are you sure you want to DELETE Client ID: " + client_fillId.getText() + "?");
                Optional<ButtonType> option = alert.showAndWait();

                if (option.get().equals(ButtonType.OK)) {
                    prepare = connect.prepareStatement(sql);
                    prepare.setString(1, client_fillId.getText());
                    prepare.executeUpdate();

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setContentText("Successfully Deleted!");
                    alert.showAndWait();

                    addClientsShowListData();
                    clientClear();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clientClear() {
        client_fillId.setText("");
        client_fillFullName.setText("");
        client_fillNumber.setText("");
        client_fullSecurityNumber.setText("");
        client_FillPassword.setText("");
        client_fillStatus.setText("");
        client_allergiesCheckBox.setSelected(false);
    }

    public void clientSelect() {
        ClientData clientD = client_tableView.getSelectionModel().getSelectedItem();
        int num = client_tableView.getSelectionModel().getSelectedIndex();

        if ((num - 1) < -1) return;

        client_fillId.setText(clientD.getClientId());
        client_fillFullName.setText(clientD.getFullName());
        client_fillNumber.setText(clientD.getMobileNumber());
        client_fullSecurityNumber.setText(clientD.getSecurityNumber());
        client_FillPassword.setText(clientD.getPassword());
        client_fillStatus.setText(clientD.getStatus());

        // Handle Checkbox Selection Logic
        if (clientD.getAllergies() != null && clientD.getAllergies().equalsIgnoreCase("Yes")) {
            client_allergiesCheckBox.setSelected(true);
        } else {
            client_allergiesCheckBox.setSelected(false);
        }
    }

    public void clientSearchLogic() {
        FilteredList<ClientData> filteredData = new FilteredList<>(addClientsList, e -> true);

        client_search.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(client -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String searchKey = newValue.toLowerCase();

                if (client.getFullName().toLowerCase().contains(searchKey)) return true;
                else if (client.getClientId().toLowerCase().contains(searchKey)) return true;
                else if (client.getMobileNumber().toLowerCase().contains(searchKey)) return true;
                else return false;
            });
        });

        SortedList<ClientData> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(client_tableView.comparatorProperty());
        client_tableView.setItems(sortedData);
    }

  // initialization

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        displayUsernameAndId();

        productCategoryList();


        addProductsShowListData();
        addEmployeesShowListData();

        addClientsShowListData();
        clientSearchLogic();


        dashboardDisplayTotalAvailable();
        dashboardDisplayTotalIncome();
        dashboardDisplayTotalClients();
        dashboardChart();
    }
}