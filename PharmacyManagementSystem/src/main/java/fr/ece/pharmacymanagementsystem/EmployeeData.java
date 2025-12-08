package fr.ece.pharmacymanagementsystem;

import java.util.Date;

public class EmployeeData {

    private String email;
    private String username;
    private String password;
    private String fullName;
    private Date date;

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFullName() {
        return fullName;
    }

    public Date getDate() {
        return date;
    }

    public EmployeeData(String email, String username, String password, String fullName, Date date) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.date = date;
    }
}
