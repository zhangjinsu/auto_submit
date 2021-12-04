package main.java.com.lemon.utils;

import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Properties;

/*
    JDBC通用工具类
 */
public class JdbcUtils {

    private static String url;
    private static String user;
    private static String password;
    private static String driver;

    static {
        //读取资源文件，获取值
        // 1.Properties集合类
       /* Properties pro = new Properties();
        //获取src路径下的文件的方式--->ClassLoader 类加载器
        ClassLoader classLoader = JdbcUtils.class.getClassLoader();
        URL resource = classLoader.getResource("jdbc.properties");
        String path = resource.getPath();
        System.out.println(path);*/
        //2.加载文件
        try {
            //3.获取数据，赋值
           /* pro.load(new FileReader(path));
            url = pro.getProperty("url");
            user = pro.getProperty("user");
            password = pro.getProperty("password");
            driver = pro.getProperty("driver");*/
            //4.加载驱动

            url = "jdbc:mysql://localhost:3306/temperature?serverTimezone=UTC";
            user = "root";
            password = "123456";
            driver = "com.mysql.cj.jdbc.Driver";
            Class.forName(driver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
       return DriverManager.getConnection(url,user,password);
    }

    public static void close(Statement stmt,Connection conn){
        if(null != stmt){
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(null != conn){
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public static void close(ResultSet rs, Statement stmt, Connection conn){

        if(null != rs){
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if(null != stmt){
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(null != conn){
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
