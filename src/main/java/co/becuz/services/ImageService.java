package co.becuz.services;

import java.text.ParseException;
import java.util.List;

import co.becuz.domain.Image;
import co.becuz.dto.ImageDTO;
import co.becuz.dto.response.StaticImage;

public interface ImageService {

	public Image save(String bucket, String imageKey) throws ParseException;
	public ImageDTO generateExpiringUrl(Image image, long expirationInMillis);
	public List<ImageDTO> generateExpiringUrls(List<Image> image, long expirationInMillis);
	Iterable<Image> listAllImages();
	Iterable<Image> listAllImagesForUser(String Id);
	List<StaticImage> getStaticImages(String bucket, String prefix);
}
