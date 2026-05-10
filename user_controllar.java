
package Library_Controllar;
import Library_Model.user_model;
import java.util.Scanner;

public class user_controllar extends user_model {
    public int id;
    public String name;
    private String user_id,user_name,user_pass,user_email,user_type;
    private String flag;
    public user_controllar(){
        
    }
    public boolean user_login_cont(String user_id,String user_pass, String user_type)
    {
        Scanner input= new Scanner(System.in);
        //System.out.println("Enter User Id:");
        this.user_id=user_id;//input.nextLine();
        //System.out.println("Enter User password:");

        this.user_pass=user_pass;//input.nextLine();
        //System.out.println("Enter User Type:");

        this.user_type=user_type;//input.nextLine();
         flag= user_login(user_id,user_pass,user_type);
//        if(flag==true)
//        {
//          
//         
//            System.out.println("Successfully Login");
//             return true;
//        }
//        else
//        {
//            System.out.println("password or Id are not match");
//        }
           return true;
//    }

    /**
     *
     * @param id
     * @param name
     */
    }
    public user_controllar(int id, String name) {
        this.id = id;
        this.name = name;
    
}
}


