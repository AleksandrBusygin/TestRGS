package com.aleksandr;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class Main {

    private static WebDriver driver;

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

        driver.findElement(By.xpath("//*[@class='hidden-xs'][contains(text(),'Путешествия')]")).click();

        driver.findElement(By.xpath("//*[contains(text(), 'Страхование выезжающих за')]")).click();

        WebElement element = driver.findElement(By.xpath("//a[contains(text(), 'Рассчитать')]"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();",element);
        driver.findElement(By.xpath("//a[contains(text(), 'Рассчитать')]")).click();

        compareText(driver.findElement(By.xpath("//div/*[contains(text(), 'Страхование выезжающих')]")).getText(),"Страхование выезжающих за рубеж");
//      здесь ломается
        JavascriptExecutor js = (JavascriptExecutor)driver;
        js.executeScript("scroll(0, 300);");
        Wait <WebDriver> wait = new WebDriverWait(driver, 10, 1000);
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//button/*[contains(@class, 'content-title')]"))));
        driver.findElement(By.xpath("//button/*[contains(@class, 'content-title')]")).click();


        fillForm(By.xpath("//*[@id='Countries']"),"Шенген");
        driver.findElement(By.xpath("//input[@class='form-control-multiple-autocomplete-actual-input tt-input collapsed']")).click();
        new Select(driver.findElement(By.name("ArrivalCountryList"))).selectByVisibleText("Испания");



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
