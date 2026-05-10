package Library_Model;
import java.sql.*;
public class user_model {
    private Connection con;
    private Statement sta;
    private ResultSet res;
    private boolean flag=false;
    public String user_login(String user_id,String user_pass,String user_type)
    {
        String url="jdbc:mysql://localhost:3306/LMS";
        String id="root";
        String pass="root";
        String query="select * from LMS.user_table where "
    + "id='" + user_id + "' and "
    + "pass='" + user_pass + "' and "
    + "user_type='" + user_type + "'";
        String info ="";
        try{
       con =DriverManager.getConnection(url,id,pass);
       sta=con.createStatement();
       res=sta.executeQuery(query);
       while(res.next())
            {
                info=info+res.getInt("id")+res.getString("name")+res.getString("user_type")+"\n";
                flag=true;
            }
       res.close();
       sta.close();
       con.close();
        }
        catch(Exception e)
        {
          System.out.println(e);
        }
      return info;}

    public String user_login() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
