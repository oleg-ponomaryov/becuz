package co.becuz.services;

import java.text.ParseException;
import java.util.List;

import co.becuz.domain.Photo;
import co.becuz.dto.PhotoDTO;
import co.becuz.dto.response.StaticImage;

public interface PhotoService {

	public Photo save(String bucket, String photoKey) throws ParseException;
	public PhotoDTO generateExpiringUrl(Photo photo, long expirationInMillis);
	public List<PhotoDTO> generateExpiringUrls(List<Photo> photos, long expirationInMillis);
	Iterable<Photo> listAllPhotos();
	Iterable<Photo> listAllPhotosForUser(String Id);
	List<StaticImage> getStaticImages(String bucket, String prefix);
}
