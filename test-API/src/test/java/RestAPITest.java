import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import jdk.jfr.Description;
import org.testng.Assert;
import org.testng.annotations.Test;

public class RestAPITest {

    private static final String BASE_URL = "https://restful-api.dev";
    private static String objectId = null;

    @Test(priority = 1)
    @Description("Validate the GET request")
    public void testGetRequest(){
        Response response = RestAssured.get(BASE_URL);
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 200, "Unexpected status code");
        String responseBody = response.getBody().asString();
        Assert.assertTrue(responseBody.contains("title"), "Response body doesn't contain expected content");
    }

    @Test(priority = 2)
    @Description("Validate the POST request")
    public void testPostRequest() {

        String requestBody = "{\n" +
                "   \"name\": \"Apple MacBook Pro 16\",\n" +
                "   \"data\": {\n" +
                "      \"year\": 2019,\n" +
                "      \"price\": 1849.99,\n" +
                "      \"CPU model\": \"Intel Core i9\",\n" +
                "      \"Hard disk size\": \"1 TB\"\n" +
                "   }\n" +
                "}";

        Response response = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("https://api.restful-api.dev/objects");

        System.out.println(response.getBody());
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 200, "Unexpected status code");
        String responseBody = response.getBody().asString();
        Assert.assertTrue(responseBody.contains("Apple MacBook Pro 16"), "Response body doesn't contain expected content");
        objectId = response.jsonPath().getString("id");
        System.out.println("Created ID: " + objectId);
    }

    @Test(priority = 4)
    @Description("Validate the DELETE request")
    public void testDeleteRequest() {
        Response response = RestAssured
                .when()
                .delete("https://api.restful-api.dev/objects/" + objectId);

        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 200, "Unexpected status code");
        String responseBody = response.getBody().asString();
        Assert.assertEquals(response.jsonPath().getString("message"), "Object with id = " + objectId + " has been deleted.", "Incorrect deletion message");
    }
}
