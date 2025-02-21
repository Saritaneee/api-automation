package apiauto;

import io.restassured.RestAssured;
import io.restassured.module.jsv.JsonSchemaValidator;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.testng.annotations.Test;

import java.io.File;
import java.util.HashMap;

import static io.restassured.RestAssured.*;

public class TestRegres {

    @Test
    public void testPositiveGetSingleUSerTest() {
        RestAssured.baseURI = "https://reqres.in/";

        int userId = 5;
        File file = new File("src/test/resources/jsonSchema/JSONSchema.json");

        given().when().get("api/users/" + userId)
                .then()
                .log().all()
                .assertThat().statusCode(200)
                .assertThat().body(JsonSchemaValidator.matchesJsonSchema(file));
    }

    @Test
    public void testPositiveGetListUSer() {
        RestAssured.baseURI = "https://reqres.in/";

        given().when().get("api/users?page=2")
                .then()
                .log().all()
                .assertThat().statusCode(200)
                .assertThat().body("total", Matchers.equalTo(12))
                .assertThat().body("page", Matchers.equalTo(2));
    }

    @Test
    public void testPositiveGetListUknown() {
        RestAssured.baseURI = "https://reqres.in/";

        given().when().get("api/unknown")
                .then()
                .log().all()
                .assertThat().statusCode(200)
                .assertThat().body("page", Matchers.equalTo(1))
                .assertThat().body("per_page", Matchers.equalTo(6));
    }

    @Test
    public void testPositiveGetSingleResources() {
        RestAssured.baseURI = "https://reqres.in/";

        int userId = 2;
        given().when().get("api/unknown/" + userId)
                .then()
                .log().all()
                .assertThat().statusCode(200);
    }

    @Test
    public void testPositivePostCreateNewUser() {
        RestAssured.baseURI = "https://reqres.in/";

        String valueName = "Tane";
        String valueJob = "Student";

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", valueName);
        jsonObject.put("job", valueJob);

        given().log().all()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .body(jsonObject.toString())
                .post("https://reqres.in/api/users")
                .then().log().all()
                .assertThat().statusCode(201)
                .assertThat().body("name", Matchers.equalTo(valueName))
                .assertThat().body("job", Matchers.equalTo(valueJob))
                .assertThat().body("$", Matchers.hasKey("id"))
                .assertThat().body("$", Matchers.hasKey("createdAt"));
    }

    @Test
    public void testPositivePutUser() {
        RestAssured.baseURI = "https://reqres.in/";

        int userId = 1;
        String newValueName = "Elysiana";
        String newValueJob  = "Teacher";
        String newValueEmail = "Elysiana@gmail.com";

        String fName = given().when().get("api/users/" + userId).getBody().jsonPath().get("data.first_name");
        String lName = given().when().get("api/users/" + userId).getBody().jsonPath().get("data.last_name");
        String avatar = given().when().get("api/users/" + userId).getBody().jsonPath().get("data.avatar");
        String email = given().when().get("api/users/" + userId).getBody().jsonPath().get("data.email");
        String job = given().when().get("api/users/" + userId).getBody().jsonPath().get("job");
        System.out.println("Name before: " + fName);
        System.out.println("Email before: " + email);
        System.out.println("Job before: " + job);

        HashMap <String, Object> bodyMap = new HashMap<>();
        bodyMap.put("id", userId);
        bodyMap.put("email", newValueEmail);
        bodyMap.put("job", newValueJob);
        bodyMap.put("avatar", avatar);
        bodyMap.put("first_name", newValueName);
        bodyMap.put("last_name", lName);
        JSONObject jsonObject = new JSONObject(bodyMap);

        given().log().all()
                .header("Content-Type", "application/json")
                .body(jsonObject.toString())
                .put("api/users/" + userId)
                .then().log().all()
                .assertThat().statusCode(200)
                .assertThat().body("first_name", Matchers.equalTo(newValueName))
                .assertThat().body("job", Matchers.equalTo(newValueJob))
                .assertThat().body("email", Matchers.equalTo(newValueEmail));
    }

    @Test
    public void testPositivePatchUser() {
        RestAssured.baseURI = "https://reqres.in/";

        int userId = 2;
        String newValueName = "Veronica";

        String fName = given().when().get("api/users/" + userId).getBody().jsonPath().get("data.first_name");
        System.out.println("Name before: " + fName);

        HashMap <String, Object> bodyMap = new HashMap<>();
        bodyMap.put("first_name", newValueName);
        JSONObject jsonObject = new JSONObject(bodyMap);

        given().log().all()
                .header("Content-Type", "application/json")
                .body(jsonObject.toString())
                .patch("api/users/" + userId)
                .then().log().all()
                .assertThat().statusCode(200)
                .assertThat().body("first_name", Matchers.equalTo(newValueName));
    }

    @Test
    public void testPositiveDeleteUser() {
        RestAssured.baseURI = "https://reqres.in/";

        int userId = 4;

        given().log().all()
                .when().delete("api/users/" + userId)
                .then().log().all()
                .assertThat().statusCode(204);
    }

    @Test
    public void testPositiveRegisterSuccess() {
        RestAssured.baseURI = "https://reqres.in/";

        String valueEmail = "eve.holt@reqres.in";
        String valuePassword = "pistol";

        HashMap <String, Object> bodyMap = new HashMap<>();
        bodyMap.put("email", valueEmail);
        bodyMap.put("password", valuePassword);
        JSONObject jsonObject = new JSONObject(bodyMap);

        given()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .body(jsonObject.toString())
                .when().post("https://reqres.in/api/register/")
                .then().log().all()
                .assertThat().statusCode(200)
                .assertThat().body("$", Matchers.hasKey("id"))
                .assertThat().body("$", Matchers.hasKey("token"));
    }

    @Test
    public void testPositiveLoginSuccess() {
        RestAssured.baseURI = "https://reqres.in/";

        String valueEmail = "eve.holt@reqres.in";
        String valuePassword = "cityslicka";

        HashMap <String, Object> bodyMap = new HashMap<>();
        bodyMap.put("email", valueEmail);
        bodyMap.put("password", valuePassword);
        JSONObject jsonObject = new JSONObject(bodyMap);

        given()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .body(jsonObject.toString())
                .when().post("https://reqres.in/api/login/")
                .then().log().all()
                .assertThat().statusCode(200)
                .assertThat().body("$", Matchers.hasKey("token"));
    }

    //TEST NEGATIVE
    @Test
    public void testNegativeGetSingleUser() {
        RestAssured.baseURI = "https://reqres.in/";

        int userId = 102;

        given().when().get("api/users/" + userId)
                .then()
                .log().all()
                .assertThat().statusCode(404)
                .assertThat().body("$", Matchers.anEmptyMap());
    }

    @Test
    public void testNegativeSingleResourcesUnknown() {
        RestAssured.baseURI = "https://reqres.in/";

        int userId = 23;

        given().when().get("api/unknown/" + userId)
                .then()
                .log().all()
                .assertThat().statusCode(404);
    }

    @Test
    public void testNegativeRegisterUnsuccessful() {
        RestAssured.baseURI = "https://reqres.in/";

        String valueEmail = "tested_email@gmail.com";

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("email", valueEmail);

        given()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .body(jsonObject.toString())
                .when().post("https://reqres.in/api/register/")
                .then().log().all()
                .assertThat().statusCode(400)
                .assertThat().body("$", Matchers.hasKey("error"));
    }

    @Test
    public void testNegativeloginUnsuccessful() {
        RestAssured.baseURI = "https://reqres.in/";

        String valueEmail = "tested_email@gmail.com";

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("email", valueEmail);

        given()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .body(jsonObject.toString())
                .when().post("https://reqres.in/api/login/")
                .then().log().all()
                .assertThat().statusCode(400)
                .assertThat().body("$", Matchers.hasKey("error"));
    }
}
