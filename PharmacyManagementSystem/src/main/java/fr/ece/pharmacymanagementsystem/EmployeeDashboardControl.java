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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class EmployeeDashboardControl implements Initializable {

    // =============================================================
    // FXML VARIABLES
    // =============================================================

    @FXML private Label ID; // Will display USERNAME
    @FXML private Label username; // Will display FULL NAME
    @FXML private AnchorPane main_form;
    @FXML private Hyperlink signout_link;
    @FXML private ImageView user_logo;

    // --- DASHBOARD ---
    @FXML private AnchorPane dashboard;
    @FXML private Button dashboard_btn;
    @FXML private Label dashboard_availableMed;
    @FXML private Label dashboard_totalClients;
    @FXML private Label dashboard_totalIncome;
    @FXML private AreaChart<String, Number> dashboard_chart;
    @FXML private Button dashboard_manageClientsBtn;
    @FXML private Button dashboard_manageProductsBtn;

    // --- PRODUCTS ---
    @FXML private AnchorPane product_page;
    @FXML private TableView<ProductsData> addProduct_tableView;
    @FXML private TableColumn<ProductsData, String> product_medcineId;
    @FXML private TableColumn<ProductsData, String> product_brandName;
    @FXML private TableColumn<ProductsData, String> product_productName;
    @FXML private TableColumn<ProductsData, String> product_category;
    @FXML private TableColumn<ProductsData, String> product_status;
    @FXML private TableColumn<ProductsData, Integer> product_Quantity;
    @FXML private TableColumn<ProductsData, Double> product_Price;
    @FXML private TableColumn<ProductsData, java.util.Date> product_expiryDate;

    @FXML private TextField product_search;
    @FXML private TextField product_searchMedcineId;
    @FXML private TextField product_searchBrandName;
    @FXML private TextField product_searchproductName;
    @FXML private TextField product_searchQuantity;
    @FXML private TextField product_searchPrice;

    @FXML private ComboBox<String> product_selectCategory;
    @FXML private ComboBox<String> product_statusInput;
    @FXML private ComboBox<String> product_selectByCategory;
    @FXML private DatePicker product_selectDate;

    @FXML private Button product_addBtn;
    @FXML private Button product_updateBtn;
    @FXML private Button product_deleteBtn;
    @FXML private Button product_ClearBtn;

    // --- CLIENTS ---
    @FXML private AnchorPane client_page;
    @FXML private TableView<ClientData> client_tableView;
    @FXML private TableColumn<ClientData, Integer> client_id;
    @FXML private TableColumn<ClientData, String> client_fullName;
    @FXML private TableColumn<ClientData, String> client_number;
    @FXML private TableColumn<ClientData, String> client_securityNumber;
    @FXML private TableColumn<ClientData, String> client_status;
    @FXML private TableColumn<ClientData, String> client_allergies;
    @FXML private TableColumn<ClientData, Double> client_totalSpent;
    @FXML private TableColumn<ClientData, java.util.Date> client_date;
    // We keep client_password column variable but we won't show it or use it

    @FXML private TextField client_fillId;
    @FXML private TextField client_fillFullName;
    @FXML private TextField client_fillNumber;
    @FXML private TextField client_fullSecurityNumber;
    @FXML private TextField client_FillPassword; // Exists in FXML but we will ignore/hide it
    @FXML private TextField client_fillStatus;
    @FXML private TextField client_search;
    @FXML private CheckBox client_allergiesCheckBox;

    @FXML private Button client_addBtn;
    @FXML private Button client_updateBtn;
    @FXML private Button client_DeleteBtn;
    @FXML private Button client_ClearBtn;

    // =============================================================
    // DATABASE & LISTS
    // =============================================================
    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;
    private double x = 0;
    private double y = 0;

    private ObservableList<ProductsData> addProductsList = FXCollections.observableArrayList();
    private ObservableList<ClientData> addClientsList = FXCollections.observableArrayList();

    // =============================================================
    // 1. DISPLAY USER INFO (FIXED)
    // =============================================================

    public void displayUsernameAndId() {
        // Current logged in ID (e.g., "id123")
        String idInput = getData.admin_username;

        // Query database to get details
        String sql = "SELECT * FROM admin WHERE administrator_id = ?";

        connect = DataBase.connectDB();
        try {
            prepare = connect.prepareStatement(sql);
            prepare.setString(1, idInput);
            result = prepare.executeQuery();

            if (result.next()) {
                // REQ: "ID" label shows Username
                ID.setText(result.getString("username"));

                // REQ: "username" label shows Full Name
                username.setText(result.getString("full_name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =============================================================
    // 2. CLIENT MANAGEMENT (MODIFIED FOR EMPLOYEE)
    // =============================================================

    public ObservableList<ClientData> addClientsData() {
        ObservableList<ClientData> listData = FXCollections.observableArrayList();
        String sql = "SELECT * FROM client";
        connect = DataBase.connectDB();
        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            while (result.next()) {
                listData.add(new ClientData(
                        result.getInt("id"), result.getString("client_id"), result.getString("password"),
                        result.getString("full_name"), result.getString("mobile_number"), result.getString("allergies"),
                        result.getString("security_number"), result.getDouble("total_spent"), result.getDate("date"),
                        result.getString("status")
                ));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return listData;
    }

    public void addClientsShowListData() {
        addClientsList.clear();
        addClientsList.addAll(addClientsData());

        client_id.setCellValueFactory(new PropertyValueFactory<>("clientId"));
        client_fullName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        client_number.setCellValueFactory(new PropertyValueFactory<>("mobileNumber"));
        client_status.setCellValueFactory(new PropertyValueFactory<>("status"));
        client_totalSpent.setCellValueFactory(new PropertyValueFactory<>("totalSpent"));
        client_date.setCellValueFactory(new PropertyValueFactory<>("date"));
        client_securityNumber.setCellValueFactory(new PropertyValueFactory<>("securityNumber"));
        client_allergies.setCellValueFactory(new PropertyValueFactory<>("allergies"));

        client_tableView.setItems(addClientsList);
        clientSearchLogic();
    }

    @FXML void clientAdd(ActionEvent event) {
        String sql = "INSERT INTO client (client_id, password, full_name, mobile_number, allergies, security_number, total_spent, date, status) VALUES(?,?,?,?,?,?,?,?,?)";
        connect = DataBase.connectDB();
        try {
            if (client_fillId.getText().isEmpty() || client_fillFullName.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error", "Fill blank fields");
            } else {
                prepare = connect.prepareStatement(sql);
                prepare.setString(1, client_fillId.getText());

                // FIXED: Employees cannot set password, so we set a DEFAULT one
                prepare.setString(2, "123456");

                prepare.setString(3, client_fillFullName.getText());
                prepare.setString(4, client_fillNumber.getText());
                prepare.setString(5, client_allergiesCheckBox.isSelected() ? "Yes" : "No");
                prepare.setString(6, client_fullSecurityNumber.getText());
                prepare.setDouble(7, 0.0);
                prepare.setString(8, String.valueOf(new java.sql.Date(System.currentTimeMillis())));
                prepare.setString(9, client_fillStatus.getText());

                prepare.executeUpdate();
                showAlert(Alert.AlertType.INFORMATION, "Success", "Client Added (Default Pass: 123456)");
                addClientsShowListData();
                clientClear(event);
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML void clientUpdate(ActionEvent event) {
        // FIXED: SQL update does NOT include the password column
        String sql = "UPDATE client SET full_name=?, mobile_number=?, allergies=?, security_number=?, status=? WHERE client_id=?";

        connect = DataBase.connectDB();
        try {
            if (client_fillId.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error", "Select client first");
            } else {
                prepare = connect.prepareStatement(sql);
                // No password parameter here
                prepare.setString(1, client_fillFullName.getText());
                prepare.setString(2, client_fillNumber.getText());
                prepare.setString(3, client_allergiesCheckBox.isSelected() ? "Yes" : "No");
                prepare.setString(4, client_fullSecurityNumber.getText());
                prepare.setString(5, client_fillStatus.getText());
                prepare.setString(6, client_fillId.getText());

                prepare.executeUpdate();
                showAlert(Alert.AlertType.INFORMATION, "Success", "Client Updated (Password Unchanged)");
                addClientsShowListData();
                clientClear(event);
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML void clientDelete(ActionEvent event) {
        String sql = "DELETE FROM client WHERE client_id=?";
        connect = DataBase.connectDB();
        try {
            if (client_fillId.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error", "Select client first");
            } else {
                prepare = connect.prepareStatement(sql);
                prepare.setString(1, client_fillId.getText());
                prepare.executeUpdate();
                showAlert(Alert.AlertType.INFORMATION, "Success", "Client Deleted!");
                addClientsShowListData();
                clientClear(event);
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML void clientClear(ActionEvent event) {
        client_fillId.setText("");
        client_fillFullName.setText("");
        client_fillNumber.setText("");
        client_fullSecurityNumber.setText("");
        if(client_FillPassword != null) client_FillPassword.setText(""); // Clear if exists
        client_fillStatus.setText("");
        client_allergiesCheckBox.setSelected(false);
    }

    @FXML void clientSelect(MouseEvent event) {
        ClientData client = client_tableView.getSelectionModel().getSelectedItem();
        if (client == null) return;
        client_fillId.setText(client.getClientId());
        client_fillFullName.setText(client.getFullName());
        client_fillNumber.setText(client.getMobileNumber());
        client_fullSecurityNumber.setText(client.getSecurityNumber());
        client_fillStatus.setText(client.getStatus());
        client_allergiesCheckBox.setSelected(client.getAllergies().equalsIgnoreCase("Yes"));

        // FIXED: Do NOT fill the password field
        if(client_FillPassword != null) client_FillPassword.setText("");
    }

    public void clientSearchLogic() {
        FilteredList<ClientData> filteredData = new FilteredList<>(addClientsList, e -> true);
        client_search.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(client -> {
                if (newValue == null || newValue.isEmpty()) return true;
                String lower = newValue.toLowerCase();
                if (client.getFullName().toLowerCase().contains(lower)) return true;
                return client.getClientId().toLowerCase().contains(lower);
            });
        });
        SortedList<ClientData> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(client_tableView.comparatorProperty());
        client_tableView.setItems(sortedData);
    }

    // =============================================================
    // 3. PRODUCT MANAGEMENT (Restored Functionality)
    // =============================================================

    public ObservableList<ProductsData> addProductsData() {
        ObservableList<ProductsData> listData = FXCollections.observableArrayList();
        String sql = "SELECT * FROM product";
        connect = DataBase.connectDB();
        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            while (result.next()) {
                listData.add(new ProductsData(
                        result.getInt("product_id"), result.getString("brand_name"), result.getString("product_name"),
                        result.getString("category"), result.getString("status"), result.getInt("quantity"),
                        result.getDouble("price"), result.getDate("expiry_date")
                ));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return listData;
    }

    public void addProductsShowListData() {
        addProductsList.clear();
        addProductsList.addAll(addProductsData());

        product_medcineId.setCellValueFactory(new PropertyValueFactory<>("medcineId"));
        product_brandName.setCellValueFactory(new PropertyValueFactory<>("brandName"));
        product_productName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        product_category.setCellValueFactory(new PropertyValueFactory<>("category"));
        product_status.setCellValueFactory(new PropertyValueFactory<>("status"));
        product_Quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        product_Price.setCellValueFactory(new PropertyValueFactory<>("price"));
        product_expiryDate.setCellValueFactory(new PropertyValueFactory<>("expiryDate"));

        addProduct_tableView.setItems(addProductsList);
        productSearchLogic();
    }

    @FXML void productAdd(ActionEvent event) {
        String sql = "INSERT INTO product (product_id, brand_name, product_name, category, status, quantity, price, expiry_date) VALUES(?,?,?,?,?,?,?,?)";
        connect = DataBase.connectDB();
        try {
            if (product_searchMedcineId.getText().isEmpty() || product_searchproductName.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error", "Fill blank fields");
            } else {
                prepare = connect.prepareStatement(sql);
                prepare.setString(1, product_searchMedcineId.getText());
                prepare.setString(2, product_searchBrandName.getText());
                prepare.setString(3, product_searchproductName.getText());
                prepare.setString(4, product_selectCategory.getValue());
                prepare.setString(5, product_statusInput.getValue());
                prepare.setString(6, product_searchQuantity.getText());
                prepare.setString(7, product_searchPrice.getText());
                prepare.setString(8, String.valueOf(product_selectDate.getValue()));
                prepare.executeUpdate();
                showAlert(Alert.AlertType.INFORMATION, "Success", "Product Added!");
                addProductsShowListData();
                productClear(event);
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML void productUpdate(ActionEvent event) {
        String sql = "UPDATE product SET brand_name=?, product_name=?, category=?, status=?, quantity=?, price=?, expiry_date=? WHERE product_id=?";
        connect = DataBase.connectDB();
        try {
            if (product_searchMedcineId.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error", "Select product first");
            } else {
                prepare = connect.prepareStatement(sql);
                prepare.setString(1, product_searchBrandName.getText());
                prepare.setString(2, product_searchproductName.getText());
                prepare.setString(3, product_selectCategory.getValue());
                prepare.setString(4, product_statusInput.getValue());
                prepare.setString(5, product_searchQuantity.getText());
                prepare.setString(6, product_searchPrice.getText());
                prepare.setString(7, String.valueOf(product_selectDate.getValue()));
                prepare.setString(8, product_searchMedcineId.getText());
                prepare.executeUpdate();
                showAlert(Alert.AlertType.INFORMATION, "Success", "Product Updated!");
                addProductsShowListData();
                productClear(event);
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML void productDelete(ActionEvent event) {
        String sql = "DELETE FROM product WHERE product_id = ?";
        connect = DataBase.connectDB();
        try {
            if (product_searchMedcineId.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error", "Select product first");
            } else {
                prepare = connect.prepareStatement(sql);
                prepare.setString(1, product_searchMedcineId.getText());
                prepare.executeUpdate();
                showAlert(Alert.AlertType.INFORMATION, "Success", "Product Deleted!");
                addProductsShowListData();
                productClear(event);
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML void productClear(ActionEvent event) {
        product_searchMedcineId.setText("");
        product_searchBrandName.setText("");
        product_searchproductName.setText("");
        product_searchQuantity.setText("");
        product_searchPrice.setText("");
        product_selectDate.setValue(null);
    }

    @FXML void productSelect(MouseEvent event) {
        ProductsData prod = addProduct_tableView.getSelectionModel().getSelectedItem();
        if (prod == null) return;
        product_searchMedcineId.setText(String.valueOf(prod.getMedcineId()));
        product_searchBrandName.setText(prod.getBrandName());
        product_searchproductName.setText(prod.getProductName());
        product_searchQuantity.setText(String.valueOf(prod.getQuantity()));
        product_searchPrice.setText(String.valueOf(prod.getPrice()));
        product_selectCategory.setValue(prod.getCategory());
        product_statusInput.setValue(prod.getStatus());
        product_selectDate.setValue(LocalDate.parse(String.valueOf(prod.getExpiryDate())));
    }

    public void productSearchLogic() {
        FilteredList<ProductsData> filteredData = new FilteredList<>(addProductsList, b -> true);
        product_search.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(product -> {
                if (newValue == null || newValue.isEmpty()) return true;
                String lower = newValue.toLowerCase();
                if (product.getProductName().toLowerCase().contains(lower)) return true;
                return String.valueOf(product.getMedcineId()).contains(lower);
            });
        });
        SortedList<ProductsData> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(addProduct_tableView.comparatorProperty());
        addProduct_tableView.setItems(sortedData);
    }

    // =============================================================
    // UTILS & INIT
    // =============================================================

    @FXML void switchForm(ActionEvent event) {
        if (event.getSource() == dashboard_btn) {
            dashboard.setVisible(true);
            product_page.setVisible(false);
            client_page.setVisible(false);
            dashboardChart();
        } else if (event.getSource() == dashboard_manageProductsBtn) {
            dashboard.setVisible(false);
            product_page.setVisible(true);
            client_page.setVisible(false);
            addProductsShowListData();
        } else if (event.getSource() == dashboard_manageClientsBtn) {
            dashboard.setVisible(false);
            product_page.setVisible(false);
            client_page.setVisible(true);
            addClientsShowListData();
        }
    }

    @FXML void logout(ActionEvent event) {
        try {
            signout_link.getScene().getWindow().hide();
            Parent root = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.show();
        } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML void minimize(MouseEvent event) {
        Stage stage = (Stage) main_form.getScene().getWindow();
        stage.setIconified(true);
    }
    @FXML void close(MouseEvent event) { System.exit(0); }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Dashboard Stats Logic (Simplified for Employee)
    public void dashboardChart() {
        dashboard_chart.getData().clear();
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
        } catch (Exception e) { e.printStackTrace(); }

        // Also load total Income and Clients count here
        try {
            String sqlIncome = "SELECT SUM(total_spent) FROM client";
            prepare = connect.prepareStatement(sqlIncome);
            result = prepare.executeQuery();
            if(result.next()) dashboard_totalIncome.setText("$" + result.getDouble(1));

            String sqlClients = "SELECT COUNT(id) FROM client";
            prepare = connect.prepareStatement(sqlClients);
            result = prepare.executeQuery();
            if(result.next()) dashboard_totalClients.setText(String.valueOf(result.getInt(1)));

            String sqlMed = "SELECT COUNT(product_id) FROM product WHERE status='Available'";
            prepare = connect.prepareStatement(sqlMed);
            result = prepare.executeQuery();
            if(result.next()) dashboard_availableMed.setText(String.valueOf(result.getInt(1)));
        } catch(Exception e) { e.printStackTrace(); }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        displayUsernameAndId();

        // Initialize ComboBoxes
        product_selectCategory.setItems(FXCollections.observableArrayList("Painkillers", "Antibiotics", "Vitamins", "Supplements"));
        product_statusInput.setItems(FXCollections.observableArrayList("Available", "Not Available"));
        product_selectByCategory.setItems(FXCollections.observableArrayList("Medicine ID", "Brand Name", "Product Name"));

        addProductsShowListData();
        addClientsShowListData();
        dashboardChart();

        if(client_FillPassword != null) client_FillPassword.setVisible(false); // Hide the field if possible
    }
}