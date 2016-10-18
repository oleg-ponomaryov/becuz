package co.becuz.services;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.becuz.domain.Collection;
import co.becuz.domain.CollectionPhotos;
import co.becuz.domain.Photo;
import co.becuz.dto.CollectionDTO;
import co.becuz.dto.PhotoDTO;
import co.becuz.repositories.CollectionPhotosRepository;

@Service
public class CollectionPhotosServiceImpl implements CollectionPhotosService {

	@Autowired
	private CollectionPhotosRepository collectionPhotosRepository;

	@Autowired
	private PhotoService photoService;

	@Override
	public CollectionPhotos getCollectionPhotosById(String id) {
		return collectionPhotosRepository.findOne(id);
	}

	@Override
	public java.util.Collection<CollectionPhotos> getAllCollectionPhotosByCollection(
			Collection collection) {
		return collectionPhotosRepository.findAllByCollectionOrderByCreatedDesc(collection);
	}

	@Override
	public java.util.Collection<CollectionPhotos> getAllCollectionPhotosByPhoto(
			Photo photo) {
		return collectionPhotosRepository.findAllByPhotoOrderByCreatedDesc(photo);
	}

	@Override
	public CollectionPhotos getCollectionPhotosByPhotoAndCollection(
			Photo photo, Collection collection) {
		return collectionPhotosRepository.findByPhotoAndCollection(photo,
				collection);
	}

	@Override
	public void delete(String id) {
		collectionPhotosRepository.delete(id);
	}

	@Override
	public CollectionPhotos save(CollectionPhotos collectionPhotos) {
		return collectionPhotosRepository.save(collectionPhotos);
	}

	@Override
	public CollectionDTO getPhotosWithUrls(Collection collection) {
		java.util.Collection<CollectionPhotos> collectionPhotos = getAllCollectionPhotosByCollection(collection);
		CollectionDTO dto = new CollectionDTO();
		Set<PhotoDTO> photos = new HashSet<PhotoDTO>();

		for (CollectionPhotos c : collectionPhotos) {
			Photo p = c.getPhoto();
			PhotoDTO d = photoService.generateExpiringUrl(p, Photo.EXP_TIME_MILLIS);
			photos.add(d);
		}

		dto.setCollection(collection);
		dto.setPhotos(photos);
		return dto;
	}
}
