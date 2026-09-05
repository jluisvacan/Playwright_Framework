package tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.nio.file.Paths;

public class UIValidationsTest {

    // Global variable
    Playwright playwright;
    Browser browser;
    Page pageA;
    BrowserContext contextA;


    @BeforeMethod(alwaysRun = true)
    public void setUp(){
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        //Browsers -> multiples profiles (incognito, cookies, etc.)
        BrowserContext contextB = browser.newContext(); // initialize incognito mode
        contextB.newPage();

        contextA = browser.newContext(); // initialize incognito mode

        //start trace viewer
        contextA.tracing().start(new Tracing.StartOptions()
                .setScreenshots(true)
                .setSnapshots(true)
                .setSources(true));

        pageA = contextA.newPage();
        pageA.navigate("https://rahulshettyacademy.com/loginpagePractise/");

    }

    @AfterMethod
    public void tearDown(){
        //close tracer view
        contextA.tracing().stop(new Tracing.StopOptions()
                .setPath(Paths.get("trace.zip")));
    }

    @Test
    public void childWindowHandle(){

        Locator blinkingText = pageA.locator(".blinkingText");
        // Save new page in another Page object
        Page newPage = contextA.waitForPage(() -> blinkingText.first().click());
        newPage.waitForLoadState();

        String textNewPage = newPage.getByText("Documents request").innerText();
        System.out.println(textNewPage);
        String childText = newPage.locator(".red").textContent();
        String emailId = childText.split("at ")[1].split(" ")[0];
        System.out.println(emailId);

        pageA.getByLabel("Username:").fill(emailId);
        pageA.waitForTimeout(3000);

        //get value in the box
        System.out.println(pageA.getByLabel("Username:").inputValue());



    }


    @Test
    public void uiControl(){
        //select radio button
        Locator userRdBtn = pageA.getByRole(AriaRole.RADIO, new Page.GetByRoleOptions().setName(" User"));
        userRdBtn.check();

        //pop up
        pageA.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Okay")).click();

        //validate change radio button
        Assert.assertTrue(userRdBtn.isChecked());

        //checkbox
        Locator checkBoxTerms = pageA.getByRole(AriaRole.CHECKBOX, new Page.GetByRoleOptions().setName("I Agree to the "));
        checkBoxTerms.check();
        Assert.assertTrue(checkBoxTerms.isChecked());

        //combo box
        pageA.getByRole(AriaRole.COMBOBOX).selectOption("Consultant");
        pageA.waitForTimeout(3000);

    }
}
