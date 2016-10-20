package co.becuz.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import co.becuz.domain.Collection;
import co.becuz.domain.User;
import co.becuz.domain.enums.CollectionStatus;

public interface CollectionRepository extends JpaRepository<Collection, String> {
    java.util.Collection<Collection> findAllByUserId(String user_id);
    java.util.Collection<Collection> findAllByFrameId(String frame_id);
    List<Collection> findAllByUserIdAndStatusOrderByHeadline(String user_id, CollectionStatus status);
    
    @Transactional
    @Query("select distinct c.collection from CollectionPhotos c " +
            "where c.photo IN (select p from Photo p where p.owner = :user) and c.collection.user NOT IN :user order by c.collection.headline")
    List<Collection> findInvitedTo(@Param("user") User user);
}
