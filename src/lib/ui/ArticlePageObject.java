package lib.ui;

import java.util.concurrent.TimeUnit;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;

public class ArticlePageObject extends MainPageObject{
    private final String saveArticleButton = "xpath://*[@text='Save']";
    private final String addArticleToList = "xpath://*[@text='ADD TO LIST']";
    private final String articleTitle =
            "xpath://android.view.View[@resource-id='pcs']/android.view.View/android.widget.TextView";
    final String listNameInput = "xpath://*[@resource-id='org.wikipedia:id/text_input'][@text='Name of this list']";
    final String listSaveButton = "xpath://*[@resource-id='android:id/button1'][@text='OK']";

    public ArticlePageObject(AppiumDriver driver) {
        super(driver);
    }

    public ArticlePageObject waitForArticleTitle(String articleTitle) {
        waitForElementPresent(
                this.articleTitle,
                String.format("Could not open article with title \"%s\"", articleTitle));

        return this;
    }

    public void addOpenedArticleToList() {

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

    public ArticlePageObject saveOpenedArticleToNewList(String newListName) {
        addOpenedArticleToList();
        waitForElementAndSetValue(
                listNameInput,
                newListName,
                String.format("Could not set value \"%s\" to element %s", newListName, listNameInput));

        waitForElementAndClick(
                listSaveButton,
                "Could not click 'Save' button");

        return this;
    }

    public ArticlePageObject saveOpenedArticleToExistingList(String existingListName) {
        final String savedList = String.format("xpath://*[@text='%s']", existingListName);
        addOpenedArticleToList();

        waitForElementAndClick(
                savedList,
                String.format("Could not choose saved list with name %s", existingListName)
        );

        return this;
    }

    public SearchPageObject returnToSearchResultsFromOpenedArticle() {
        final String backButton = "xpath://*[@content-desc='Navigate up']";

        waitForElementAndClick(
                backButton,
                "Could not return to search results from article "
        );

        return new SearchPageObject(driver);
    }

    public boolean titleIsPresent(int timeoutSeconds) {
        return checkElementPresent(articleTitle, timeoutSeconds);
    }

    public boolean articleTitleHasText(String text) {
        String actualText = getElementText(articleTitle);

        return text.equals(actualText);
    }

    public String getArticleTitle(){
        return getElementText(articleTitle);
    }
}
