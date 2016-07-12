package co.becuz.dto.response;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import co.becuz.dto.CollectionDTO;

public class PhotoSaveResponse implements Serializable {

	private static final long serialVersionUID = -1541675834277186577L;

	@Getter
	@Setter
	private CollectionDTO collectionPhotos; 

	@Getter
	@Setter
	private String status; 

	@Getter
	@Setter
	private String message; 
}
