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
        if (actual.contains(expected)) {
            System.out.println("Искомый текст есть: " + expected);
        }
        else {
            System.err.println("Искомого текста нет: " + expected + " вместо него " + actual);
            driver.close();
        }
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

    public static void checkBoxCheck(WebElement checkbox){
        if(!checkbox.isSelected()){
            checkbox.click();
        }
    }

    public static void waitFieldisDisplayed(By locator) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, 10);
            wait.until((WebDriver d) -> d.findElement(locator).isDisplayed());
            return;
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }
        System.out.println("Поле не отображено");
    }

    public static boolean isElementPresented(By locator) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, 10);
            wait.until((WebDriver d) -> d.findElement(locator));
            return true;
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void click(By locator) {
        if (!isElementPresented(locator)) {
        }
        waitFieldisDisplayed(locator);
        driver.findElement(locator).click();
    }

    public static void scrollToElement(WebElement find){
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", find);
        Wait <WebDriver> wait = new WebDriverWait(driver, 20, 1000);
        wait.until(ExpectedConditions.visibilityOf(find));
    }

    public static void main(String[] args) {
        System.out.println("Тестовое задание для сайта РГС");
        setup();
        click(By.xpath("//ol/li/a[contains(text(),'Страхование')]"));

        click(By.xpath("//*[@class='hidden-xs'][contains(text(),'Путешествия')]"));

        click(By.xpath("//*[contains(text(), 'Страхование выезжающих за')]"));

        WebElement element = driver.findElement(By.xpath("//a[contains(text(), 'Рассчитать')]"));
        scrollToElement(element);
        click(By.xpath("//a[contains(text(), 'Рассчитать')]"));

        compareText(driver.findElement(By.xpath("//div/*[contains(text(), 'Страхование выезжающих')]")).getText(),"Страхование выезжающих за рубеж");

        WebElement element1 = driver.findElement(By.xpath("//button/*[contains(@class, 'content-title')]"));
        scrollToElement(element1);
        click(By.xpath("//button/*[contains(@class, 'content-title')]"));

        fillForm(By.xpath("//*[@id='Countries']"),"Шенген");
        WebElement element2 = driver.findElement(By.xpath("//*[@id='Countries']"));
        element2.sendKeys(Keys.DOWN,Keys.ENTER);

        new Select(driver.findElement(By.name("ArrivalCountryList"))).selectByVisibleText("Испания");

        driver.findElement(By.xpath("//input[@data-test-name='FirstDepartureDate']")).click();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        fillForm(By.xpath("//input[@data-test-name='FirstDepartureDate']"),setData());

        WebElement element3 = driver.findElement(By.xpath("//label[@class='btn btn-attention']"));
        scrollToElement(element3);
        click(By.xpath("//label[@class='btn btn-attention']"));

        fillForm(By.xpath("//input[@data-test-name='FullName']"),"IVANOV  IVAN");
        fillForm(By.xpath("//input[@data-test-name=\"BirthDate\"]"),"25101994");

        WebElement element4 = driver.findElement(By.xpath("//*[contains(text(), 'активный отдых или спорт')]/ancestor::div[@class=\"calc-vzr-toggle-risk-group\"]//div[@class=\"toggle off toggle-rgs\"]"));
        scrollToElement(element4);
        checkBoxCheck(element4);

        WebElement element5 = driver.findElement(By.xpath("//label[@class='adaptive-checkbox-label'][contains(text(),' Я согласен')]"));
        scrollToElement(element5);
        checkBoxCheck(element5);

        click(By.xpath("//button[@data-test-name='NextButton'][contains(text(),'Рассчитать')]"));

        waitFieldisDisplayed(By.xpath("//div[@class='program-name'][contains(text(),'Комфорт')]"));

        WebElement element6 = driver.findElement(By.xpath("//h2[@class='step-title'][contains(text(),'Расчет')]"));
        scrollToElement(element6);

        compareText(driver.findElement(By.xpath("//span[contains(text(),'Многократные поездки')]")).getText(),"Многократные поездки в течение года");
        compareText(driver.findElement(By.xpath("//strong[contains(text(),'Шенген')]")).getText(),"Шенген");
        compareText(driver.findElement(By.xpath("//strong[contains(text(),'IVANOV IVAN')]")).getText(),"IVANOV IVAN");
        compareText(driver.findElement(By.xpath("//strong[contains(text(),'25')]")).getText(),"25.10.1994");
        compareText(driver.findElement(By.xpath(" //div[@style=\"visibility: visible; opacity: 1; display: block; transform: translateX(0px);\"]//child::small[@data-bind=\"text: ko.unwrap('undefined' === typeof info ? '' : info)\"]")).getText(),"(включая активный отдых)");
        driver.close();
    }
}
