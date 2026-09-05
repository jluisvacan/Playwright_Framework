package tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.LocatorAssertions;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.options.AriaRole;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.ConfigU;

import java.io.IOException;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class BasicTest {

    //Global variable
    Playwright playwright;
    Browser browser;
    Page page;


    @BeforeMethod
    public void setUp(){
        //Invoke browser -> Invoke tab -> Type url
        //HeadMode (open browser) -- Headless (without browser)

        playwright = Playwright.create();

        //select browser and launch
//        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
//        Browser browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(false));
//        Browser browser = playwright.webkit().launch(new BrowserType.LaunchOptions().setHeadless(false));

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

        //get website title
        System.out.println(page.title()); // EventHub — Discover & Book Events

        //assertions
        assertThat(page).hasTitle("EventHub — Discover & Book Events");



        //locators
        //identify label Email and input specific value in field
//        page.getByLabel("Email").fill("email@gmail.com");
        page.getByLabel("Password").fill(ConfigU.getPassUser());

        //identify placeholder and input specific value in field
        page.getByPlaceholder("you@email.com").fill(ConfigU.getEmailUser());


        //action button
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign In")).click();

        //validate button link in new page
        assertThat(page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Browse Events →"))).isVisible();




        // Step 1 - Created Event from Admin page

        //after login, navigate in page
        page.navigate("https://eventhub.rahulshettyacademy.com/admin/events");

        //locator element by id CSS // change action timeout
        page.locator("css=#event-title-input").fill("QA Summit Example: Title 3", new Locator.FillOptions().setTimeout(10000));

        // locator child element -> #parent child_tag
        page.locator("#admin-event-form textarea").fill("QA Summit Example: Description");

        //select value: dropdown
        page.getByLabel("Category").selectOption("Sports");


        page.getByLabel("City").fill("QA Summit Example: City");
        page.getByLabel("Venue").fill("QA Summit Example: Venue");


        //select date: calendar
        page.getByLabel("Event Date & Time").fill("2026-10-31T18:20");

        page.getByLabel("Price ($)").fill("250.74");
        page.getByLabel("Total Seats").fill("210");

        //hold browser ms
        page.waitForTimeout(3000);

        //Send the form
        page.locator("css=#add-event-btn").click(new Locator.ClickOptions().setTimeout(12000));

        //validate success message // use global assertions timeout (7s)
        assertThat(page.getByText("Event created!")).isVisible();



        //Step 2 - Find newly created in the events page
        page.locator("css=#nav-events").click();

        page.waitForTimeout(3000);

        //using data-testid
        Locator eventCards = page.getByTestId("event-card");
        System.out.println("### Cards -> " + eventCards.count());

        //Validate new card using title
        Locator targetCard = eventCards.filter(new Locator.FilterOptions().setHasText("QA Summit Example: Title 3"));

        // change assertions timeout
        assertThat(targetCard).isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(10000));


        page.waitForTimeout(3000);
        //search text in target item
        String seatsText = targetCard.getByText("seats").innerText();
        System.out.println("#### Before: "+ seatsText);


        //Obtain number before booking
        int seatsNumBeforeBooking = Integer.parseInt(seatsText.split(" ")[0]);

        // action to target item btn
        targetCard.getByTestId("book-now-btn").click();




        //Step 3 - Purchase confirmation
        page.getByLabel("Full Name").fill("Luis");
        page.locator("#customer-email").fill("luis@example.com");
        page.getByPlaceholder("+91 98765 43210").fill("1112223344");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Confirm Booking")).click();

        assertThat(page.getByText("Your tickets are reserved.")).isVisible();
        //Save text
        String bookingReference = page.locator(".booking-ref").innerText();
        // If the button redirection to new page, use LINK
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("View My Bookings")).click();

        //Verify booking history
        Locator bookingCards = page.locator(".booking-ref");

        //Filter target booking
        Locator targetBookingCard = bookingCards.filter(new Locator.FilterOptions().setHasText(bookingReference));
        assertThat(targetBookingCard).isVisible();


        // seat reduction check
        page.locator("#nav-events").click();
        Locator eventCardsAfterBooking = page.getByTestId("event-card");
        System.out.println("### Event Cards -> " + eventCardsAfterBooking.count());

        page.waitForTimeout(3000);
        Locator targetCardAfterBooking = eventCardsAfterBooking.filter(new Locator.FilterOptions().setHasText("QA Summit Example: Title 3"));
        assertThat(targetCardAfterBooking).isVisible();
        String seatsTextAfterBooking = targetCardAfterBooking.getByText("seats").innerText();

        System.out.println("#### After: " + seatsTextAfterBooking);

        //Obtain number after booking
        int seatsNumAfterBooking = Integer.parseInt(seatsTextAfterBooking.split(" ")[0]);

        //Validate decrease number of seats available
        Assert.assertTrue(seatsNumBeforeBooking > seatsNumAfterBooking);




    }

    @AfterMethod
    public void tearDown(){

    }

}
