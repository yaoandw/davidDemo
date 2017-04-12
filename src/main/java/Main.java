
import java.sql.*;

public class Main {

    public static void main(String[] args) {
        // write your code here
        try{
            //调用Class.forName()方法加载驱动程序
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("成功加载MySQL驱动！");
        }catch(ClassNotFoundException e1){
            System.out.println("找不到MySQL驱动!");
            e1.printStackTrace();
        }

        String url="jdbc:mysql://127.0.0.1:3306/emojiTest";    //JDBC的URL
//        String url="jdbc:mysql://115.28.215.207:3306/wx_cpic?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true";    //JDBC的URL
        //调用DriverManager对象的getConnection()方法，获得一个Connection对象
        Connection conn;
        try {
            conn = DriverManager.getConnection(url,    "root","");
//            conn = DriverManager.getConnection(url,    "appman","app207@!b15");
            //创建一个Statement对象
            Statement stmt = conn.createStatement(); //创建Statement对象
            System.out.println("成功连接到数据库！");


            String sql = "SELECT * FROM emoji where id=1";    //要执行的SQL
            ResultSet rs = stmt.executeQuery(sql);//创建数据对象
            System.out.println("nick_name"+"\t"+"id");
            while (rs.next()){
                System.out.print(rs.getString("nick_name") + "\t");
                System.out.print(rs.getInt("id") + "\t");
                System.out.println();
            }

            //修改数据的代码
            String sql2 = "UPDATE emoji SET `nick_name`='abc\uD83D\uDE0A' WHERE `id`='1'";
            PreparedStatement pst = conn.prepareStatement(sql2);
//            pst.setString(1,"8888");
//            pst.setInt(2,198);
            pst.executeUpdate();



            String sql3 = "SELECT * FROM emoji where id=1";    //要执行的SQL
            ResultSet rs1 = stmt.executeQuery(sql);//创建数据对象
            System.out.println("nick_name"+"\t"+"id");
            while (rs1.next()){
                System.out.print(rs1.getString("nick_name") + "\t");
                System.out.print(rs1.getInt("id") + "\t");
                System.out.println();
            }


            rs.close();

            stmt.close();
            conn.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
