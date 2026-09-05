package tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.options.AriaRole;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.ConfigU;
import java.io.IOException;
import java.nio.file.Paths;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class MockWebTest {

    //Global variable
    Playwright playwright;
    Browser browser;
    Page page;


    @BeforeMethod
    public void setUp(){

        playwright = Playwright.create();

        // execute with local browser
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setChannel("chrome").setHeadless(false));

        //open new page
        page = browser.newPage();

        // enter to url
        page.navigate("https://eventhub.rahulshettyacademy.com/login");

        //Change global assertions timeout - default 5s
        PlaywrightAssertions.setDefaultAssertionTimeout(7000);

        //Change global actions timeout - default 30s
        page.setDefaultTimeout(8000);
    }



    @Test(description = "Create Event - Book that event and verify its booked")
    public void demoTest() throws IOException {

        page.getByLabel("Password").fill(ConfigU.getPassUser());
        page.getByPlaceholder("you@email.com").fill(ConfigU.getEmailUser());

        //locator
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign In")).click();

        assertThat(page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Browse Events →"))).isVisible();


        //STEP 1 - Create Event from Admin page
        //modify response with route.fulfill
        page.route("**/api/events**", route -> route.fulfill(
                new Route.FulfillOptions()
                        .setPath(Paths.get("src/test/resources/events_6.json"))
        ));
        page.navigate("https://eventhub.rahulshettyacademy.com/events");
        page.waitForTimeout(5000);
        Locator eventCards = page.getByTestId("event-card");

        Assert.assertEquals(eventCards.count(), 6);

        //Validate the first element of banner
        assertThat(page.locator(".mx-1").first()).isVisible();


        //Banner is not active, 4 cards active
        page.route("**/api/events**", route -> route.fulfill(
                new Route.FulfillOptions()
                        .setPath(Paths.get("src/test/resources/events_4.json"))
        ));
        page.navigate("https://eventhub.rahulshettyacademy.com/events");
        page.waitForTimeout(5000);
        Locator eventCards1 = page.getByTestId("event-card");

        Assert.assertEquals(eventCards1.count(), 4);
        assertThat(page.locator(".mx-1").first()).isHidden();




    }

    @Test
    public void routeResumeTest() throws IOException {
        //login
        page.getByLabel("Password").fill(ConfigU.getPassUser());
        page.getByPlaceholder("you@email.com").fill(ConfigU.getEmailUser());
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign In")).click();

        assertThat(page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Browse Events →"))).isVisible();

        page.waitForTimeout(5000);
        page.getByTestId("nav-bookings").click();

        //route.resume
        //request is modified
        page.route("**/api/bookings**", route -> route.resume(
                new Route.ResumeOptions()
                        .setUrl("https://api.eventhub.rahulshettyacademy.com/api/bookings/91159")
        ));
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("View Details")).first().click();

        assertThat(page.getByText("Not found")).isVisible();
    }
}
