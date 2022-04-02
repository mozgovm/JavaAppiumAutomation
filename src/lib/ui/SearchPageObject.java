package lib.ui;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class SearchPageObject extends MainPageObject {
    private static String
            ARTICLE_XPATH_BY_TITLE_TPL = "//*[@text='ARTICLE_TITLE']",
            SEARCH_RESULTS_BY_TITLE_TPL = "//*[@resource-id='org.wikipedia:id/page_list_item_title']" +
                    "[contains(@text, 'SEARCH_STRING')]";

    private final By searchInput = By.xpath("//*[@text='Search Wikipedia']");
    private final By backButton = By.xpath(
            "//*[@resource-id='org.wikipedia:id/search_toolbar']/android.widget.ImageButton");
    final By cancelSearchButton = By.id("org.wikipedia:id/search_close_btn");
    final By searchResults = By.id("org.wikipedia:id/search_results_container");
    final By searchResultTitle = By.id("org.wikipedia:id/page_list_item_title");
    final By tutorialSkipButton = By.xpath("//*[@text='SKIP']");

    public SearchPageObject(AppiumDriver driver) {
        super(driver);
    }

    public SearchPageObject initSearch() {
        waitForElementAndClick(
                tutorialSkipButton,
                "Could not find and click tutorial skip button"
        );
        waitForElementAndClick(
                searchInput,
                "Could not click on search input element");
        waitForElementPresent(searchInput, "Search input did not open");

        return this;
    }

    public SearchPageObject waitForSearchResults(String searchString) {
        final By searchResults = By.xpath(
                SEARCH_RESULTS_BY_TITLE_TPL.replaceFirst("SEARCH_STRING", searchString));
        waitForElementPresent(
                searchResults,
                String.format("No results were found for search string %s", searchString));

        return this;
    }

    public SearchPageObject cancelSearch() {
        waitForElementAndClick(
                cancelSearchButton,
                "Could not click on cancel search button element");
        waitForInvisibilityOfElement(searchResults, "Search results are still present");

        return this;
    }

    public SearchPageObject searchForValue(String value) {

        initSearch();

        waitForElementAndSetValue(
                searchInput,
                value,
                String.format("Could not search for value \"%s\"", value));

        return this;
    }

    public ArticlePageObject openArticleFromSearchResults(String articleTitle) {
        final By article = By.xpath(
                String.format(ARTICLE_XPATH_BY_TITLE_TPL.replaceFirst("ARTICLE_TITLE", articleTitle)));

        waitForElementAndClick(
                article,
                String.format("Could click article title \"%s\" in search results", articleTitle));

        return new ArticlePageObject(driver);
    }

    public void returnToMainActivityFromSearchResults() {

        waitForElementAndClick(
                backButton,
                "Could not return to main activity from search results"
        );
    }

    public SearchPageObject waitForSearchResultTitle() {
        waitForElementPresent(searchResultTitle, "First search result title was not present");

        return this;
    }

    public List<WebElement> getListOfResultTitles() {
        return driver.findElements(searchResultTitle);
    }
}
