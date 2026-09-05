package tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.nio.file.Paths;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class UIValidationsContinuedTest {

    // Global variable
    Playwright playwright;
    Browser browser;
    Page page;
    BrowserContext context;

    @BeforeMethod(alwaysRun = true)
    public void setUp(){
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        context = browser.newContext(); // initialize incognito mode
        page = context.newPage();
        page.navigate("https://rahulshettyacademy.com/AutomationPractice/");
        page.setDefaultTimeout(5000);

    }

    @Test
    public void popupValidations(){

        assertThat(page.getByPlaceholder("Hide/Show Example")).isVisible();
        page.locator("#hide-textbox").click();
        assertThat(page.getByPlaceholder("Hide/Show Example")).isHidden();

        //wait popup and accept
        page.onDialog(dialog -> dialog.accept());
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Alert")).click();
        page.waitForTimeout(3000);


        //hover menu
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Mouse Hover")).hover();
        page.waitForTimeout(3000);
//        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Top")).click();


        //iFrames // page Level / frame Level
        FrameLocator framesPage = page.frameLocator("#courses-iframe");
        framesPage.getByRole(AriaRole.LINK, new FrameLocator.GetByRoleOptions().setName("Job Support")).click();
        page.waitForTimeout(5000);
        String textCheck = framesPage.locator(".inner-box h1").textContent();
        System.out.println(textCheck);
    }


    @Test
    public void screenShotTest(){
        //window screenshot
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("pagescreenshot.png")));
        //element screenshot
        Locator displayEditBox = page.getByPlaceholder("Hide/Show Example");
        displayEditBox.screenshot(new Locator.ScreenshotOptions().setPath(Paths.get("editboxscreenshot.png")));
        page.locator("#hide-textbox").click();
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("pagePostScreenshot.png")));



    }
}
