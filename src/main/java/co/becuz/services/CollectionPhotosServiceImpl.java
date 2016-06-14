package co.becuz.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.becuz.domain.Collection;
import co.becuz.domain.CollectionPhotos;
import co.becuz.domain.Photo;
import co.becuz.repositories.CollectionPhotosRepository;

@Service
public class CollectionPhotosServiceImpl implements CollectionPhotosService {
	
    @Autowired
    private CollectionPhotosRepository collectionPhotosRepository;

	
	@Override
	public CollectionPhotos getCollectionPhotosById(String id) {
		return collectionPhotosRepository.findOne(id);
	}

	@Override
	public java.util.Collection<CollectionPhotos> getAllCollectionPhotosByCollection(
			Collection collection) {
		return collectionPhotosRepository.findAllByCollection(collection);
	}

	@Override
	public java.util.Collection<CollectionPhotos> getAllCollectionPhotosByPhoto(
			Photo photo) {
		return collectionPhotosRepository.findAllByPhoto(photo);
	}

	@Override
	public CollectionPhotos getCollectionPhotosByPhotoAndCollection(
			Photo photo, Collection collection) {
		return collectionPhotosRepository.findByPhotoAndCollection(photo, collection);
	}

	@Override
	public void delete(String id) {
		collectionPhotosRepository.delete(id);
	}

	@Override
	public CollectionPhotos save(CollectionPhotos collectionPhotos) {
        return collectionPhotosRepository.save(collectionPhotos);
	}
}
