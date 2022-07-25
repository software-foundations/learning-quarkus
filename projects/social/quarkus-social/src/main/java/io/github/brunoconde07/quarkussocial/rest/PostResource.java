package io.github.brunoconde07.quarkussocial.rest;

import io.github.brunoconde07.quarkussocial.domain.model.Post;
import io.github.brunoconde07.quarkussocial.domain.model.User;
import io.github.brunoconde07.quarkussocial.domain.repository.FollowerRepository;
import io.github.brunoconde07.quarkussocial.domain.repository.PostRepository;
import io.github.brunoconde07.quarkussocial.domain.repository.UserRepository;
import io.github.brunoconde07.quarkussocial.rest.dto.CreatePostRequest;
import io.github.brunoconde07.quarkussocial.rest.dto.PostResponse;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Sort;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Path("/users/{userId}/posts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PostResource {

    private UserRepository userRepository;
    private PostRepository repository;
    private FollowerRepository followerRepository;

    @Inject
    public PostResource(
            UserRepository userRepository,
            PostRepository repository,
            FollowerRepository followerRepository) {

        this.userRepository = userRepository;
        this.repository = repository;
        this.followerRepository = followerRepository;
    }

    @POST
    @Transactional
    public Response savePost(@PathParam("userId") Long userId, CreatePostRequest request) {

        User user = userRepository.findById(userId);

        if ( user == null ) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Post post = new Post();

        post.setText(request.getText());

        post.setUser(user);

        // Unecessary since @PrePersist in Post
        // post.setDateTime(LocalDateTime.now());

        repository.persist(post);

        return Response.status(Response.Status.CREATED).build();
    }

    @GET
    public Response listPost(
            @PathParam("userId") Long userId,
            @HeaderParam("followerId") Long followerId  ) {

        User user = userRepository.findById(userId);

        if ( user == null ) {

            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if ( followerId == null ) {

            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("You forgot the header followerId")
                    .build();
        }

        User follower = userRepository.findById(followerId);

        if ( follower == null ) {

            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("Inexistent FollowerId")
                    .build();

        }

        boolean follows = followerRepository.follows(follower, user);

        if ( ! follows ) {

            return Response
                    .status(Response.Status.FORBIDDEN)
                    .entity("You cannot see these posts")
                    .build();

        }

        PanacheQuery<Post> query = repository.find(
                "user",
                Sort.by("date_time", Sort.Direction.Descending),
                user);

        List<Post> list = query.list();

        List<PostResponse> postResponseList = list.stream()
//                .map(post -> PostResponse.fromEntity(post)) // works the same of above
                .map(PostResponse::fromEntity)
                .collect(Collectors.toList());

        return Response.ok(postResponseList).build();
    }
}
