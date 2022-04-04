package lib.ui;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import java.util.List;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.openqa.selenium.support.ui.ExpectedConditions.invisibilityOfElementLocated;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;
import static org.openqa.selenium.support.ui.ExpectedConditions.urlContains;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

public class MainPageObject {
    protected AppiumDriver driver;
    private static final int DEFAULT_WAIT_TIMEOUT = 5;

    public MainPageObject(AppiumDriver driver) {
        this.driver = driver;
    }

    public void assertElementHasText(String locator, String expectedText) {
        String actualText = getElementText(locator);

        Assert.assertEquals("Actual text did not match expected text", expectedText, actualText);
    }

    public void assertElementContainsString(WebElement el, String s) {
        Assert.assertTrue(String.format(
                "Element's text \"%s\" didn't contain string \"%s\"", el.getText(), s
        ), el.getText().contains(s));
    }

    public void assertElementPresent(String locator, String errorMessage, int timeoutSeconds) {
        By by = getLocatorByString(locator);
        driver.manage().timeouts().implicitlyWait(timeoutSeconds, TimeUnit.SECONDS);
        final boolean elementIsPresent = driver.findElements(by).size() > 0;
        driver.manage().timeouts().implicitlyWait(DEFAULT_WAIT_TIMEOUT, TimeUnit.SECONDS);

        Assert.assertTrue(errorMessage, elementIsPresent);
    }

    public void waitForElementAndClick(String locator, String errorMessage) {
        waitForElementAndClick(locator, errorMessage, DEFAULT_WAIT_TIMEOUT);
    }

    public void waitForElementAndClick(String locator, String errorMessage, int timeoutSeconds) {
        By by = getLocatorByString(locator);
        WebDriverWait wait = new WebDriverWait(driver, timeoutSeconds);
        wait
                .withMessage(errorMessage)
                .until(visibilityOfElementLocated(by))
                .click();
    }

    public void waitForElementAndSetValue(String locator, String value, String errorMessage) {
        waitForElementAndSetValue(locator, value, errorMessage, DEFAULT_WAIT_TIMEOUT);
    }

    public void waitForElementAndSetValue(String locator, String value, String errorMessage, int timeoutSeconds) {
        By by = getLocatorByString(locator);
        WebDriverWait wait = new WebDriverWait(driver, timeoutSeconds);
        MobileElement el = (MobileElement) wait
                .withMessage(errorMessage)
                .until(visibilityOfElementLocated(by));
        el.setValue(value);
    }

    public WebElement waitForElementPresent(String locator, String errorMessage) {
        return waitForElementPresent(locator, errorMessage, DEFAULT_WAIT_TIMEOUT);
    }

    public WebElement waitForElementPresent(String locator, String errorMessage, long timeoutInSeconds) {
        By by = getLocatorByString(locator);
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
        return wait
                .withMessage(errorMessage + "\n")
                .until(presenceOfElementLocated(by));
    }

    public boolean waitForInvisibilityOfElement(String locator, String errorMessage) {
        return waitForInvisibilityOfElement(locator, errorMessage, DEFAULT_WAIT_TIMEOUT);
    }

    public boolean waitForInvisibilityOfElement(String locator, String errorMessage, int timeoutSeconds) {
        By by = getLocatorByString(locator);
        WebDriverWait wait = new WebDriverWait(driver, timeoutSeconds);
        return wait
                .withMessage(errorMessage)
                .until(invisibilityOfElementLocated(by));
    }

    public String getElementText(String locator) {
        return waitForElementPresent(locator, "Could not get article's title").getText();
    }

    public void swipeElementLeft(String locator, String errorMessage) {
        By by = getLocatorByString(locator);
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
                .press(PointOption.point(rightX, middleY))
                .waitAction(WaitOptions.waitOptions(Duration.ofMillis(300)))
                .moveTo(PointOption.point(leftX, middleY))
                .release()
                .perform();
    }

    public boolean checkElementPresent(String locator, int timeoutSeconds) {
        driver.manage().timeouts().implicitlyWait(timeoutSeconds, TimeUnit.SECONDS);
        final boolean isPresent = driver.findElements(getLocatorByString(locator)).size() > 0;
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        return isPresent;
    }

    public List<WebElement> getListOfElements(String locator) {
        By by = getLocatorByString(locator);
        return driver.findElements(by);
    }

    private By getLocatorByString(String locatorWithType) {
        String[] splitLocator = locatorWithType.split(Pattern.quote(":"), 2);
        String byType = splitLocator[0];
        String locator = splitLocator[1];

        if (byType.equals("xpath")) {
            return By.xpath(locator);
        } else if (byType.equals("id")) {
            return By.id(locator);
        } else {
            throw new IllegalArgumentException(
                    "Could not get type of locator. Locator " + locator
            );
        }
    }
}
