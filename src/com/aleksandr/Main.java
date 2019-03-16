package com.aleksandr;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

public class Main {

    private static WebDriver driver;
    private WebDriverWait wait;

    public static void setup() {
        System.setProperty("webdriver.chrome.driver", "drv/chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver.get("https://www.rgs.ru");
    }
    public static void main(String[] args) {
        System.out.println("Тестовое задание для сайта РГС");
        setup();

        driver.findElement(By.xpath("//ol/li/a[contains(text(),'Страхование')]")).click();

        driver.findElement(By.xpath("//*[@id=\"rgs-main-menu-insurance-dropdown\"]/div[1]/div[1]/div/div[2]/div[2]/div/a[2]")).click();
        driver.findElement(By.xpath("//*[contains(text(), 'Страхование выезжающих за')]")).click();

        WebElement element = driver.findElement(By.xpath("//a[contains(text(), 'Рассчитать')]"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();",element);

        driver.findElement(By.xpath("//a[contains(text(), 'Рассчитать')]")).click();

        compareText(driver.findElement(By.xpath("//div/*[contains(text(), 'Страхование выезжающих')]")).getText(),"Страхование выезжающих за рубеж");

        WebElement element1 = driver.findElement(By.xpath("//*[@id=\"calc-vzr-steps\"]/myrgs-steps-partner-auth/div[1]/div/div/div[1]/div[2]/div/div[1]/div/form/div[1]/btn-radio-group/div/button[2]"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();",element1);
        driver.findElement(By.xpath("//*[@id=\"calc-vzr-steps\"]/myrgs-steps-partner-auth/div[1]/div/div/div[1]/div[2]/div/div[1]/div/form/div[1]/btn-radio-group/div/button[2]")).click();

        fillForm(By.xpath("//*[@id=\"Countries\"]"),"Шенген");

        driver.close();


    }
    public static void compareText(String actual, String expected){
        if (actual.contains(expected)){
            System.out.println("Искомый текст есть: " + expected);
        }
        else System.err.println("Искомого текста нет: " + expected + "вместо него" + actual);
        driver.close();
    }
    public static void fillForm(By locator, String text){
        driver.findElement(locator).click();
        driver.findElement(locator).clear();
        driver.findElement(locator).sendKeys(text);
    }
}
