package co.becuz.repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

import co.becuz.domain.Photo;

public interface PhotoRepository extends JpaRepository<Photo, String> {
    Collection<Photo> findAllByOwnerId(String user_id);
    Photo findByMd5Digest(String digest);
}
