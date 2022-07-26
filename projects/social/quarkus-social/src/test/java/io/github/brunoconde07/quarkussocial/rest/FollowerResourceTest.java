package io.github.brunoconde07.quarkussocial.rest;

import io.github.brunoconde07.quarkussocial.domain.model.Follower;
import io.github.brunoconde07.quarkussocial.domain.model.User;
import io.github.brunoconde07.quarkussocial.domain.repository.FollowerRepository;
import io.github.brunoconde07.quarkussocial.domain.repository.UserRepository;
import io.github.brunoconde07.quarkussocial.rest.dto.FollowerRequest;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@TestHTTPEndpoint(FollowerResource.class)
class FollowerResourceTest {

    @Inject
    UserRepository userRepository;

    @Inject
    FollowerRepository followerRepository;

    Long userId;

    Long followerId;

    @BeforeEach
    @Transactional
    void setUp() {

        // default user of the tests
        User user = new User();

        user.setName("John");

        user.setAge(20);

        userRepository.persist(user);

        userId = user.getId();

        // default follower of the tests
        User follower = new User();

        follower.setName("Mary");

        follower.setAge(35);

        userRepository.persist(follower);

        followerId = follower.getId();

        //
        Follower followerEntity = new Follower();

        followerEntity.setFollower(follower);

        followerEntity.setUser(user);

        followerRepository.persist(followerEntity);
    }

    @Test
    @DisplayName("should return 409 when Follower Id is equal to User Id")
    public void sameUserAsFollowerTest() {

        FollowerRequest body = new FollowerRequest();

        body.setFollowerId(userId);

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .pathParam("userId", userId)
                .when()
                .put()
                .then()
                .statusCode(Response.Status.CONFLICT.getStatusCode())
                .body(Matchers.is("You cannot follow yourself"));


    }

    @Test
    @DisplayName("should return 404 when follow a user Id does not exist")
    public void userNotFoundWhenTryingToFollowTest() {

        FollowerRequest body = new FollowerRequest();

        body.setFollowerId(userId);

        int nonExistentUserId = 999;

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .pathParam("userId", nonExistentUserId)
                .when()
                .put()
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    @DisplayName("should follow user")
    public void followUserTest() {

        FollowerRequest body = new FollowerRequest();

        body.setFollowerId(followerId);

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .pathParam("userId", userId)
                .when()
                .put()
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());
    }

    @Test
    @DisplayName("should return 404 on list user followers and user Id does not exist")
    public void userNotFoundWhenListToFollowersTest() {

        int nonExistentUserId = 999;

        given()
                .contentType(ContentType.JSON)
                .pathParam("userId", nonExistentUserId)
                .when()
                .get()
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    @DisplayName("should list a user followers")
    public void listFollowersTest() {

        var response = given()
                .contentType(ContentType.JSON)
                .pathParam("userId", userId)
                .when()
                .get()
                .then()
                .extract().response();

        var followersCount = response.jsonPath().get("followersCount");

        var followersContent = response.jsonPath().getList("content");

        assertEquals(Response.Status.OK.getStatusCode(), response.statusCode());

        assertEquals(1, followersCount);

        assertEquals(1, followersContent.size());
    }
}