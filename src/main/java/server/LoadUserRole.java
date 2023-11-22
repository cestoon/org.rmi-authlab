package server;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LoadUserRole {
    private static final String DB_URL = "jdbc:sqlite:users.db";

    public static Map<Integer, Role> roles;

    static void loadRolesNew() {
        return;
    }

    static Map<Integer, Role> loadRoles() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            String sql = "SELECT id, role_name, op_id FROM t_role";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int roleId = rs.getInt("id");
                String roleName = rs.getString("role_name");
                String[] operationIds = rs.getString("op_id") != null ? rs.getString("op_id").split(",") : new String[0];
                List<String> operations = new ArrayList<>();

                for (String opIdStr : operationIds) {
                    int opId = Integer.parseInt(opIdStr.trim());
                    String operationSql = "SELECT operation_name FROM t_operation WHERE id = ?";
                    try (PreparedStatement opStmt = conn.prepareStatement(operationSql)) {
                        opStmt.setInt(1, opId);
                        ResultSet opRs = opStmt.executeQuery();
                        if (opRs.next()) {
                            operations.add(opRs.getString("operation_name"));
                        }
                    }
                }
                roles.put(roleId, new Role(roleId, roleName, operations));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roles;
    }

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


}
