package tests;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import org.testng.annotations.BeforeMethod;
import java.io.IOException;


import static utils.ConfigU.*;


public class BaseTest {

    //Global variable
    Playwright playwright;
    Browser browser;
    Page page;
    String base_url;

    @BeforeMethod(alwaysRun = true)
    public void setUp() throws IOException {

        playwright = Playwright.create();

        String browserName = System.getProperty("browser")!=null ? System.getProperty("browser") : getBrowser();
        String envName = System.getProperty("env")!=null ? System.getProperty("env") :  getEnv();


        if("firefox".equals(browserName)){
//            browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(false));
            browser = playwright.firefox().launch();
        } else if ("safari".equals(browserName)) {
//            browser = playwright.webkit().launch(new BrowserType.LaunchOptions().setHeadless(false));
            browser = playwright.webkit().launch();
        } else {
            browser = playwright.chromium().launch();
        }


        page = browser.newPage();
        page.setDefaultTimeout(8000);

        base_url =  getURL(envName +".base_url");

        PlaywrightAssertions.setDefaultAssertionTimeout(7000);


    }
}
