package database;

import java.sql.*;

public class RoleDAO {

    private static final String DB_URL = "jdbc:sqlite:users.db"; // Replace with your actual database path

    // ... [Other fields and methods]

    public String getUserRole(String username) {
        String role = null;
        String sql = "SELECT r.role_name FROM users u JOIN t_role r ON u.role_id = r.id WHERE u.username = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                role = rs.getString("role_name");
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return role;
    }

    // ... [Rest of your UserDAO class]
}
