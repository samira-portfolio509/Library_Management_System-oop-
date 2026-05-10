
package Library_Controllar;


public class Teacher_controllar extends user_controllar {

    public Teacher_controllar(int id, String name) {
        super(id, name);
    }

    public void addBook(String title) {
        System.out.println("Teacher " + name + " added a new book: " + title);
    }

    public void issueBook(String bookName, String studentName) {
        System.out.println("Teacher " + name + " issued " + bookName + " to " + studentName);
    }
}
