package org.csanchez.crochunterjava;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class CrocHunterJavaApplicationTests {

    @Test
    public void testMainEndpoint() {
        given()
          .when().get("/")
          .then()
             .statusCode(200)
             .body(is("hello"));
    }

    @Test
    public void testGreetingEndpoint() {
        given()
                .queryParam("code", 501)
                .when().get("/status")
                .then()
                .statusCode(501)
                .body(is(""));
    }

}
