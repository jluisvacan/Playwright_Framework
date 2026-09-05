package pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.options.AriaRole;
import utils.ConfigU;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class LoginPage{

    Page page;
    String base_url;
    Properties prop;
    FileInputStream fis;

    private static final String email_placeHolder = "you@email.com";
    private static final String password_label = "Password";

    public LoginPage(Page page, String base_url){
        this.page = page;
        this.base_url = base_url;
    }

    public DashboardPage loginToApplication() throws IOException {

        PlaywrightAssertions.setDefaultAssertionTimeout(10000);

        //Enter to page
        page.navigate(base_url);

        //get website title
        System.out.println(page.title()); // EventHub — Discover & Book Events

        //assertions
        assertThat(page).hasTitle("EventHub — Discover & Book Events");


        //locators
        page.getByLabel(password_label).fill(ConfigU.getPassUser());
        page.getByPlaceholder(email_placeHolder).fill(ConfigU.getEmailUser());


        //action button
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign In")).click();


        return new DashboardPage(page);
    }

}
