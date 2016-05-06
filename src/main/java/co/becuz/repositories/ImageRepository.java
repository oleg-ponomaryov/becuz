package co.becuz.repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

import co.becuz.domain.Image;
import co.becuz.domain.User;

public interface ImageRepository extends JpaRepository<Image, String> {
    Collection<Image> findByOwner(User user);
    Image findByMd5Digest(String digest);
}
