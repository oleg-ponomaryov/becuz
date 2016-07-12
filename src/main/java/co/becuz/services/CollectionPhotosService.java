package co.becuz.services;

import java.util.Collection;

import co.becuz.domain.CollectionPhotos;
import co.becuz.domain.Photo;
import co.becuz.dto.CollectionDTO;

public interface CollectionPhotosService {
	CollectionPhotos getCollectionPhotosById(String id);

    Collection<CollectionPhotos> getAllCollectionPhotosByCollection(co.becuz.domain.Collection collection);

    Collection<CollectionPhotos> getAllCollectionPhotosByPhoto(Photo photo);

    CollectionPhotos getCollectionPhotosByPhotoAndCollection(Photo photo, co.becuz.domain.Collection collection);
    
	void delete(String id);

	CollectionPhotos save(CollectionPhotos collectionPhotos);

	CollectionDTO getPhotosWithUrls(co.becuz.domain.Collection collection);
}
