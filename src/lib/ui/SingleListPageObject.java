package lib.ui;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;

public class SingleListPageObject extends MainPageObject {
    private static String ARTICLE_ELEMENT_XPATH_TPL = "xpath://*[@text='ARTICLE_TITLE']";

    public SingleListPageObject(AppiumDriver driver) {
        super(driver);
    }

    public SingleListPageObject deleteArticleFromSavedList(String articleTitle) {
        final String article =
                ARTICLE_ELEMENT_XPATH_TPL.replaceFirst("ARTICLE_TITLE", articleTitle);

        swipeElementLeft(
                article,
                String.format("Could not delete article with title \"%s\" from saved list", articleTitle));
        waitForInvisibilityOfElement(
                article,
                String.format("Article with \"%s\" is still present is saved list", articleTitle),
                3);

        return this;
    }

    public ArticlePageObject openArticleFromSavedList(String articleTitle) {
        final String article = ARTICLE_ELEMENT_XPATH_TPL.replaceFirst("ARTICLE_TITLE", articleTitle);

        waitForElementAndClick(
                article,
                String.format("%s article was not found in saved list)", articleTitle));

        return new ArticlePageObject(driver);
    }
}
