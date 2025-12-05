package fr.ece.pharmacymanagementsystem;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DataBase {

    public static Connection connectDB(){

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/pharmacy", "root", "");
            return connect;
        } catch (Exception e) {
            e.printStackTrace();
        }return null;
    }
}
