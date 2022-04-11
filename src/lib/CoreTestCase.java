package lib;

import java.net.URL;
import java.util.concurrent.TimeUnit;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import junit.framework.TestCase;
import org.openqa.selenium.remote.DesiredCapabilities;

public class CoreTestCase extends TestCase {
    protected AppiumDriver driver;
    private static final int DEFAULT_WAIT_TIMEOUT = 5;
    private static final String PLATFORM_IOS = "ios";
    private static final String PLATFORM_ANDROID = "android";
    private static String AppiumURL = "http://127.0.0.1:4723/wd/hub";

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        DesiredCapabilities capabilities = getCapabilitiesByPlatformEnv();
        driver = getDriver(capabilities);
        driver.manage().timeouts().implicitlyWait(DEFAULT_WAIT_TIMEOUT, TimeUnit.SECONDS);
    }

    @Override
    protected void tearDown() throws Exception {
        driver.quit();

        super.tearDown();
    }

    private DesiredCapabilities getCapabilitiesByPlatformEnv() throws Exception {
        String platform = System.getenv("PLATFORM");
        DesiredCapabilities capabilities = new DesiredCapabilities();

        if (platform.equals(PLATFORM_ANDROID)) {
            capabilities.setCapability("platformName", "Android");
            capabilities.setCapability("deviceName", "AndroidTestDevice");
            capabilities.setCapability("platformVersion", "8.0.0");
            capabilities.setCapability("automationName", "Appium");
            capabilities.setCapability("appPackage", "org.wikipedia");
            capabilities.setCapability("appActivity", ".main.MainActivity");
            capabilities.setCapability("app",
                    "/Users/mmozgov/JavaAppiumAutomation/apks/org.wikipedia.apk");
            capabilities.setCapability("orientation", "PORTRAIT");
        } else if (platform.equals(PLATFORM_IOS)) {
            capabilities.setCapability("platformName", "iOS");
            capabilities.setCapability("deviceName", "iPhone SE");
            capabilities.setCapability("platformVersion", "13.0");
            capabilities.setCapability("app",
                    "/Users/mmozgov/JavaAppiumAutomation/apps/wikipedia.app");
            capabilities.setCapability("orientation", "PORTRAIT");
        } else {
            throw new Exception(String.format(
                    "Could not get run platform from env variable. Platform value: %s",
                    platform));
        }

        return capabilities;
    }

    private AppiumDriver getDriver(DesiredCapabilities capabilities) throws Exception {
        AppiumDriver driver;
        final URL url = new URL(AppiumURL);
        String platform = System.getenv("PLATFORM");

        if (platform.equals(PLATFORM_ANDROID)) {
            driver = new AndroidDriver(url, capabilities);
        } else if (platform.equals(PLATFORM_IOS)) {
            driver = new IOSDriver(url, capabilities);
        } else {
            throw new Exception(String.format(
                    "Could not get run platform from env variable. Platform value: %s",
                    platform));
        }

        return driver;
    }
}
