package Tests;

import lib.CoreTestCase;
import lib.ui.ArticlePageObject;
import lib.ui.MainPageObject;
import lib.ui.NavigationUI;
import lib.ui.SearchPageObject;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;

public class FirstTest extends CoreTestCase {
    private MainPageObject MainPageObject;
    private SearchPageObject SearchPageObject;
    private NavigationUI NavigationUI;
    private ArticlePageObject ArticlePageObject;

    protected void setUp() throws Exception {
        super.setUp();

        MainPageObject = new MainPageObject(driver);
        SearchPageObject = new SearchPageObject(driver);
        NavigationUI = new NavigationUI(driver);
        ArticlePageObject = new ArticlePageObject(driver);
    }

    @Test
    public void testOpenedSearchInputText() {
        final String searchInput = "xpath://*[@text='Search Wikipedia']";
        SearchPageObject
                .initSearch()
                .assertElementHasText(searchInput, "Search Wikipedia");
    }

    @Test
    public void testCancelSearch() {
        final String searchString = "Elden Ring";

        SearchPageObject
                .searchForValue(searchString)
                .waitForSearchResults(searchString)
                .cancelSearch();
    }

    @Test
    public void testSearchPhrasePresenceInSearchResults() {
        final String searchString = "Java";

        SearchPageObject
                .searchForValue(searchString)
                .waitForSearchResultTitle()
                .getListOfResultTitles()
                    .forEach(searchResultTitle ->
                        MainPageObject.assertElementContainsString(searchResultTitle, searchString));
    }

    //Ex5: Тест: сохранение двух статей
    @Test()
    public void testSaveTwoArticlesToListThenDeleteOne() {
        final String listName = "Learning programming";
        final String javaProgrammingLanguageTitle = "Java (programming language)";
        final String javaSoftwarePlatformTitle = "Java (software platform)";

        SearchPageObject
                .searchForValue("Java")
                .openArticleFromSearchResults(javaProgrammingLanguageTitle)
                .waitForArticleTitle(javaProgrammingLanguageTitle)
                .saveOpenedArticleToNewList(listName)
                .returnToSearchResultsFromOpenedArticle()
                .openArticleFromSearchResults(javaSoftwarePlatformTitle)
                .waitForArticleTitle(javaSoftwarePlatformTitle)
                .saveOpenedArticleToExistingList(listName)
                .returnToSearchResultsFromOpenedArticle()
                .returnToMainActivityFromSearchResults();

        boolean articleTitleHasText = NavigationUI
                .openMyLists()
                .openListByName(listName)
                .deleteArticleFromSavedList(javaProgrammingLanguageTitle)
                .openArticleFromSavedList(javaSoftwarePlatformTitle)
                .articleTitleHasText(ArticlePageObject.getArticleTitle());

        Assert.assertTrue(
                String.format("Article title did not contain text \"%s\"", ArticlePageObject.getArticleTitle()),
                articleTitleHasText);
    }

    //Ex6: Тест: assert title
    @Test
    public void testOpenedArticleImmediatelyHasTitleElement() {

        boolean titleIsImmediatelyPresent = SearchPageObject
                .searchForValue("Sekiro")
                .openArticleFromSearchResults("Sekiro: Shadows Die Twice")
                .titleIsPresent(0);

        Assert.assertTrue(
                "Title was not immediately present in opened article",
                titleIsImmediatelyPresent);
    }
}
