package database;

import utils.SessionTokenGenerator;
import utils.Logger;
import java.sql.*;
import java.time.LocalDateTime;

public class SessionDAO {
    private static final java.util.logging.Logger logger = Logger.getLogger(SessionDAO.class);

    private static final String DB_URL = "jdbc:sqlite:sessions.db";
    public SessionDAO() {

    }
    public static void createTable() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS sessions " +
                    "(id INTEGER PRIMARY KEY, " +
                    "token TEXT NOT NULL, " +
                    "username TEXT NOT NULL, " +
                    "expiration TIMESTAMP NOT NULL)";
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String createSession(String username, int expirationMinutes) {
        SessionTokenGenerator sessionTokenGenerator = new SessionTokenGenerator();
        String token = sessionTokenGenerator.generateToken();
        LocalDateTime expiration = LocalDateTime.now().plusMinutes(expirationMinutes);

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO sessions (token, username, expiration) VALUES (?, ?, ?)")) {
            pstmt.setString(1, token);
            pstmt.setString(2, username);
            pstmt.setTimestamp(3, Timestamp.valueOf(expiration));
            pstmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return token;
    }

    public boolean validateSession(String token) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement pstmt = conn.prepareStatement("SELECT expiration FROM sessions WHERE token = ?")) {
            pstmt.setString(1, token);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    LocalDateTime expiration = rs.getTimestamp("expiration").toLocalDateTime();
                    if (LocalDateTime.now().isBefore(expiration)) {
                        logger.info("The session is valid");
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

//    public static void main(String[] args) {
//        createTable();
//    }

}
