package Library_Model;

import java.sql.*;

/**
 * DatabaseConnection - Singleton pattern for MySQL database connection
 * Handles all database connectivity for the Library Management System
 * 
 * @author Samira
 */
public class DatabaseConnection {
    
    // Private static instance of DatabaseConnection
    private static DatabaseConnection instance;
    
    // Connection object
    private Connection connection;
    
    // Database configuration
    private static final String DB_URL = "jdbc:mysql://localhost:3306/LMS";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";
    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    
    // Connection timeout in seconds
    private static final int CONNECTION_TIMEOUT = 30;
    
    /**
     * Private constructor to prevent instantiation from outside
     */
    private DatabaseConnection() {
        this.connection = null;
    }
    
    /**
     * Get singleton instance of DatabaseConnection
     * Thread-safe implementation using synchronized block
     * 
     * @return DatabaseConnection instance
     */
    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }
    
    /**
     * Establish connection to MySQL database
     * Uses lazy initialization - connection is created only when needed
     * 
     * @return Connection object
     * @throws SQLException if connection fails
     */
    public Connection connect() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                // Load MySQL JDBC Driver
                Class.forName(DB_DRIVER);
                
                // Set connection timeout properties
                java.util.Properties properties = new java.util.Properties();
                properties.setProperty("user", DB_USER);
                properties.setProperty("password", DB_PASSWORD);
                properties.setProperty("connectTimeout", String.valueOf(CONNECTION_TIMEOUT * 1000));
                properties.setProperty("autoReconnect", "true");
                properties.setProperty("useSSL", "false");
                properties.setProperty("allowPublicKeyRetrieval", "true");
                
                // Establish connection
                connection = DriverManager.getConnection(DB_URL, properties);
                System.out.println("✓ Database connection established successfully!");
                
            } catch (ClassNotFoundException e) {
                System.err.println("✗ MySQL JDBC Driver not found!");
                System.err.println("  Error: " + e.getMessage());
                throw new SQLException("MySQL JDBC Driver not found. Please add mysql-connector-java to your classpath.", e);
                
            } catch (SQLException e) {
                System.err.println("✗ Failed to establish database connection!");
                System.err.println("  Error: " + e.getMessage());
                System.err.println("  Details: Make sure MySQL is running and credentials are correct.");
                throw new SQLException("Unable to connect to database at " + DB_URL, e);
            }
        }
        
        return connection;
    }
    
    /**
     * Get current database connection
     * Ensures connection is active before returning
     * 
     * @return Connection object
     * @throws SQLException if connection is not available
     */
    public Connection getConnection() throws SQLException {
        return connect();
    }
    
    /**
     * Close database connection
     * Safely closes the connection and handles exceptions
     */
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                System.out.println("✓ Database connection closed successfully!");
            } catch (SQLException e) {
                System.err.println("✗ Error closing database connection:");
                System.err.println("  " + e.getMessage());
            }
        }
    }
    
    /**
     * Check if connection is active
     * 
     * @return true if connection is active and not closed
     */
    public boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            System.err.println("✗ Error checking connection status: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Execute a SELECT query
     * 
     * @param query SQL SELECT query
     * @return ResultSet containing query results
     * @throws SQLException if query execution fails
     */
    public ResultSet executeQuery(String query) throws SQLException {
        if (!isConnected()) {
            connect();
        }
        
        try {
            Statement statement = connection.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY
            );
            return statement.executeQuery(query);
        } catch (SQLException e) {
            System.err.println("✗ Error executing SELECT query:");
            System.err.println("  Query: " + query);
            System.err.println("  Error: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Execute an INSERT, UPDATE, or DELETE query
     * 
     * @param query SQL INSERT/UPDATE/DELETE query
     * @return number of affected rows
     * @throws SQLException if query execution fails
     */
    public int executeUpdate(String query) throws SQLException {
        if (!isConnected()) {
            connect();
        }
        
        try {
            Statement statement = connection.createStatement();
            int result = statement.executeUpdate(query);
            statement.close();
            return result;
        } catch (SQLException e) {
            System.err.println("✗ Error executing UPDATE query:");
            System.err.println("  Query: " + query);
            System.err.println("  Error: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Execute a prepared statement (safe against SQL injection)
     * 
     * @param query SQL query with placeholders
     * @param parameters values for placeholders
     * @return PreparedStatement ready to execute
     * @throws SQLException if statement creation fails
     */
    public PreparedStatement prepareStatement(String query) throws SQLException {
        if (!isConnected()) {
            connect();
        }
        
        try {
            return connection.prepareStatement(query);
        } catch (SQLException e) {
            System.err.println("✗ Error preparing statement:");
            System.err.println("  Query: " + query);
            System.err.println("  Error: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Test database connection
     * Used for debugging and connection verification
     * 
     * @return true if connection is successful
     */
    public boolean testConnection() {
        try {
            Connection testConn = connect();
            Statement stmt = testConn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT 1");
            boolean result = rs.next();
            rs.close();
            stmt.close();
            System.out.println("✓ Database connection test successful!");
            return result;
        } catch (SQLException e) {
            System.err.println("✗ Database connection test failed:");
            System.err.println("  " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Graceful shutdown - closes connection when application exits
     */
    @Override
    protected void finalize() throws Throwable {
        closeConnection();
        super.finalize();
    }
}
