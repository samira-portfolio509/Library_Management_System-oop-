
package Library_Controllar;

import Library_View.User_View;


public class main {
    public static void main(String[] args) {
        user_controllar oj=new user_controllar();
       
        User_View view=new User_View();
        view.setVisible(true);
        Book_controllar myBook = new Book_controllar("Java (oop)", "Samira", "123-ABC");
        Student_controllar student = new Student_controllar(12341, "Sumaiya", "CSE");
        student.borrowBook(myBook.title);
        Librarian librarian = new Librarian(1001, "Fatima");
        librarian.addBook(myBook.title);
        librarian.generateReport();
        
    }
}
