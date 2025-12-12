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
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

public class ClientDashboardControl implements Initializable {


    @FXML

    private AnchorPane main_form;
    @FXML

    private Button dashboard_btn;
    @FXML

    private Button dashboard_orderProductBtn;
    @FXML

    private Label ID;
    @FXML

    private Label username;
    @FXML

    private Button dashboard_rechargeBalanceBtn;
    @FXML

    private Hyperlink signout_link;
    @FXML

    private AnchorPane dashboard;
    @FXML

    private AnchorPane product_page;
    @FXML

    private AnchorPane recharge_page;
    @FXML

    private Label client_fullName;
    @FXML

    private Label client_password;
    @FXML

    private Label client_phoneNumber;
    @FXML

    private Label client_securityNumber;
    @FXML

    private Label client_status;
    @FXML

    private Label client_totalSpent;
    @FXML

    private Label client_allergies;
    @FXML

    private CheckBox client_showMyPassword;
    @FXML

    private TableView<ProductsData> OrderProduct_tableView;
    @FXML

    private TableColumn<ProductsData, String> order_medcineId;
    @FXML

    private TableColumn<ProductsData, String> order_brandName;
    @FXML

    private TableColumn<ProductsData, String> order_productName;
    @FXML

    private TableColumn<ProductsData, String> order_category;
    @FXML

    private TableColumn<ProductsData, Double> order_Price;
    @FXML

    private TextField order_search;
    @FXML

    private TextField order_searchMedcineId;
    @FXML

    private TextField order_searchBrandName;
    @FXML

    private TextField order_searchproductName;
    @FXML

    private Label order_seePrice;
    @FXML

    private TextField order_amount;
    @FXML

    private Label order_balance;
    @FXML

    private Button order_addToCartBtn;
    @FXML

    private Button order_pay;
    @FXML

    private TextField order_RechargeMoney;
    @FXML

    private Button rechargeBtn;
    @FXML

    private ComboBox<?> order_selectByCategory;
    @FXML

    private ComboBox<?> order_selectCategory;
    @FXML

    private ImageView signout_icon;
    @FXML

    private ImageView user_logo;



    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;

    private ObservableList<ProductsData> productsList = FXCollections.observableArrayList();
    private String realPassword = "";
    private double currentBalance = 0;
    private double totalPrice = 0;



    public void displayClientInfo() {
        String userKey = getData.client_id;
        String sql = "SELECT * FROM client WHERE security_number = ?";
        connect = DataBase.connectDB();

        try {
            prepare = connect.prepareStatement(sql);
            prepare.setString(1, userKey);
            result = prepare.executeQuery();

            if (result.next()) {
                ID.setText(result.getString("client_id"));
                String name = result.getString("full_name");
                username.setText(name);
                client_fullName.setText(name);
                client_phoneNumber.setText(result.getString("mobile_number"));
                client_securityNumber.setText(result.getString("security_number"));
                client_status.setText(result.getString("status"));
                client_totalSpent.setText(String.format("%.2f EURO", result.getDouble("total_spent")));
                client_allergies.setText(result.getString("allergies"));

                // Balance
                currentBalance = result.getDouble("balance");
                if (order_balance != null) order_balance.setText(String.format("%.2f EURO", currentBalance));

                // Password
                realPassword = result.getString("password");
                client_password.setText("********");
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void showPasswordLogic() {
        if (client_showMyPassword.isSelected()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to view your password?");
            Optional<ButtonType> option = alert.showAndWait();

            if (option.get().equals(ButtonType.OK)) {
                client_password.setText(realPassword);
            } else {
                client_showMyPassword.setSelected(false);
            }
        } else {
            client_password.setText("********");
        }
    }



    public void showProductList() {
        productsList.clear();
        String sql = "SELECT * FROM product WHERE status = 'Available'";
        connect = DataBase.connectDB();
        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            while (result.next()) {
                productsList.add(new ProductsData(
                        result.getInt("product_id"), result.getString("brand_name"),
                        result.getString("product_name"), result.getString("category"),
                        result.getString("status"), result.getInt("quantity"),
                        result.getDouble("price"), result.getDate("expiry_date")
                ));
            }
        } catch (Exception e) { e.printStackTrace(); }

        order_medcineId.setCellValueFactory(new PropertyValueFactory<>("medcineId"));
        order_brandName.setCellValueFactory(new PropertyValueFactory<>("brandName"));
        order_productName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        order_category.setCellValueFactory(new PropertyValueFactory<>("category"));
        order_Price.setCellValueFactory(new PropertyValueFactory<>("price"));

        OrderProduct_tableView.setItems(productsList);
        searchLogic();
    }

    @FXML void productSelect(MouseEvent event) {

        ProductsData prod = OrderProduct_tableView.getSelectionModel().getSelectedItem();

        if (prod == null) return;

        order_searchMedcineId.setText(String.valueOf(prod.getMedcineId()));
        order_searchBrandName.setText(prod.getBrandName());
        order_searchproductName.setText(prod.getProductName());
        order_seePrice.setText(String.valueOf(prod.getPrice()));
        order_amount.setText("");
    }

    @FXML void addToCart(ActionEvent event) {
        if (order_searchMedcineId.getText().isEmpty() || order_amount.getText().isEmpty()) {

            showAlert(Alert.AlertType.ERROR, "Error", "Please select product and quantity.");


            return;
        }
        try {

            double price = Double.parseDouble(order_seePrice.getText());
            int qty = Integer.parseInt(order_amount.getText());
            totalPrice = price * qty;
            showAlert(Alert.AlertType.INFORMATION, "Cart Info", "Total Price: " + totalPrice + " EURO");


        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid Quantity");
        }
    }


    @FXML void payOrder(ActionEvent event) {
        if (totalPrice == 0) {
            showAlert(Alert.AlertType.ERROR, "Error", "Calculate total first (Add To Cart).");
            return;
        }
        if (currentBalance < totalPrice) {
            showAlert(Alert.AlertType.ERROR, "Insufficient Balance", "Please recharge your account.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Payment");
        alert.setContentText("Pay " + totalPrice + " EURO?");
        Optional<ButtonType> option = alert.showAndWait();

        if (option.get().equals(ButtonType.OK)) {
            connect = DataBase.connectDB();
            try {
                String insertOrder = "INSERT INTO customer_orders (customer_id, product_id, brand_name, product_name, quantity, price, date) VALUES(?,?,?,?,?,?,?)";
                prepare = connect.prepareStatement(insertOrder);
                prepare.setString(1, client_securityNumber.getText());
                prepare.setString(2, order_searchMedcineId.getText());
                prepare.setString(3, order_searchBrandName.getText());
                prepare.setString(4, order_searchproductName.getText());
                prepare.setString(5, order_amount.getText());
                prepare.setDouble(6, totalPrice);
                prepare.setDate(7, new java.sql.Date(System.currentTimeMillis()));
                prepare.executeUpdate();

                String updateBalance = "UPDATE client SET balance = balance - ?, total_spent = total_spent + ? WHERE security_number = ?";
                prepare = connect.prepareStatement(updateBalance);
                prepare.setDouble(1, totalPrice);
                prepare.setDouble(2, totalPrice);
                prepare.setString(3, client_securityNumber.getText());
                prepare.executeUpdate();

                showAlert(Alert.AlertType.INFORMATION, "Success", "Payment Successful!");
                displayClientInfo();
                totalPrice = 0;
                order_amount.setText("");
            } catch (Exception e) { e.printStackTrace(); }
        }
    }


    public void rechargeTransaction() {
        if (order_RechargeMoney.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter amount to recharge.");
            return;
        }
        try {
            double amount = Double.parseDouble(order_RechargeMoney.getText());
            if (amount <= 0) {
                showAlert(Alert.AlertType.ERROR, "Error", "Amount must be positive.");
                return;
            }


            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Recharge");
            alert.setContentText("Recharge account with " + amount + " EURO?");
            Optional<ButtonType> option = alert.showAndWait();

            if (option.get().equals(ButtonType.OK)) {
                String sql = "UPDATE client SET balance = balance + ? WHERE security_number = ?";
                connect = DataBase.connectDB();
                prepare = connect.prepareStatement(sql);
                prepare.setDouble(1, amount);
                prepare.setString(2, client_securityNumber.getText());
                prepare.executeUpdate();

                showAlert(Alert.AlertType.INFORMATION, "Success", "Recharge Successful!");
                order_RechargeMoney.setText("");
                displayClientInfo(); // Refresh balance
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid Amount.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @FXML void switchForm(ActionEvent event) {
        if (event.getSource() == dashboard_btn) {
            dashboard.setVisible(true);
            product_page.setVisible(false);
            if(recharge_page != null) recharge_page.setVisible(false);
            displayClientInfo();

        } else if (event.getSource() == dashboard_orderProductBtn) {
            dashboard.setVisible(false);
            product_page.setVisible(true);
            if(recharge_page != null) recharge_page.setVisible(false);
            showProductList();

        } else if (event.getSource() == dashboard_rechargeBalanceBtn) {
            dashboard.setVisible(false);
            product_page.setVisible(false);
            if(recharge_page != null) recharge_page.setVisible(true);
        }
    }

    @FXML void logout(ActionEvent event) {
        try {
            signout_link.getScene().getWindow().hide();
            Parent root = FXMLLoader.load(getClass().getResource("ClientPage.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void searchLogic() {
        FilteredList<ProductsData> filteredData = new FilteredList<>(productsList, b -> true);
        order_search.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(product -> {
                if (newValue == null || newValue.isEmpty()) return true;
                String lower = newValue.toLowerCase();
                if (product.getProductName().toLowerCase().contains(lower)) return true;
                else return product.getBrandName().toLowerCase().contains(lower);
            });
        });
        SortedList<ProductsData> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(OrderProduct_tableView.comparatorProperty());
        OrderProduct_tableView.setItems(sortedData);
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML public void minimize(MouseEvent event) {
        Stage stage = (Stage) main_form.getScene().getWindow();
        stage.setIconified(true);
    }
    @FXML public void close(MouseEvent event) { System.exit(0); }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        displayClientInfo();
        showProductList();

        // probleme avec setting dans scene builder alors remplacé par code
        client_showMyPassword.setOnAction(event -> showPasswordLogic());
        rechargeBtn.setOnAction(event -> rechargeTransaction());
    }
}