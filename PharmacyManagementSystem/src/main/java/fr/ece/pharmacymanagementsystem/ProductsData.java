package fr.ece.pharmacymanagementsystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Date;

public class ProductsData {

    private Integer medcineId;
    private String brandName;
    private String productName;
    private Integer quantity;
    private String category ;
    private String status;
    private Double price;
    private Date expiryDate;



    public ProductsData(Integer productId, String brandName, String productName, String category,
                        String status, Integer quantity, Double price, Date expiryDate) {
        this.medcineId = productId;
        this.brandName = brandName;
        this.productName = productName;
        this.quantity = quantity;
        this.category = category;
        this.status = status;
        this.price = price;
        this.expiryDate = expiryDate;
    }

    public Integer getMedcineId() {
        return medcineId;
    }

    public String getBrandName() {
        return brandName;
    }

    public String getProductName() {
        return productName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getCategory() {
        return category;
    }

    public String getStatus() {
        return status;
    }

    public Double getPrice() {
        return price;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setMedcineId(Integer medcineId) {
        this.medcineId = medcineId;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }
}
