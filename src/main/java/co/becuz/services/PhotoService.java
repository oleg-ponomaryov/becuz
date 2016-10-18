package co.becuz.services;

import java.text.ParseException;
import java.util.Collection;
import java.util.List;

import co.becuz.domain.Photo;
import co.becuz.domain.nottables.CurrentUser;
import co.becuz.dto.PhotoDTO;
import co.becuz.dto.response.PhotoDeleteResponse;
import co.becuz.dto.response.PhotoSaveResponse;

public interface PhotoService {

	PhotoDTO generateExpiringUrl(Photo photo, long expirationInMillis);
	List<PhotoDTO> generateExpiringUrls(List<Photo> photos, long expirationInMillis);
	Collection<Photo> listAllPhotos();
	Collection<Photo> listAllPhotosForUser(String Id);
	List<PhotoDTO> getStaticImages(String bucket, String prefix);
	PhotoSaveResponse save(String bucket, String photoKey, String collectionId,
			CurrentUser user) throws ParseException;
	
	Photo save(Photo photo);
	PhotoDeleteResponse delete(Collection<PhotoDTO> photos, CurrentUser user);
	Photo update(PhotoDTO photo);
}
