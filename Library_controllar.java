
package Library_Controllar;



public class Book_controllar {
    public String title;
    public String author;
    public String isbn;

    public Book_controllar(String title, String author, String isbn) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
    }

    public void displayBookInfo() {
        System.out.println("Book: " + title + " | Author: " + author + " | ISBN: " + isbn);
    }
}
