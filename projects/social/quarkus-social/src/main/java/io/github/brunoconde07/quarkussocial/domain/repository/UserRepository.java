package io.github.brunoconde07.quarkussocial.domain.repository;

import io.github.brunoconde07.quarkussocial.domain.model.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped // Make UserRepository a singleton, injecting it in the global application scope
public class UserRepository implements PanacheRepository<User> {

}
