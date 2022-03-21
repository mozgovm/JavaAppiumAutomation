import java.net.URL;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;
import static org.openqa.selenium.support.ui.ExpectedConditions.invisibilityOfElementLocated;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

public class FirstTest {

    private AppiumDriver driver;
    private static final int DEFAULT_WAIT_TIMEOUT = 5;
    private final By openedArticleTitle = By.xpath(
            "//android.view.View[@resource-id='pcs']/android.view.View/android.widget.TextView");

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
        driver.manage().timeouts().implicitlyWait(DEFAULT_WAIT_TIMEOUT, TimeUnit.SECONDS);

        waitForElementAndClick(
                By.xpath("//*[@text='SKIP']"),
                "Could not find tutorial skip button"
        );
    }

    @After
    public void tearDown() {
        //Ex7*: Поворот экрана
        if (driver.getOrientation().equals(ScreenOrientation.LANDSCAPE)) {
            driver.rotate(ScreenOrientation.PORTRAIT);
        }

        driver.quit();
    }

    @Test
    public void openedSearchInputTextTest() {
        final By searchInput = By.xpath("//*[@text='Search Wikipedia']");
        waitForElementAndClick(
                searchInput,
                "Could not click on search input element");
        waitForElementPresent(searchInput, "Search input did not open");
        assertElementHasText(searchInput, "Search Wikipedia");
    }

    @Test
    public void cancelSearchTest() {
        final By cancelSearchButton = By.id("org.wikipedia:id/search_close_btn");
        final By searchResults = By.id("org.wikipedia:id/search_results_container");
        final String searchString = "Elden ring";

        searchForValue(searchString);
        waitForElementPresent(
                searchResults,
                String.format("No results were found for search string %s", searchString));

        waitForElementAndClick(
                cancelSearchButton,
                "Could not click on cancel search button element");
        waitForInvisibilityOfElement(searchResults, "Search results are still present");
    }

    @Test
    public void searchPhrasePresenceInSearchResultsTest() {
        final String searchString = "Java";
        final By firstSearchResultTitle = By.id("org.wikipedia:id/page_list_item_title");

        searchForValue(searchString);
        waitForElementPresent(firstSearchResultTitle, "First search result title was not present");

        List<WebElement> searchResultTitles = driver.findElements(firstSearchResultTitle);
        searchResultTitles.forEach(searchResultTitle -> assertElementContainsString(searchResultTitle, searchString));
    }

    //Ex5: Тест: сохранение двух статей
    @Test()
    public void saveTwoArticlesToListThenDeleteOne() {
        final String listName = "Learning programming";
        final String javaProgrammingLanguageTitle = "Java (programming language)";
        final String javaSoftwarePlatformTitle = "Java (software platform)";

        searchForValue("Java");
        openArticleFromSearchResults(javaProgrammingLanguageTitle);
        saveOpenedArticleToNewList(listName);
        returnToSearchResultsFromOpenedArticle();
        openArticleFromSearchResults(javaSoftwarePlatformTitle);
        saveOpenedArticleToExistingList(listName);
        returnToSearchResultsFromOpenedArticle();
        returnToMainActivityFromSearchResults();
        openSavedLists();
        openListByName(listName);
        deleteArticleFromSavedList(javaProgrammingLanguageTitle);
        openArticleFromSavedList(javaSoftwarePlatformTitle);

        assertElementHasText(openedArticleTitle, getArticleTitle());
    }

    //Ex6: Тест: assert title
    @Test
    public void openedArticleImmediatelyHasTitleElement() {
        final String articleTitle = "Sekiro: Shadows Die Twice";
        final By article = By.xpath(
                String.format("//*[@text='%s']", articleTitle));

        searchForValue("Sekiro");
        waitForElementAndClick(
                article,
                String.format("Could click article title \"%s\" in search results", articleTitle));

        assertElementPresent(openedArticleTitle,
                "Title was not immediately present in opened article",
                0);
    }

    private void assertElementHasText(By by, String expectedText) {
        String actualText = getElementText(by);

        Assert.assertEquals("Actual text did not match expected text", expectedText, actualText);
    }

    private void assertElementContainsString(WebElement el, String s) {
        Assert.assertTrue(String.format(
                "Element's text \"%s\" didn't contain string \"%s\"", el.getText(), s
        ), el.getText().contains(s));
    }

    private void assertElementPresent(By by, String errorMessage, int timeoutSeconds) {
        driver.manage().timeouts().implicitlyWait(timeoutSeconds, TimeUnit.SECONDS);
        final boolean elementIsPresent = driver.findElements(by).size() > 0;
        driver.manage().timeouts().implicitlyWait(DEFAULT_WAIT_TIMEOUT, TimeUnit.SECONDS);

        Assert.assertTrue(errorMessage, elementIsPresent);
    }

    private void searchForValue(String value) {
        final By searchInput = By.xpath("//*[@text='Search Wikipedia']");

        waitForElementAndClick(
                searchInput,
                "Could not click on search input element");

        waitForElementAndSetValue(
                searchInput,
                value,
                String.format("Could not search for value \"%s\"", value));
    }

    private void waitForElementAndClick(By by, String errorMessage) {
        waitForElementAndClick(by, errorMessage, DEFAULT_WAIT_TIMEOUT);
    }

    private void waitForElementAndClick(By by, String errorMessage, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, timeoutSeconds);
        wait
                .withMessage(errorMessage)
                .until(visibilityOfElementLocated(by))
                .click();
    }

    private void waitForElementAndSetValue(By by, String value, String errorMessage) {
        waitForElementAndSetValue(by, value, errorMessage, DEFAULT_WAIT_TIMEOUT);
    }

    private void waitForElementAndSetValue(By by, String value, String errorMessage, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, timeoutSeconds);
        MobileElement el = (MobileElement) wait
                .withMessage(errorMessage)
                .until(visibilityOfElementLocated(by));
        el.setValue(value);
    }

    private WebElement waitForElementPresent(By by, String errorMessage) {
        return waitForElementPresent(by, errorMessage, DEFAULT_WAIT_TIMEOUT);
    }

    private WebElement waitForElementPresent(By by, String errorMessage, long timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
        return wait
                .withMessage(errorMessage + "\n")
                .until(presenceOfElementLocated(by));
    }

    private boolean waitForInvisibilityOfElement(By by, String errorMessage) {
        return waitForInvisibilityOfElement(by, errorMessage, DEFAULT_WAIT_TIMEOUT);
    }

    private boolean waitForInvisibilityOfElement(By by, String errorMessage, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, timeoutSeconds);
        return wait
                .withMessage(errorMessage)
                .until(invisibilityOfElementLocated(by));
    }

    private String getElementText(By by) {
        return waitForElementPresent(by, "Could not get article's title").getText();
    }

    private String getArticleTitle() {
        return getElementText(openedArticleTitle);
    }

    protected void swipeElementLeft(By by, String errorMessage) {
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

    private void openArticleFromSearchResults(String articleTitle) {
        final By article = By.xpath(
                String.format("//*[@text='%s']", articleTitle));

        waitForElementAndClick(
                article,
                String.format("Could click article title \"%s\" in search results", articleTitle));

        waitForElementPresent(
                openedArticleTitle,
                String.format("Could not open article with title \"%s\"", articleTitle));
    }

    private void addOpenedArticleToList() {
        final By saveArticleButton = By.xpath("//*[@text='Save']");
        final By addArticleToList = By.xpath("//*[@text='ADD TO LIST']");

        try {
            waitForElementAndClick(
                    saveArticleButton,
                    "Could not click Save button in bottom action tab");

            waitForElementPresent(
                    addArticleToList,
                    "\"ADD TO LIST\" button did not appear",
                    1);
        } catch (TimeoutException e) {
            waitForElementAndClick(
                    saveArticleButton,
                    "Could not click Save button in bottom action tab");
        }

        waitForElementAndClick(
                addArticleToList,
                "Could not click \"ADD TO LIST\" in popup");
    }

    private void saveOpenedArticleToNewList(String newListName) {
        final By listNameInput = By.xpath("//*[@resource-id='org.wikipedia:id/text_input'][@text='Name of this list']");
        final By listSaveButton = By.xpath("//*[@resource-id='android:id/button1'][@text='OK']");
        addOpenedArticleToList();
        waitForElementAndSetValue(
                listNameInput,
                newListName,
                String.format("Could not set value \"%s\" to element %s", newListName, listNameInput));

        driver.findElement(listSaveButton).click();
    }

    private void saveOpenedArticleToExistingList(String existingListName) {
        final By savedList = By.xpath(String.format("//*[@text='%s']", existingListName));
        addOpenedArticleToList();

        waitForElementAndClick(
                savedList,
                String.format("Could not choose saved list with name %s", existingListName)
        );
    }

    private void returnToSearchResultsFromOpenedArticle() {
        final By backButton = By.xpath("//*[@content-desc='Navigate up']");

        waitForElementAndClick(
                backButton,
                "Could not return to search results from article "
        );
    }

    private void returnToMainActivityFromSearchResults() {
        final By backButton = By.xpath("//*[@resource-id='org.wikipedia:id/search_toolbar']/android.widget.ImageButton");

        waitForElementAndClick(
                backButton,
                "Could not return to main activity from search results"
        );
    }

    private void openSavedLists() {
        final By savedListsButton = By.xpath("//android.widget.FrameLayout[@content-desc='Saved']");
        waitForElementAndClick(
                savedListsButton,
                "Could not open saved lists"
        );

        boolean foundTutorial = driver.findElements(By.id("org.wikipedia:id/onboarding_view")).size() > 0;
        if (foundTutorial) driver.findElement(By.id("org.wikipedia:id/negativeButton")).click();
    }

    private void openListByName(String listName) {
        final By savedList = By.xpath(String.format("//*[@text='%s']", listName));
        waitForElementAndClick(
                savedList,
                String.format("Could not open saved list with name %s", listName)
        );

        waitForElementPresent(
                By.id("org.wikipedia:id/item_reading_list_statistical_description"),
                "List description was not present"
        );
    }

    private void deleteArticleFromSavedList(String articleTitle) {
        final By article = By.xpath(
                String.format("//*[@text='%s']", articleTitle));

        swipeElementLeft(
                article,
                String.format("Could not delete article with title \"%s\" from saved list", articleTitle));
        waitForInvisibilityOfElement(
                article,
                String.format("Article with \"%s\" is still present is saved list", articleTitle),
                3);
    }

    private void openArticleFromSavedList(String articleTitle) {
        final By article = By.xpath(
                String.format("//*[@text='%s']", articleTitle));

        waitForElementAndClick(
                article,
                String.format("%s article was not found in saved list)", articleTitle));
    }
}
