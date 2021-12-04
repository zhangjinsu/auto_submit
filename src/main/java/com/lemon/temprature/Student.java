package main.java.com.lemon.temprature;


import main.java.com.lemon.dao.StudentDao;
import main.java.com.lemon.utils.JdbcUtils;
import org.openqa.selenium.By;

import java.sql.*;
import java.util.concurrent.TimeUnit;


/**
 * @author Aupt
 * @create 2021-10-14-8:46
 */
public class Student extends BaseDemo {

    private boolean flag = false;

    public void start(){
        Connection conn = null;
        Statement stm = null;
        ResultSet rs = null;

        try {
            conn = JdbcUtils.getConnection();
            stm = conn.createStatement();
            String sql = "select * from student1";
            rs = stm.executeQuery(sql);
            while (rs.next()) {
                String username = rs.getString("username");
                String password = rs.getString("password");
                System.out.println(username + " " + password);
                //初始化
                init();
                //传入用户名和密码
                int code = test(username, password);

                if (code == 1){ //登录成功
                    //判断是否上报完毕
                    if(isElementPresent(By.xpath("//*[@id=\"app\"]/div/div[2]/div[1]/p"))) {
                        //已经上报，退出
                        System.out.println("已上报，直接退出");
                        quit();
                    }else if(isElementPresent(By.xpath("//*[@id=\"app\"]/div/button"))) {
                        //没有上报，做上报操作
                        System.out.println("没有上报，上报体温");
                        flag = undo(username);  //如果返回true，说明出现了异常
                        if (flag) {
                            quit();
                            System.out.println("体温上报失败！！");
                            break;
                        }else {
                            quit();
                            System.out.println(username + "  体温上报完毕");
                        }
                    }else {
                        quit();
                        System.out.println("登录成功之后的异常情况");
                        load();
                        //异常情况下，将flag置为true
                        flag = true;
                        break;
                    }
                }else if (code == 0){  //登录失败，删除非法用户
                    continue;
                }else {
                    //异常情况下，继续初始化直至上报完毕
                    quit();
                    flag = true;  //出现异常，进行标记
                    System.out.println("异常情况,需要重新初始化");
                    load();
                    break;
                }
            }
            //出现异常之后，重新进行初始化
            if (flag) {
                System.out.println("发生了异常，初始化开始");
                start();
            }
        } catch (SQLException e) {
            System.out.println("数据库连接超时，请检查数据库服务器!");
            e.printStackTrace();
        }finally {
            JdbcUtils.close(rs,stm,conn);
        }
    }

    @Override
    public int test(String username, String password) {

        try{
            driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
            driver.get("http://hmgr.sec.lit.edu.cn/web/#/login");
//            driver.get("http://www.google.com");
            driver.findElement(By.xpath("//*[@id=\"app\"]/div/div/div/div[1]/div[2]/div/input")).sendKeys(username);
            driver.findElement(By.xpath("//*[@id=\"app\"]/div/div/div/div[2]/div[2]/div/input")).sendKeys(password);
            driver.findElement(By.xpath("//*[@id=\"app\"]/div/div/button")).click();

            load();
            if (isElementPresent(By.xpath("/html/body/div[2]/div"))) {
                System.out.println("用户名或密码错误");
                quit();
                int i = StudentDao.delete(username);
                if (i > 0) {
                    System.out.println("已经删除非法用户!");
                    load();
                    return 0;
                }
            }else {
                load();
                driver.findElement(By.xpath("//*[@id=\"app\"]/div/div/ul/div[1]/div[2]/li[1]")).click();
                load();
                return 1;
            }
        }catch (Exception e){
            quit();
            System.out.println("警告！！访问超时!!");
//            e.printStackTrace();
        }
        return -1;
    }
}
