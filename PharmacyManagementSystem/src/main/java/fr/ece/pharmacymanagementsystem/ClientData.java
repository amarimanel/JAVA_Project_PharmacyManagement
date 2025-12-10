package fr.ece.pharmacymanagementsystem;

import java.util.Date;

public class ClientData {

    private Integer id;
    private String clientId;
    private String password;
    private String fullName;
    private String mobileNumber;
    private String allergies;
    private String securityNumber;
    private Double totalSpent;
    private Date date;
    private String status;

    // Constructeur
    public ClientData(Integer id, String clientId, String password, String fullName,
                      String mobileNumber, String allergies, String securityNumber,
                      Double totalSpent, Date date, String status) {
        this.id = id;
        this.clientId = clientId;
        this.password = password;
        this.fullName = fullName;
        this.mobileNumber = mobileNumber;
        this.allergies = allergies;
        this.securityNumber = securityNumber;
        this.totalSpent = totalSpent;
        this.date = date;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public String getClientId() {
        return clientId;
    }

    public String getPassword() {
        return password;
    }

    public String getFullName() {
        return fullName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getAllergies() {
        return allergies;
    }

    public String getSecurityNumber() {
        return securityNumber;
    }

    public Double getTotalSpent() {
        return totalSpent;
    }

    public Date getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }
}
