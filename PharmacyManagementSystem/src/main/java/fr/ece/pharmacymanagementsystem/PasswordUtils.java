package fr.ece.pharmacymanagementsystem;

// Make sure you imported the library from Step 1 for this to work
import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtils {

    // 1. Method to Hash a password (Used when Registering/Adding a user)
    public static String hashPassword(String plainTextPassword) {
        // BCrypt handles the "Salt" automatically
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }

    // 2. Method to Check a password (Used when Logging in)
    public static boolean verifyPassword(String plainTextPassword, String hashedPassword) {
        return BCrypt.checkpw(plainTextPassword, hashedPassword);
    }
}