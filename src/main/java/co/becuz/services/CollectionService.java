package co.becuz.services;

import co.becuz.domain.Collection;

public interface CollectionService {

	Collection getCollectionById(String id);

    java.util.Collection<Collection> getAllCollectionsByUserId(String user_id);

	void delete(String id);

	Collection save(Collection collection);

	Collection update(Collection collection);
}
