package co.becuz.services;

import co.becuz.domain.Collection;
import co.becuz.domain.User;
import co.becuz.domain.nottables.CurrentUser;
import co.becuz.dto.StatisticsDTO;

public interface CollectionService {

	Collection getCollectionById(String id);

    java.util.Collection<Collection> getAllCollectionsByUserId(String user_id);

	Collection save(Collection collection);

	Collection update(Collection collection);

	void delete(String id, CurrentUser currentUser);
	
	StatisticsDTO getStatistics(User user);
}
