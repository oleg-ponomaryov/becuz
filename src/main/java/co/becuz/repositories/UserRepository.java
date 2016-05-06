package co.becuz.repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

import co.becuz.domain.User;
import co.becuz.social.SocialMediaTypes;

public interface UserRepository extends JpaRepository<User, String> {
    Collection<User> findAllByEmail(String email);
    User findByUsernameAndSigninprovider(String name, SocialMediaTypes type);
}
