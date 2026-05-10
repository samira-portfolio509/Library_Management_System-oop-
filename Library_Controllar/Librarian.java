package Library_Controllar;

public class Librarian extends user_controllar {

    public Librarian(int id, String name) {
        super(id, name);
    }

    public void addBook(String title) {
        System.out.println("Librarian " + name + " registered book: " + title);
    }

    public void issueBook(String bookName, String userName) {
        System.out.println("Librarian " + name + " finalized issue of " + bookName + " to " + userName);
    }

    public void generateReport() {
        System.out.println("Librarian " + name + " is generating the daily library report...");
    }
}
