package io.github.brunoconde07.quarkussocial.rest;

import io.github.brunoconde07.quarkussocial.domain.model.User;
import io.github.brunoconde07.quarkussocial.domain.repository.UserRepository;
import io.github.brunoconde07.quarkussocial.rest.dto.CreatePostRequest;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.transaction.Transactional;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@TestHTTPEndpoint(PostResource.class) // allows us do not pass the url in the http request test
class PostResourceTest {

    @Inject
    UserRepository userRepository;
    Long userId;

    @BeforeEach
    @Transactional
    public void setUP() {

        User user = new User();

        user.setName("John");

        user.setAge(20);

        userRepository.persist(user);

        userId = user.getId();

    }

    @Test
    @DisplayName("should create a post for a user")
    public void createPostTest() {

        CreatePostRequest postRequest = new CreatePostRequest();

        postRequest.setText("Some text");

        given()
                .contentType(ContentType.JSON)
                .body(postRequest)
                .pathParam("userId", userId)
                .when()
                .post()
                .then()
                .statusCode(201);
    }

    @Test
    @DisplayName("should return 404 when trying to make a post for a non existent user")
    public void postForNonExistentUserTest() {

        CreatePostRequest postRequest = new CreatePostRequest();

        postRequest.setText("Some text");

        var nonExistentUserId = 999;

        given()
                .contentType(ContentType.JSON)
                .body(postRequest)
                .pathParam("userId", nonExistentUserId)
                .when()
                .post()
                .then()
                .statusCode(404);
    }
}