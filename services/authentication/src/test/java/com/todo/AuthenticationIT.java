package com.todo;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.todo.repository.TokenRepository;
import com.todo.repository.UserRepository;
import com.todo.web.contract.AuthToken;
import com.todo.web.contract.LoginData;
import com.todo.web.contract.NewUserData;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.PostConstruct;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AuthenticationApplication.class)
@WebAppConfiguration
@IntegrationTest({"server.port=0", "management.port=0", "token.expire.inSeconds=10"})
public class AuthenticationIT {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenRepository tokenRepository;

    @Value("${local.server.port}")
    private int port;

    @PostConstruct
    public void postConstruct() {
        RestAssured.port = port;
    }

    @Before
    public void before() {
        tokenRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @Test
    public void shouldFailCreatingUserWhenNoDataSent() throws Exception {
        given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .content("{}")
        .when()
                .post("/user")
        .then()
                .statusCode(422)
                .contentType(ContentType.JSON)
                .body("errors", not(empty()));
    }

    @Test
    public void shouldFailCreatingUserWhenTooShortPassword() throws Exception {
        given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .content(new NewUserData("loginLongEnough", "email@domain.com", "below8"))
        .when()
                .post("/user")
        .then()
                .statusCode(422)
                .contentType(ContentType.JSON)
                .body("errors", not(empty()));
    }

    @Test
    public void shouldFailCreatingUserWhenEmailIncorrect() throws Exception {
        given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .content(new NewUserData("loginLongEnough", "email_domain_com", "above8signs"))
        .when()
                .post("/user")
        .then()
                .statusCode(422)
                .contentType(ContentType.JSON)
                .body("errors", not(empty()));
    }

    @Test
    public void shouldFailCreatingUserWhenLoginIncorrect() throws Exception {
        given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .content(new NewUserData("lg", "email@domain.com", "above8signs"))
                .when()
                .post("/user")
                .then()
                .statusCode(422)
                .contentType(ContentType.JSON)
                .body("errors", not(empty()));
    }

    @Test
    public void shouldCreateUserAccountLoginAndValidateToken() throws Exception {
        //create account
        AuthToken authToken =
            given()
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .content(new NewUserData("loginLongEnough", "email@domain.com", "above8signs"))
            .when()
                    .post("/user")
            .then()
                    .statusCode(200)
                    .body("token", not(isEmptyString()))
                    .extract().as(AuthToken.class);

        //validate token of new created account
        given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .content(authToken)
        .when()
                .post("/authentication/validateToken")
        .then()
                .statusCode(200)
                .body("uid", not(isEmptyString()));

        //prevent duplicate login account
        given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .content(new NewUserData("loginLongEnough", "email2@domain.com", "awerfgwgwbove8signs"))
        .when()
                .post("/user")
        .then()
                .statusCode(422)
                .contentType(ContentType.JSON)
                .body("errors", not(empty()));

        //prevent duplicate email account
        given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .content(new NewUserData("loginLongEnough2", "email@domain.com", "awerfgwgwbove8signs"))
                .when()
                .post("/user")
                .then()
                .statusCode(422)
                .contentType(ContentType.JSON)
                .body("errors", not(empty()));

        //login to get new token
        authToken =
                given()
                        .accept(ContentType.JSON)
                        .contentType(ContentType.JSON)
                        .content(new LoginData("loginLongEnough", "above8signs"))
                .when()
                        .post("/authentication/login")
                .then()
                        .statusCode(200)
                        .body("token", not(isEmptyString()))
                        .extract().as(AuthToken.class);

        //validate token from login
        given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .content(authToken)
        .when()
                .post("/authentication/validateToken")
        .then()
                .statusCode(200)
                .body("uid", not(isEmptyString()));

        //wait for token expiration
        Thread.currentThread().sleep(12 * 1000);

        given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .content(authToken)
        .when()
                .post("/authentication/validateToken")
        .then()
                .statusCode(422)
                .body("errors", not(empty()));
    }

    @Test
    public void shouldFailLoggingWhenNoDataSent() throws Exception {
        given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .content("{}")
        .when()
                .post("/authentication/login")
        .then()
                .statusCode(422)
                .contentType(ContentType.JSON)
                .body("errors", not(empty()));
    }

    @Test
    public void shouldFailLoggingWhenTooShortPassword() throws Exception {
        given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .content(new LoginData("email@domain.com", "below8"))
        .when()
                .post("/authentication/login")
        .then()
                .statusCode(422)
                .contentType(ContentType.JSON)
                .body("errors", not(empty()));
    }

    @Test
    public void shouldFailLoggingWhenLoginIncorrect() throws Exception {
        given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .content(new LoginData("below8", "above8signs"))
        .when()
                .post("/authentication/login")
        .then()
                .statusCode(422)
                .contentType(ContentType.JSON)
                .body("errors", not(empty()));
    }

    @Test
    public void shouldFailTokenValidationWhenNoDataSent() throws Exception {
        given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .content("{}")
        .when()
                .post("/authentication/validateToken")
        .then()
                .statusCode(422)
                .contentType(ContentType.JSON)
                .body("errors", not(empty()));
    }

    @Test
    public void shouldFailTokenValidationWhenTokenBlank() throws Exception {
        given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .content(new AuthToken("  "))
        .when()
                .post("/authentication/validateToken")
        .then()
                .statusCode(422)
                .contentType(ContentType.JSON)
                .body("errors", not(empty()));
    }

    @Test
    public void shouldFailTokenValidationWhenTokenNotExist() throws Exception {
        given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .content(new AuthToken("incorrectToken"))
        .when()
                .post("/authentication/validateToken")
        .then()
                .statusCode(422)
                .contentType(ContentType.JSON)
                .body("errors", not(empty()));
    }

    @Test
    public void shouldFailTokenValidationWhenUnrecognizedProperty() throws Exception {
        given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .content("{\"unrecognized\":\"property\"}")
        .when()
                .post("/authentication/validateToken")
        .then()
                .statusCode(422)
                .contentType(ContentType.JSON)
                .body("errors", not(empty()));
    }
}
