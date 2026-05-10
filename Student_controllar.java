/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package Library_Controllar;

/**
 *
 * @author DELL
 */

public class Student_controllar extends user_controllar {
    public String dept;

    public Student_controllar(int id, String name, String dept) {
        super(id, name);
        this.dept = dept;
    }

    public void borrowBook(String bookName) {
        System.out.println("Student " + name + " from " + dept + " dept requested to borrow: " + bookName);
    }
}

