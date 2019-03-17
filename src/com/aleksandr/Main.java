package com.aleksandr;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Main {

    private static WebDriver driver;

    public static void setup() {
        System.setProperty("webdriver.chrome.driver", "drv/chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver.get("https://www.rgs.ru");
    }
    public static void compareText(String actual, String expected){
        if (actual.contains(expected))
        {
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
    public static String setData(){
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd.MM.yyyy");
        Calendar i = Calendar.getInstance();
        i.setTime(new Date());
        i.add(Calendar.DAY_OF_MONTH, 14);
        String newDate = formatForDateNow.format(i.getTime());
        return newDate;
    }

    public static void main(String[] args) {
        System.out.println("Тестовое задание для сайта РГС");
        setup();
        String name ="IVANOV IVAN";
        String dateOfBirth = "25101994";

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
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//span[contains(text(), 'Несколько')]"))));
        driver.findElement(By.xpath("//span[contains(text(), 'Несколько')]")).click();
        //button/*[contains(@class, 'content-title')] можно эту кнопку, но и с ней не работает

        fillForm(By.xpath("//*[@id='Countries']"),"Шенген");

        driver.findElement(By.xpath("//input[@class='form-control-multiple-autocomplete-actual-input tt-input collapsed']")).click();
        new Select(driver.findElement(By.name("ArrivalCountryList"))).selectByVisibleText("Испания");

        driver.findElement(By.xpath("//input[@data-test-name='FirstDepartureDate']")).click();

        fillForm(By.xpath("//input[@data-test-name='FirstDepartureDate']"),setData());

        driver.findElement(By.xpath("//label[@class='btn btn-attention active']")).click();

        fillForm(By.xpath("//div[@data-fi-input-mode='combined']"),name);
        fillForm(By.xpath("//input[@data-test-name=\"BirthDate\"]"),dateOfBirth);

        driver.findElement(By.xpath("//*[contains(text(), 'активный отдых или спорт')]/ancestor::div[@class=\"calc-vzr-toggle-risk-group\"]//div[@class=\"toggle off toggle-rgs\"]")).click();

        driver.findElement(By.xpath("//input[@data-test-name='IsProcessingPersonalDataToCalculate']")).click();

        driver.findElement(By.xpath("//button[@data-test-name='NextButton'][contains(text(),'Рассчитать')]")).click();

        compareText(driver.findElement(By.xpath("//span[contains(text(),'Многократные поездки')]")).getText(),"Многократные поездки в течение года");
        compareText(driver.findElement(By.xpath("//strong[contains(text(),'Шенген')]")).getText(),"Шенген");
        compareText(driver.findElement(By.xpath("//strong[contains(text(),'IVANOV IVAN')]")).getText(),name);
        compareText(driver.findElement(By.xpath("//strong[contains(text(),'25')]")).getText(),dateOfBirth);
        compareText(driver.findElement(By.xpath(" //div[@style=\"visibility: visible; opacity: 1; display: block; transform: translateX(0px);\"]//child::small[@data-bind=\"text: ko.unwrap('undefined' === typeof info ? '' : info)\"]")).getText(),"(включая активный отдых)");

        driver.close();
    }

}
