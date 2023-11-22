package server;

import java.util.List;

public class Role {
    private int roleId;
    private String roleName;
    private List<String> operations;

    public Role(int roleId, String roleName, List<String> operations) {
        this.roleId = roleId;
        this.roleName = roleName;
        this.operations = operations;
    }

    // Getters
    public int getRoleId() {
        return roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public List<String> getOperations() {
        return operations;
    }

    // Setters
    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public void setOperations(List<String> operations) {
        this.operations = operations;
    }

    public static List<String> getOperationsForRole(String roleName) {
        // This method should connect to the database and fetch the operations for the given role
        // For simplicity, it's returning a hardcoded list, replace this with actual database code
        switch (roleName) {
            case "admin":
                return List.of("Print", "Queue", "TopQueue", "Start Server", "Stop Server", "Restart Server", "Get status", "Read Configuration", "Set Configuration", "updateRole", "addUser");
            case "janitor":
                return List.of("Start Server", "Stop Server", "Restart Server", "Get status", "Read Configuration", "Set Configuration");
            case "power_user":
                return List.of("Print", "Queue", "TopQueue", "Restart Server");
            case "user":
                return List.of("Print", "Queue");
            default:
                return List.of(); // invalid role or no operations
        }
    }

    @Override
    public String toString() {
        return "Role{" +
                "roleId=" + roleId +
                ", roleName='" + roleName + '\'' +
                ", operations=" + operations +
                '}';
    }
}
