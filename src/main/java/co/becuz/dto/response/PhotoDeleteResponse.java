package co.becuz.dto.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import co.becuz.domain.Collection;

public class PhotoDeleteResponse implements Serializable {

	private static final long serialVersionUID = 3736661828307312797L;

	@Getter
	@Setter
	private List<Collection> collections; 

	@Getter
	@Setter
	private String status; 

	@Getter
	@Setter
	private List<String> messages=new ArrayList<String>(); 
}
