package lib.ui;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;

public class NavigationUI extends MainPageObject {

    public NavigationUI(AppiumDriver driver) {
        super(driver);
    }

    public MyListsPageObject openMyLists() {
        final By savedListsButton = By.xpath("//android.widget.FrameLayout[@content-desc='Saved']");
        waitForElementAndClick(
                savedListsButton,
                "Could not open saved lists"
        );

        boolean foundTutorial = driver.findElements(By.id("org.wikipedia:id/onboarding_view")).size() > 0;
        if (foundTutorial) driver.findElement(By.id("org.wikipedia:id/negativeButton")).click();

        return new MyListsPageObject(driver);
    }
}
