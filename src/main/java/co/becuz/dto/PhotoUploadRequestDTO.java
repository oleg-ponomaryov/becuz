package co.becuz.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import co.becuz.domain.Collection;

public class PhotoUploadRequestDTO implements Serializable {
	private static final long serialVersionUID = 660295355066807665L;
	@Getter
	@Setter
	private String collectionId;

	@Getter
	@Setter
	private Collection collection;
}
