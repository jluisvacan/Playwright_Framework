package tests;

import com.jayway.jsonpath.JsonPath;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.ConfigU;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
public class APITest {

    //Global variables
    Playwright playwright;


    @Test
    public void e2eApiTest() throws IOException {



        // LOGIN
        // Request body
        HashMap<Object,Object> loginPayload = new HashMap<>();
        loginPayload.put("email", ConfigU.getEmailUser());
        loginPayload.put("password",ConfigU.getPassUser());

        // Post request
        playwright = Playwright.create();
        APIRequestContext apiRequest = playwright.request().newContext();
        APIResponse loginResponse = apiRequest.post("https://api.eventhub.rahulshettyacademy.com/api/auth/login",
                RequestOptions.create().setData(loginPayload));

        // Validate success response
        Assert.assertTrue(loginResponse.ok());

        //response body to string
        System.out.println(loginResponse.text());

        // extract token to response
        String token = JsonPath.read(loginResponse.text(), "$.token"); // $ -> root
        System.out.println("Login success " + token);



        // CREATE EVENT
        // payload event
        HashMap<Object,Object> createEventPayload = new HashMap<>();
        createEventPayload.put("title", "Playwright Event Title");
        createEventPayload.put("description", "Api Test");
        createEventPayload.put("category", "Conference");
        createEventPayload.put("venue", "Test road");
        createEventPayload.put("city", "Test");
        createEventPayload.put("eventDate", "2026-09-15T05:41:00.000Z");
        createEventPayload.put("price", 10);
        createEventPayload.put("totalSeats", 25);

        //send request
        APIResponse eventResponse = apiRequest.post("https://api.eventhub.rahulshettyacademy.com/api/events",
                RequestOptions.create().setHeader("Authorization", "Bearer " + token)
                        .setData(createEventPayload));

        //validate success response
        Assert.assertTrue(eventResponse.ok(), "Create Event API method should succeed");

        //grab id
        int eventId = JsonPath.read(eventResponse.text(), "$.data.id");
        System.out.println("Event created and its ID is: " + eventId);


        //GET EVENT
        //send request
        APIResponse retrieveEvents = apiRequest.get("https://api.eventhub.rahulshettyacademy.com/api/events",
                RequestOptions.create()
                        .setQueryParam("page", "1")
                        .setQueryParam("limit", "12")
                        .setHeader("Authorization", "Bearer " + token)
        );
        Assert.assertTrue(retrieveEvents.ok(), "Event Retrieval API should succeed");

        //find the new id event in the list
        List<Integer> allEventIds = JsonPath.read(retrieveEvents.text(), "$.data[*].id");
        Assert.assertTrue(allEventIds.contains(eventId), "Created event should appear in events list");


        //DELETE EVENT

        //send request
        APIResponse deleteResponse = apiRequest.delete("https://api.eventhub.rahulshettyacademy.com/api/events/" + eventId, RequestOptions.create()
                .setHeader("Authorization","Bearer " + token)
        );

        Assert.assertTrue(deleteResponse.ok());

        //Verify deletion is success
        APIResponse verifyRes = apiRequest.get("https://api.eventhub.rahulshettyacademy.com/api/events",
                RequestOptions.create()
                        .setQueryParam("page", "1")
                        .setQueryParam("limit", "12")
                        .setHeader("Authorization", "Bearer " + token)
        );
        Assert.assertTrue(verifyRes.ok(), "Post-delete events list call should succeed");

        List<String> idsAfterDelete = JsonPath.read(verifyRes.text(), "$.data[*].id");
        Assert.assertFalse(idsAfterDelete.contains(eventId), "Deleted event must no longer appear in the events");
    }
}
