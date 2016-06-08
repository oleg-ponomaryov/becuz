package co.becuz.repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import co.becuz.domain.Collection;
import co.becuz.domain.Frame;
import co.becuz.domain.User;

public interface CollectionRepository extends JpaRepository<Collection, String> {
    java.util.Collection<Collection> findAllByOwnerType(String type);
    java.util.Collection<Collection> findAllByUser(User user);
    java.util.Collection<Collection> findAllByFrame(Frame frame);
}
