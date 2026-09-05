package pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class BookingFormPage {
    Page page;
    private static final String FULL_NAME_INPUT = "Full Name";
    private static final String EMAIL_LABEL = "#customer-email";
    private static final String NUMBER_PLACEHOLDER = "+91 98765 43210";
    private static final String CONFIRM_BTN = "Confirm Booking";


    public BookingFormPage(Page page){
        this.page = page;
    }


    public void fillAndConfirm(String fullName, String email, String phoneNumber){
        page.getByLabel(FULL_NAME_INPUT).fill(fullName);
        page.locator(EMAIL_LABEL).fill(email);
        page.getByPlaceholder(NUMBER_PLACEHOLDER).fill(phoneNumber);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(CONFIRM_BTN)).click();

        assertThat(page.getByText("Your tickets are reserved.")).isVisible();
    }


}
