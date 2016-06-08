package co.becuz.repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import co.becuz.domain.Collection;
import co.becuz.domain.CollectionPhotos;
import co.becuz.domain.Photo;

public interface CollectionPhotosRepository extends JpaRepository<CollectionPhotos, String> {
    java.util.Collection<CollectionPhotos> findAllByCollection(Collection collection);
    java.util.Collection<CollectionPhotos> findAllByPhoto(Photo photo);
    CollectionPhotos findByPhotoAndCollection(Photo photo, Collection collection);
}
