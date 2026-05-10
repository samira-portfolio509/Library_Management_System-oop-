package Library_Controllar;

import Library_Model.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Book_Controller - Handles all book-related operations
 * Interacts with the MySQL database through DatabaseConnection
 * 
 * @author Samira
 */
public class Book_Controller {
    
    private DatabaseConnection dbConnection;
    
    /**
     * Constructor - Initialize database connection
     */
    public Book_Controller() {
        this.dbConnection = DatabaseConnection.getInstance();
    }
    
    /**
     * Add a new book to the database
     * 
     * @param title Book title
     * @param author Book author
     * @param isbn Book ISBN
     * @param publisher Book publisher
     * @param publicationYear Publication year
     * @param category Book category
     * @param totalCopies Total number of copies
     * @return true if book added successfully, false otherwise
     */
    public boolean addBook(String title, String author, String isbn, String publisher, 
                          int publicationYear, String category, int totalCopies) {
        
        // Validation
        if (title == null || title.trim().isEmpty() || author == null || author.trim().isEmpty()) {
            System.err.println("✗ Book title and author cannot be empty!");
            return false;
        }
        
        String query = "INSERT INTO books (title, author, isbn, publisher, publication_year, category, total_copies, available_copies) " +
                      "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try {
            PreparedStatement stmt = dbConnection.prepareStatement(query);
            stmt.setString(1, title);
            stmt.setString(2, author);
            stmt.setString(3, isbn != null ? isbn : "");
            stmt.setString(4, publisher != null ? publisher : "");
            stmt.setInt(5, publicationYear);
            stmt.setString(6, category != null ? category : "General");
            stmt.setInt(7, totalCopies);
            stmt.setInt(8, totalCopies); // Available copies = total copies initially
            
            int result = stmt.executeUpdate();
            stmt.close();
            
            if (result > 0) {
                System.out.println("✓ Book added successfully: " + title);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("✗ Error adding book: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Get all books from database
     * 
     * @return List of BookData objects containing all books
     */
    public List<BookData> getAllBooks() {
        List<BookData> books = new ArrayList<>();
        String query = "SELECT * FROM books ORDER BY book_id DESC";
        
        try {
            ResultSet rs = dbConnection.executeQuery(query);
            
            while (rs.next()) {
                BookData book = new BookData(
                    rs.getInt("book_id"),
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getString("isbn"),
                    rs.getString("publisher"),
                    rs.getInt("publication_year"),
                    rs.getString("category"),
                    rs.getInt("total_copies"),
                    rs.getInt("available_copies"),
                    rs.getString("status")
                );
                books.add(book);
            }
            
            System.out.println("✓ Retrieved " + books.size() + " books from database");
            
        } catch (SQLException e) {
            System.err.println("✗ Error retrieving all books: " + e.getMessage());
        }
        
        return books;
    }
    
    /**
     * Get a specific book by ID
     * 
     * @param bookId Book ID to search
     * @return BookData object if found, null otherwise
     */
    public BookData getBookById(int bookId) {
        String query = "SELECT * FROM books WHERE book_id = ?";
        
        try {
            PreparedStatement stmt = dbConnection.prepareStatement(query);
            stmt.setInt(1, bookId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                BookData book = new BookData(
                    rs.getInt("book_id"),
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getString("isbn"),
                    rs.getString("publisher"),
                    rs.getInt("publication_year"),
                    rs.getString("category"),
                    rs.getInt("total_copies"),
                    rs.getInt("available_copies"),
                    rs.getString("status")
                );
                rs.close();
                stmt.close();
                return book;
            }
            
            rs.close();
            stmt.close();
            
        } catch (SQLException e) {
            System.err.println("✗ Error retrieving book by ID: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Search books by title or author
     * 
     * @param searchTerm Search keyword
     * @return List of matching BookData objects
     */
    public List<BookData> searchBooks(String searchTerm) {
        List<BookData> books = new ArrayList<>();
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return books;
        }
        
        String query = "SELECT * FROM books WHERE title LIKE ? OR author LIKE ? ORDER BY title";
        
        try {
            PreparedStatement stmt = dbConnection.prepareStatement(query);
            String searchPattern = "%" + searchTerm + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                BookData book = new BookData(
                    rs.getInt("book_id"),
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getString("isbn"),
                    rs.getString("publisher"),
                    rs.getInt("publication_year"),
                    rs.getString("category"),
                    rs.getInt("total_copies"),
                    rs.getInt("available_copies"),
                    rs.getString("status")
                );
                books.add(book);
            }
            
            System.out.println("✓ Found " + books.size() + " books matching: " + searchTerm);
            rs.close();
            stmt.close();
            
        } catch (SQLException e) {
            System.err.println("✗ Error searching books: " + e.getMessage());
        }
        
        return books;
    }
    
    /**
     * Update book information
     * 
     * @param bookId Book ID to update
     * @param title New title
     * @param author New author
     * @param publisher New publisher
     * @param category New category
     * @return true if update successful, false otherwise
     */
    public boolean updateBook(int bookId, String title, String author, String publisher, String category) {
        
        if (title == null || title.trim().isEmpty()) {
            System.err.println("✗ Book title cannot be empty!");
            return false;
        }
        
        String query = "UPDATE books SET title = ?, author = ?, publisher = ?, category = ?, updated_date = NOW() WHERE book_id = ?";
        
        try {
            PreparedStatement stmt = dbConnection.prepareStatement(query);
            stmt.setString(1, title);
            stmt.setString(2, author != null ? author : "");
            stmt.setString(3, publisher != null ? publisher : "");
            stmt.setString(4, category != null ? category : "General");
            stmt.setInt(5, bookId);
            
            int result = stmt.executeUpdate();
            stmt.close();
            
            if (result > 0) {
                System.out.println("✓ Book updated successfully: " + title);
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("✗ Error updating book: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Delete a book from database
     * 
     * @param bookId Book ID to delete
     * @return true if delete successful, false otherwise
     */
    public boolean deleteBook(int bookId) {
        String query = "DELETE FROM books WHERE book_id = ?";
        
        try {
            PreparedStatement stmt = dbConnection.prepareStatement(query);
            stmt.setInt(1, bookId);
            
            int result = stmt.executeUpdate();
            stmt.close();
            
            if (result > 0) {
                System.out.println("✓ Book deleted successfully. ID: " + bookId);
                return true;
            } else {
                System.err.println("✗ No book found with ID: " + bookId);
            }
            
        } catch (SQLException e) {
            System.err.println("✗ Error deleting book: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Update book status (Available, Issued, Damaged, Lost)
     * 
     * @param bookId Book ID
     * @param status New status
     * @return true if status updated successfully
     */
    public boolean updateBookStatus(int bookId, String status) {
        String query = "UPDATE books SET status = ?, updated_date = NOW() WHERE book_id = ?";
        
        try {
            PreparedStatement stmt = dbConnection.prepareStatement(query);
            stmt.setString(1, status);
            stmt.setInt(2, bookId);
            
            int result = stmt.executeUpdate();
            stmt.close();
            
            if (result > 0) {
                System.out.println("✓ Book status updated to: " + status);
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("✗ Error updating book status: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Update available copies when book is issued or returned
     * 
     * @param bookId Book ID
     * @param quantity Change in quantity (negative for issue, positive for return)
     * @return true if update successful
     */
    public boolean updateAvailableCopies(int bookId, int quantity) {
        String query = "UPDATE books SET available_copies = available_copies + ? WHERE book_id = ? AND available_copies + ? >= 0";
        
        try {
            PreparedStatement stmt = dbConnection.prepareStatement(query);
            stmt.setInt(1, quantity);
            stmt.setInt(2, bookId);
            stmt.setInt(3, quantity);
            
            int result = stmt.executeUpdate();
            stmt.close();
            
            if (result > 0) {
                System.out.println("✓ Available copies updated for book ID: " + bookId);
                return true;
            } else {
                System.err.println("✗ Cannot update copies (not enough available or book not found)");
            }
            
        } catch (SQLException e) {
            System.err.println("✗ Error updating available copies: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Get available books
     * 
     * @return List of available BookData objects
     */
    public List<BookData> getAvailableBooks() {
        List<BookData> books = new ArrayList<>();
        String query = "SELECT * FROM books WHERE available_copies > 0 AND status = 'Available' ORDER BY title";
        
        try {
            ResultSet rs = dbConnection.executeQuery(query);
            
            while (rs.next()) {
                BookData book = new BookData(
                    rs.getInt("book_id"),
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getString("isbn"),
                    rs.getString("publisher"),
                    rs.getInt("publication_year"),
                    rs.getString("category"),
                    rs.getInt("total_copies"),
                    rs.getInt("available_copies"),
                    rs.getString("status")
                );
                books.add(book);
            }
            
            System.out.println("✓ Retrieved " + books.size() + " available books");
            
        } catch (SQLException e) {
            System.err.println("✗ Error retrieving available books: " + e.getMessage());
        }
        
        return books;
    }
    
    /**
     * Inner class to represent Book data
     */
    public static class BookData {
        public int bookId;
        public String title;
        public String author;
        public String isbn;
        public String publisher;
        public int publicationYear;
        public String category;
        public int totalCopies;
        public int availableCopies;
        public String status;
        
        public BookData(int bookId, String title, String author, String isbn, String publisher,
                       int publicationYear, String category, int totalCopies, int availableCopies, String status) {
            this.bookId = bookId;
            this.title = title;
            this.author = author;
            this.isbn = isbn;
            this.publisher = publisher;
            this.publicationYear = publicationYear;
            this.category = category;
            this.totalCopies = totalCopies;
            this.availableCopies = availableCopies;
            this.status = status;
        }
        
        @Override
        public String toString() {
            return title + " by " + author + " (Available: " + availableCopies + "/" + totalCopies + ")";
        }
    }
}
