package co.becuz.dto.response;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import co.becuz.domain.CollectionPhotos;

public class PhotoSaveResponse implements Serializable {

	private static final long serialVersionUID = -1541675834277186577L;

	@Getter
	@Setter
	private CollectionPhotos collectionPhotos; 

	@Getter
	@Setter
	private String status; 

	@Getter
	@Setter
	private String message; 
}
