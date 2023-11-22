package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

// Create a SQLite database file and a table to store the usernames and passwords
public class Database {

    private static final String DB_URL = "jdbc:sqlite:users.db";

    public static void createDatabase() {
        Connection connection = null;
        Statement statement = null;
        try {
            // create a database connection
            connection = DriverManager.getConnection(DB_URL);
            statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            // create a table to store usernames and passwords
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY, username TEXT, password TEXT, role_id int)");
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        } finally {
            try {
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (Exception e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
            }
        }
    }

    public static void createAndPopulateOperationsTable() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            String createTableSQL = "CREATE TABLE IF NOT EXISTS t_operation (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "operation_name VARCHAR(255) NOT NULL);";
            stmt.executeUpdate(createTableSQL);

            String[] operations = {"print", "queue", "topQueue", "start", "stop", "restart", "status", "readConfig", "setConfig", "updateRole", "addUser"};
            for (String operation : operations) {
                String insertSQL = "INSERT INTO t_operation (operation_name) VALUES ('" + operation + "');";
                stmt.executeUpdate(insertSQL);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createRoleTable() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            String createTableSQL = "CREATE TABLE IF NOT EXISTS t_role (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "role_name VARCHAR(255) NOT NULL, " +
                    "op_id INT, " +
                    "FOREIGN KEY (op_id) REFERENCES t_operation(id));";
            stmt.executeUpdate(createTableSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void populateRoleTable() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            String[] roles = {"admin", "janitor", "power_user", "user", "invalid"};
            for (String role : roles) {
                String insertSQL = "INSERT INTO t_role (role_name, op_id) SELECT '" + role + "', id FROM t_operation;";
                stmt.executeUpdate(insertSQL);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
//    -- Admin: All operations
//    UPDATE t_role SET op_id = (SELECT GROUP_CONCAT(id) FROM t_operation) WHERE role_name = 'admin';
//
//-- Janitor: start, stop, restart, status, readConfig, setConfig
//    UPDATE t_role SET op_id = (SELECT GROUP_CONCAT(id) FROM t_operation WHERE operation_name IN ('start', 'stop', 'restart', 'status', 'readConfig', 'setConfig')) WHERE role_name = 'janitor';
//
//-- Power User: print, queue, topQueue, restart
//    UPDATE t_role SET op_id = (SELECT GROUP_CONCAT(id) FROM t_operation WHERE operation_name IN ('print', 'queue', 'topQueue', 'restart')) WHERE role_name = 'power_user';
//
//-- User: print, queue
//    UPDATE t_role SET op_id = (SELECT GROUP_CONCAT(id) FROM t_operation WHERE operation_name IN ('print', 'queue')) WHERE role

    public static void main(String[] args) {
        createAndPopulateOperationsTable();
        createRoleTable();
        populateRoleTable();
    }

}