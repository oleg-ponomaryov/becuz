package co.becuz.repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

import co.becuz.domain.Photo;
import co.becuz.domain.User;

public interface PhotoRepository extends JpaRepository<Photo, String> {
    Collection<Photo> findAllByOwner(User user);
    Photo findByMd5Digest(String digest);
}
