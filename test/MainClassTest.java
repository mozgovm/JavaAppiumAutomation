import org.junit.Assert;
import org.junit.Test;

public class MainClassTest {

    private final MainClass mainClass;

    public MainClassTest() {
         this.mainClass = new MainClass();
    }

    @Test
    public void testGetLocalNumber() {
        final int expectedLocalNumber = 14;
        final int actualLocalNumber = mainClass.getLocalNumber();

        Assert.assertEquals("Expected local number value did not match actual value",
                expectedLocalNumber, actualLocalNumber);
    }

    @Test
    public void testGetClassNumber() {
        final int actualClassNumber = mainClass.getClassNumber();

        Assert.assertTrue("Actual class number value was less than 45", actualClassNumber > 45);
    }

    @Test
    public void testGetClassString() {
        final String actualClassString = mainClass.getClassString();
        boolean actualStringContainsSubstring = actualClassString.contains("hello")
                || actualClassString.contains("Hello");

        Assert.assertTrue("Class string \"" + actualClassString +
                        "\" did not contain substrings \"hello\" or \"Hello\"",
                actualStringContainsSubstring);
    }
}
