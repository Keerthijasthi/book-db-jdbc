
import java.sql.*;

public class BookDB {

    // Use the same DB path as H2 Console (~/test)
    static final String JDBC_URL = "jdbc:h2:~/test";  // Shared DB file
    static final String JDBC_USER = "sa";
    static final String JDBC_PASS = "";

    public static void main(String[] args) {
        try {
            // Load H2 driver
            Class.forName("org.h2.Driver");

            // Connect to the H2 database
            try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASS)) {
                createTable(conn);     // Create books table if not exists
                insertBook(conn, 1, "The Alchemist", "Paulo Coelho");
                insertBook(conn, 2, "1984", "George Orwell");

                System.out.println("Books in the database:");
                fetchBooks(conn);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void createTable(Connection conn) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS books (" +
                "id INT PRIMARY KEY, " +
                "title VARCHAR(255), " +
                "author VARCHAR(255)" +
                ")";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }

    private static void insertBook(Connection conn, int id, String title, String author) throws SQLException {
        // Insert only if not already exists (to avoid duplicate PK errors)
        String check = "SELECT COUNT(*) FROM books WHERE id = ?";
        try (PreparedStatement checkStmt = conn.prepareStatement(check)) {
            checkStmt.setInt(1, id);
            ResultSet rs = checkStmt.executeQuery();
            rs.next();
            if (rs.getInt(1) == 0) {
                String sql = "INSERT INTO books (id, title, author) VALUES (?, ?, ?)";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, id);
                    pstmt.setString(2, title);
                    pstmt.setString(3, author);
                    pstmt.executeUpdate();
                }
            }
        }
    }

    private static void fetchBooks(Connection conn) throws SQLException {
        String sql = "SELECT * FROM books";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String author = rs.getString("author");
                System.out.println(id + ": " + title + " by " + author);
            }
        }
    }
}
