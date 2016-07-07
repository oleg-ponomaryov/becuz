package co.becuz.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import co.becuz.domain.Collection;

public interface CollectionRepository extends JpaRepository<Collection, String> {
    java.util.Collection<Collection> findAllByUserId(String user_id);
    java.util.Collection<Collection> findAllByFrameId(String frame_id);
}
