package main.java.com.lemon.temprature;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import org.openqa.selenium.chrome.ChromeDriver;

import org.openqa.selenium.chrome.ChromeOptions;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;


public abstract class BaseDemo {

    public static WebDriver driver = null;

    public static void init() {

        String OSInfo = System.getProperties().getProperty("os.name");
        System.setProperty("webdriver.chrome.driver", "src/chromedriver");
        System.out.println(OSInfo);
        ChromeOptions chromeOptions = new ChromeOptions();
        //获取当前系统类别
        if(!OSInfo.toLowerCase().contains("win")){
            chromeOptions.addArguments("no-sandbox");
            chromeOptions.addArguments("headless");
            chromeOptions.addArguments("window-size=1200x600");
            chromeOptions.addArguments("disable-gpu");
            chromeOptions.addArguments("disable-dev-shm-usage");
            System.out.println("无界面启动！！");
        }
        try {
            driver = new ChromeDriver(chromeOptions);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("驱动器初始化异常，严重！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！");
        }
    }
    public boolean isElementPresent(By by) {
        try {
            driver.findElement(by);
            return true;
        }catch (Exception e) {
            return false;
        }
    }

    public void load() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void quit() {
        load();
        driver.quit();
    }

    //体温未上报处理
    public boolean undo(String username) {

        try{
            double t1 = new Random().nextDouble() + 35.8;
            BigDecimal b = new BigDecimal(t1);
            double t2 = b.setScale(1, RoundingMode.UP).doubleValue();
            String temperature = String.valueOf(t2);

            driver.findElement(By.xpath("//*[@id=\"app\"]/div/button")).click();
            load();
            driver.findElement(By.xpath("//*[@id=\"app\"]/div/div[3]/div/ul/li[2]/ul/li[8]/div/div[2]/input")).sendKeys(temperature);
            driver.findElement(By.xpath("//*[@id=\"app\"]/div/div[3]/div/ul/li[3]/div/button")).click();

            System.out.println(username + "上报温度为：" + temperature);
            load();
            return false;
        }catch (Exception e){
            //中间点击出现异常，退出驱动器，重新进行初始化
            quit();
            System.out.println("中间出现异常！！!");
            return true;
        }
    }

    public abstract int test(String login,String password);

}
