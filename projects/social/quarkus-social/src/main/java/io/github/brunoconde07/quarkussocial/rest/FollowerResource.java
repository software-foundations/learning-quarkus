package io.github.brunoconde07.quarkussocial.rest;

import io.github.brunoconde07.quarkussocial.domain.model.Follower;
import io.github.brunoconde07.quarkussocial.domain.model.User;
import io.github.brunoconde07.quarkussocial.domain.repository.FollowerRepository;
import io.github.brunoconde07.quarkussocial.domain.repository.UserRepository;
import io.github.brunoconde07.quarkussocial.rest.dto.FollowerRequest;
import io.github.brunoconde07.quarkussocial.rest.dto.FollowerResponse;
import io.github.brunoconde07.quarkussocial.rest.dto.FollowersPerUseResponse;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("/users/{userId}/followers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FollowerResource {

    private FollowerRepository repository;
    private UserRepository userRepository;

    @Inject
    public FollowerResource(
            FollowerRepository repository,
            UserRepository userRepository) {

        this.repository = repository;

        this.userRepository = userRepository;
    }

    @PUT
    @Transactional
    public Response followUser(
            @PathParam("userId") Long userId, FollowerRequest request) {

        if ( userId.equals(request.getFollowerId()) ) {

            return Response.status(Response.Status.CONFLICT)
                    .entity("You cannot follow yourself")
                    .build();
        }

        User user = userRepository.findById(userId);

        if ( user == null ) {

            return Response.status(Response.Status.NOT_FOUND).build();
        }

        User follower = userRepository.findById(request.getFollowerId());

        boolean follows = repository.follows(follower, user);

        if ( ! follows ) {

            Follower entity = new Follower();

            entity.setUser(user);

            entity.setFollower(follower);

            repository.persist(entity);
        }

        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @GET
    public Response listFollowers( @PathParam("userId") Long userId ) {

        User user = userRepository.findById(userId);

        if ( user == null ) {

            return Response.status(Response.Status.NOT_FOUND).build();
        }

        List<Follower> list = repository.findByUser(userId);

        FollowersPerUseResponse responseObject = new FollowersPerUseResponse();

        responseObject.setFollowersCount(list.size());

        // ::new makes pass the reference of the constructor of the FollowerResponse to
        // the argument of the map method
        List<FollowerResponse> followerList = list.stream()
                .map(FollowerResponse::new)
                .collect(Collectors.toList());

        responseObject.setContent(followerList);

        return Response.ok(responseObject).build();
    }

}
