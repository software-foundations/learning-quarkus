package io.github.brunoconde07.quarkussocial.domain.repository;

import io.github.brunoconde07.quarkussocial.domain.model.Follower;
import io.github.brunoconde07.quarkussocial.domain.model.User;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;

import javax.enterprise.context.ApplicationScoped;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@ApplicationScoped
public class FollowerRepository implements PanacheRepository<Follower> {

    public boolean follows(User follower, User user){

        // way 01 to pass parameters
//        Map<String, Object> params = new HashMap<>();
//        params.put("follower", follower);
//        params.put("user", user);

        // way 02 to pass parameters
        Map<String, Object> params = Parameters
                .with("follower", follower)
                .and("user", user)
                .map();

        // find comes from inheritance
        // :parameter must be together
        // : parameter does not work
        PanacheQuery<Follower> query = find("follower = :follower and user = :user", params);

        // They work similar
//        Follower result = query.firstResult();

        Optional<Follower> result = query.firstResultOptional();

        return result.isPresent();

    }

    public List<Follower> findByUser(Long userId) {

        PanacheQuery<Follower> query = find("user.id", userId);

        return query.list();

    }

    public void deleteByFollowerAndUser(Long followerId, Long userId) {
        Map<String, Object> params = Parameters
                .with("userId", userId)
                .and("followerId", followerId)
                .map();

        delete("follower.id =:followerId and user.id =:userId", params);
    }
}
