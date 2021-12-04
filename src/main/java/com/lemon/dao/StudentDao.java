package main.java.com.lemon.dao;

import main.java.com.lemon.domain.Student;
import main.java.com.lemon.utils.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Aupt
 * @create 2021-10-17-20:56
 */
public class StudentDao {

    public static int delete(String username){
        Connection conn = null;
        PreparedStatement pstm = null;

        try {
            conn = JdbcUtils.getConnection();
            String sql = "delete from student1 where username = ?";
            pstm = conn.prepareStatement(sql);
            pstm.setString(1,username);
            int i = pstm.executeUpdate();
            return i;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            JdbcUtils.close(pstm,conn);
        }
        return 0;
    }

    public static List<Student> findAll(){
        List<Student> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            conn = JdbcUtils.getConnection();
            String sql = "select * from student";
            pstm = conn.prepareStatement(sql);
            rs = pstm.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String username = rs.getString("username");
                String password = rs.getString("password");
                Student student = new Student(id,username,password);
                list.add(student);
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.close(rs,pstm,conn);
        }
        return null;
    }
}
