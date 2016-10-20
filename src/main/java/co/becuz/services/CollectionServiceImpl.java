package co.becuz.services;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.becuz.domain.Collection;
import co.becuz.domain.User;
import co.becuz.domain.enums.CollectionStatus;
import co.becuz.domain.nottables.CurrentUser;
import co.becuz.dto.StatisticsDTO;
import co.becuz.repositories.CollectionRepository;
import co.becuz.repositories.UserRepository;

@Service
public class CollectionServiceImpl implements CollectionService {


    @Autowired
    private CollectionRepository collectionRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Override
    public Collection getCollectionById(String id) {
        return collectionRepository.findOne(id);
    }

    @Override
    public java.util.Collection<Collection> getAllCollectionsByUserId(String user_id) {
    	User user = userRepository.findOne(user_id);
    	if (user == null) {
    		throw new NoSuchElementException(String.format("User=%s not found", user_id));
    	}
        return collectionRepository.findAllByUserId(user.getId());
    }
    
    @Override
    public void delete(String id, CurrentUser currentUser) {
    	Collection col = getCollectionById(id);
    	if (col == null) {
    		throw new NoSuchElementException(String.format("Collection=%s not found", id));
    	}
    	if (!col.getUser().equals(currentUser.getUser())) {
    		throw new IllegalArgumentException(String.format("Attempt to delete not own collection"));
    	}
    	collectionRepository.delete(id);
    }

    @Override
    public Collection save(Collection collection) {
        return collectionRepository.save(collection);
    }
    
    @Override
    public Collection update(Collection collection) {
    	Collection col = getCollectionById(collection.getId());
    	if (col == null) {
    		throw new NoSuchElementException(String.format("Collection=%s not found", collection.getId()));
    	}
    	
    	if (collection.getHeadline() != null)
    		col.setHeadline(collection.getHeadline());
    	
    	if (collection.getOwnerType()!=null)
    		col.setOwnerType(collection.getOwnerType());

    	if (collection.getStatus()!=null)
    		col.setStatus(collection.getStatus());
    	
        return collectionRepository.save(col);
    }

	@Override
	public StatisticsDTO getStatistics(User user) {
		StatisticsDTO st = new StatisticsDTO();
		List<Collection> drafts;
		List<Collection> inits = new ArrayList<>();
		List<Collection> invited = new ArrayList<>();
		
		drafts = collectionRepository.findAllByUserIdAndStatusOrderByHeadline(user.getId(), CollectionStatus.DRAFT);
		if (drafts==null) {
			drafts = new ArrayList<>();
		}
		
		inits = collectionRepository.findAllByUserIdAndStatusOrderByHeadline(user.getId(), CollectionStatus.SHARED);
		if (inits==null) {
			inits = new ArrayList<>();
		}
		
		invited = collectionRepository.findInvitedTo(user);
		if (invited==null) {
			invited = new ArrayList<>();
		}
		st.setDrafts(drafts);
		st.setInitiated(inits);
		st.setInvitedTo(invited);
		return st;
	}
}
