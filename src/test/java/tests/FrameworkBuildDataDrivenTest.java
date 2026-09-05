package tests;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.*;
import utils.DataProviderUtil;
import java.io.IOException;
import java.util.HashMap;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class FrameworkBuildDataDrivenTest extends BaseTest{

    @DataProvider(name = "eventBookingData")
    public Object[][] eventBookingData() throws IOException {

        return DataProviderUtil.getJsonDataToMap("\\src\\test\\resources\\eventBookingData.json");
    }

    @Test(dataProvider = "eventBookingData", description = "Create Event - Book that event and verify its booked")
    public void demoTest(HashMap<String,String> data) throws IOException {


        LoginPage loginPage = new LoginPage(page, base_url);

        DashboardPage dashboardPage = loginPage.loginToApplication();;
        dashboardPage.waitForEventsToLoad();


        AdminEventsPage adminEventsPage = new AdminEventsPage(page);
        adminEventsPage.goTo();
        adminEventsPage.createEvent(
                data.get("titlePrefix"),
                data.get("description"),
                data.get("category"),
                data.get("city"),
                data.get("venue"),
                data.get("dateTime"),
                data.get("price"),
                data.get("totalSeats")
        );



        //Step 2 - Find newly created in the events page
        EventsPage eventsPage = new EventsPage(page);
        eventsPage.goTo();
        Locator targetCard = eventsPage.findEventCard(data.get("titlePrefix"));
        int seatsNumBeforeBooking = eventsPage.getSeatsCount(targetCard);


        BookingFormPage bookingFormPage = eventsPage.proceedToBookingEvent(targetCard);
        bookingFormPage.fillAndConfirm(
                data.get("fullName"),
                data.get("email"),
                data.get("phone")
        );




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
        Locator targetCardAfterBooking = eventCardsAfterBooking.filter(new Locator.FilterOptions().setHasText(data.get("titlePrefix")));
        assertThat(targetCardAfterBooking).isVisible();
        String seatsTextAfterBooking = targetCardAfterBooking.getByText("seats").innerText();

        System.out.println("#### After: " + seatsTextAfterBooking);

        //Obtain number after booking
        int seatsNumAfterBooking = Integer.parseInt(seatsTextAfterBooking.split(" ")[0]);

        //Validate decrease number of seats available
        Assert.assertTrue(seatsNumBeforeBooking > seatsNumAfterBooking);




    }

}
