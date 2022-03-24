package lib.ui;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;

public class MyListsPageObject extends MainPageObject {
    private static String LIST_ELEMENT_XPATH_TPL = "//*[@text='LIST_NAME']";

    public MyListsPageObject(AppiumDriver driver) {
        super(driver);
    }

    public SingleListPageObject openListByName(String listName) {
        final By savedList = By.xpath(LIST_ELEMENT_XPATH_TPL.replaceFirst("LIST_NAME", listName));
        waitForElementAndClick(
                savedList,
                String.format("Could not open saved list with name %s", listName)
        );

        waitForElementPresent(
                By.id("org.wikipedia:id/item_reading_list_statistical_description"),
                "List description was not present"
        );

        return new SingleListPageObject(driver);
    }
}
