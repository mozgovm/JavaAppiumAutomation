import java.net.URL;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class FirstTest {

    private AppiumDriver driver;
    private WebDriverWait wait;

    @Before
    public void setUp() throws Exception {
        DesiredCapabilities capabilities = new DesiredCapabilities();

        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("deviceName", "AndroidTestDevice");
        capabilities.setCapability("platformVersion", "8.0.0");
        capabilities.setCapability("automationName", "Appium");
        capabilities.setCapability("appPackage", "org.wikipedia");
        capabilities.setCapability("appActivity", ".main.MainActivity");
        capabilities.setCapability("app",
                "/Users/mmozgov/JavaAppiumAutomation/apks/org.wikipedia.apk");

        driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
        wait = new WebDriverWait(driver, 3);

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@text='SKIP']"))).click();
    }

    @After
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void openedSearchInputTextTest() {
        WebElement openedSearchInput = openSearch();
        assertElementHasText(openedSearchInput, "Search Wikipedia");
    }

    @Test
    public void cancelSearchTest() {
        final By cancelSearchButton = By.id("org.wikipedia:id/search_close_btn");
        final By searchResults = By.id("org.wikipedia:id/search_results_container");

        openSearch()
                .setValue("Elden ring");
        wait.until(ExpectedConditions.presenceOfElementLocated(searchResults));

        driver.findElement(cancelSearchButton).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(searchResults));
    }

    @Test
    public void searchPhrasePresenceInSearchResutsTest() {
        final String searchString = "Java";
        final By firstSearchResultTitle = By.id("org.wikipedia:id/page_list_item_title");

        openSearch()
                .setValue(searchString);
        wait.until(ExpectedConditions.visibilityOfElementLocated(firstSearchResultTitle));

        List<WebElement> searchResultTitles = driver.findElements(firstSearchResultTitle);
        searchResultTitles.forEach(searchResultTitle -> assertElementContainsString(searchResultTitle, searchString));
    }

    private void assertElementHasText(WebElement el, String expectedText) {
        String actualText = wait.until(ExpectedConditions.visibilityOf(el)).getText();

        Assert.assertEquals("Actual text did not match expected text", expectedText, actualText);
    }

    private void assertElementContainsString(WebElement el, String s) {
        Assert.assertTrue(String.format(
                "Element's text \"%s\" didn't contain string \"%s\"", el.getText(), s
        ), el.getText().contains(s));
    }

    private MobileElement openSearch() {
        final By searchInput = By.xpath("//*[@text='Search Wikipedia']");
        driver.findElement(searchInput).click();
        return (MobileElement) wait.until(ExpectedConditions.visibilityOfElementLocated(searchInput));
    }
}
