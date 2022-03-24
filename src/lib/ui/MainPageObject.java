package lib.ui;

import java.util.concurrent.TimeUnit;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.openqa.selenium.support.ui.ExpectedConditions.invisibilityOfElementLocated;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

public class MainPageObject {
    protected AppiumDriver driver;
    private static final int DEFAULT_WAIT_TIMEOUT = 5;

    public MainPageObject(AppiumDriver driver) {
        this.driver = driver;
    }

    public void assertElementHasText(By by, String expectedText) {
        String actualText = getElementText(by);

        Assert.assertEquals("Actual text did not match expected text", expectedText, actualText);
    }

    public void assertElementContainsString(WebElement el, String s) {
        Assert.assertTrue(String.format(
                "Element's text \"%s\" didn't contain string \"%s\"", el.getText(), s
        ), el.getText().contains(s));
    }

    public void assertElementPresent(By by, String errorMessage, int timeoutSeconds) {
        driver.manage().timeouts().implicitlyWait(timeoutSeconds, TimeUnit.SECONDS);
        final boolean elementIsPresent = driver.findElements(by).size() > 0;
        driver.manage().timeouts().implicitlyWait(DEFAULT_WAIT_TIMEOUT, TimeUnit.SECONDS);

        Assert.assertTrue(errorMessage, elementIsPresent);
    }

    public void waitForElementAndClick(By by, String errorMessage) {
        waitForElementAndClick(by, errorMessage, DEFAULT_WAIT_TIMEOUT);
    }

    public void waitForElementAndClick(By by, String errorMessage, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, timeoutSeconds);
        wait
                .withMessage(errorMessage)
                .until(visibilityOfElementLocated(by))
                .click();
    }

    public void waitForElementAndSetValue(By by, String value, String errorMessage) {
        waitForElementAndSetValue(by, value, errorMessage, DEFAULT_WAIT_TIMEOUT);
    }

    public void waitForElementAndSetValue(By by, String value, String errorMessage, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, timeoutSeconds);
        MobileElement el = (MobileElement) wait
                .withMessage(errorMessage)
                .until(visibilityOfElementLocated(by));
        el.setValue(value);
    }

    public WebElement waitForElementPresent(By by, String errorMessage) {
        return waitForElementPresent(by, errorMessage, DEFAULT_WAIT_TIMEOUT);
    }

    public WebElement waitForElementPresent(By by, String errorMessage, long timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
        return wait
                .withMessage(errorMessage + "\n")
                .until(presenceOfElementLocated(by));
    }

    public boolean waitForInvisibilityOfElement(By by, String errorMessage) {
        return waitForInvisibilityOfElement(by, errorMessage, DEFAULT_WAIT_TIMEOUT);
    }

    public boolean waitForInvisibilityOfElement(By by, String errorMessage, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, timeoutSeconds);
        return wait
                .withMessage(errorMessage)
                .until(invisibilityOfElementLocated(by));
    }

    public String getElementText(By by) {
        return waitForElementPresent(by, "Could not get article's title").getText();
    }

    public void swipeElementLeft(By by, String errorMessage) {
        WebDriverWait wait = new WebDriverWait(driver, DEFAULT_WAIT_TIMEOUT);
        WebElement el = wait
                .withMessage(errorMessage)
                .until(presenceOfElementLocated(by));
        int leftX = el.getLocation().getX();
        int rightX = leftX + el.getSize().getWidth();
        int upperY = el.getLocation().getY();
        int lowerY = upperY + el.getSize().getHeight();
        int middleY = (upperY + lowerY) / 2;
        TouchAction action = new TouchAction(driver);
        action
                .press(rightX, middleY)
                .waitAction(300)
                .moveTo(leftX, middleY)
                .release()
                .perform();
    }
}
