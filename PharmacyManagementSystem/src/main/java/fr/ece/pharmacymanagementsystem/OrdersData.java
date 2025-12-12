package fr.ece.pharmacymanagementsystem;

import java.util.Date;

public class OrdersData {

    private Integer orderId;
    private String customerId;
    private Integer productId;
    private String brandName;
    private String productName;
    private Integer quantity;
    private Double price;
    private Date date;
    private String status;

    public OrdersData(Integer orderId, String customerId, Integer productId,
                      String brandName, String productName, Integer quantity,
                      Double price, Date date, String status) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.productId = productId;
        this.brandName = brandName;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.date = date;
        this.status = status;
    }

    public Integer getOrderId() { return orderId; }
    public String getCustomerId() { return customerId; }
    public Integer getProductId() { return productId; }
    public String getBrandName() { return brandName; }
    public String getProductName() { return productName; }
    public Integer getQuantity() { return quantity; }
    public Double getPrice() { return price; }
    public Date getDate() { return date; }
    public String getStatus() { return status; }
}