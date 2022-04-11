package lib.ui;

import io.appium.java_client.AppiumDriver;

public class WelcomePageObject extends MainPageObject {

    private static final String
        STEP_LEARN_MORE_LINK = "xpath://XCUIElementTypeStaticText[@name='Learn more about Wikipedia']",
        STEP_NEW_WAYS_TO_EXPLORE_TEXT = "xpath://XCUIElementTypeStaticText[@name='New ways to explore']",
        STEP_ADD_OR_EDIT_PREFERRED_LANG_LINK = "xpath:" +
                "(//XCUIElementTypeStaticText[@name='Add or edit preferred languages'])[2]",
        STEP_LEARN_MORE_ABOUT_DATA_COLLECTED_LINK = "xpath:" +
                "//XCUIElementTypeStaticText[@name='Learn more about data collected']",
        NEXT_BUTTON = "xpath://XCUIElementTypeButton[@name='Next']",
        GET_STARTED_BUTTON = "xpath://XCUIElementTypeButton[@name='Get started']";

    public WelcomePageObject(AppiumDriver driver) {
        super(driver);
    }

    public void waitForLearnMoreLink() {
        waitForElementPresent(
                STEP_LEARN_MORE_LINK,
                "Could not find 'Learn More' link");
    }

    public void waitForNewWaysToExploreText() {
        waitForElementPresent(
                STEP_NEW_WAYS_TO_EXPLORE_TEXT,
                "Could not find 'New Ways To Explore' text");
    }

    public void waitForAddOrEditPreferredLangLink() {
        waitForElementPresent(
                STEP_ADD_OR_EDIT_PREFERRED_LANG_LINK,
                "Could not find 'Add or Edit Preferred Lang' link");
    }

    public void waitForLearnMoreAboutDataCollectedLink() {
        waitForElementPresent(
                STEP_LEARN_MORE_ABOUT_DATA_COLLECTED_LINK,
                "Could not find 'Learn More About Data Collected' link");
    }

    public void clickNextButton() {
        waitForElementAndClick(
                NEXT_BUTTON,
                "Could not find and click 'Next' button"
        );
    }

    public void clickGetStartedButton() {
        waitForElementAndClick(
                GET_STARTED_BUTTON,
                "Could not find and click 'Get Started' button"
        );
    }
}
