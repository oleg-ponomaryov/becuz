package co.becuz.dto;

import java.io.Serializable;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import co.becuz.domain.Collection;

public class CollectionDTO implements Serializable {

	private static final long serialVersionUID = -4605272357843140719L;

	@Getter
	@Setter
	private Collection collection;
	
	@Getter
	@Setter
	private Set<PhotoDTO> photos;
}
