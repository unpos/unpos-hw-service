package ru.unpos.hw.service;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class TestResourceTest {

    @Test
    public void testHelloEndpoint() {
        //given()
          //.when().get("/api/test")
          //.then()
             //.statusCode(200);
             //.body(is("Hello RESTEasy"));
    }

}