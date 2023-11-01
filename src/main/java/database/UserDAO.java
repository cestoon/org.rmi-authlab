package database;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.Base64;
import org.mindrot.jbcrypt.BCrypt;


public class UserDAO {

    private static final String DB_URL = "jdbc:sqlite:users.db";

    // without hash version
    public void addUser(String username, String password) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:users.db");

            // insert the new user into the database
            preparedStatement = connection.prepareStatement("INSERT INTO users (username, password) VALUES (?, ?)");
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (Exception e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
            }
        }
    }

    // with hash version
    public boolean authenticateUser(String username, String enteredPassword) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement pstmt = conn.prepareStatement("SELECT password FROM users WHERE username = ?")) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String storedHashedPassword = rs.getString("password");
                    String generatedSecuredPasswordHash = BCrypt.hashpw(enteredPassword, BCrypt.gensalt(12));
                    boolean matched = BCrypt.checkpw(storedHashedPassword, generatedSecuredPasswordHash);
                    System.out.println(matched);
                    return matched;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    private static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Could not hash password", e);
        }
    }
    // with hash version
    private static void storeUser(String username, String hashedPassword) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:users.db")) {
//            String sql = "CREATE TABLE IF NOT EXISTS users (username TEXT PRIMARY KEY, password TEXT)";
//            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
//                pstmt.executeUpdate();
//            }
            String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, username);
                pstmt.setString(2, hashedPassword);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Could not store user", e);
        }
    }

    public static void main(String[] args) {
        UserDAO userDAO = new UserDAO();
        // Add a user to the database
//        String username = "test1";
//        String password = "group29";
        String username = "admin";
        String password = "admin";
        String hashedPassword = hashPassword(password);
        // Store hashed password in database
        storeUser(username, hashedPassword);

    }
}
