package tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.LocatorAssertions;
import com.microsoft.playwright.options.AriaRole;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import pages.*;

import java.io.IOException;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static utils.Date.getNextDay;

public class FrameworkBuildTest extends BaseTest{

    @Test(groups = {"smoke", "framework"}, description = "Create Event - Book that event and verify its booked")
    public void demoTest() throws IOException {

        String eventTitle = "Test QA";


        LoginPage loginPage = new LoginPage(page, base_url);

        DashboardPage dashboardPage = loginPage.loginToApplication();
//        DashboardPage dashboardPage = new DashboardPage();
        dashboardPage.waitForEventsToLoad();


        AdminEventsPage adminEventsPage = new AdminEventsPage(page);
        adminEventsPage.goTo();
        adminEventsPage.createEvent(
                eventTitle,
                "QA Summit Example: Description",
                "Sports",
                "QA Summit Example: City",
                "QA Summit Example: Venue",
                getNextDay(),
                "250.74",
                "210"

        );



        //Step 2 - Find newly created in the events page
        EventsPage eventsPage = new EventsPage(page);
        eventsPage.goTo();
        Locator targetCard = eventsPage.findEventCard(eventTitle);
        int seatsNumBeforeBooking = eventsPage.getSeatsCount(targetCard);


        BookingFormPage bookingFormPage = eventsPage.proceedToBookingEvent(targetCard);
        bookingFormPage.fillAndConfirm(
                "Luis",
                "luis@example.com",
                "1112223344"
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
        page.waitForTimeout(3000);
        System.out.println("### Event Cards -> " + eventCardsAfterBooking.count());

        page.waitForTimeout(3000);
        Locator targetCardAfterBooking = eventCardsAfterBooking.filter(new Locator.FilterOptions().setHasText("Test QA"));
        assertThat(targetCardAfterBooking).isVisible();
        String seatsTextAfterBooking = targetCardAfterBooking.getByText("seats").innerText();

        System.out.println("#### After: " + seatsTextAfterBooking);

        //Obtain number after booking
        int seatsNumAfterBooking = Integer.parseInt(seatsTextAfterBooking.split(" ")[0]);

        //Validate decrease number of seats available
        Assert.assertTrue(seatsNumBeforeBooking > seatsNumAfterBooking);



    }

}
