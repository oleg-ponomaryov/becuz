package co.becuz.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import co.becuz.domain.Collection;

public class StatisticsDTO implements Serializable {

	private static final long serialVersionUID = 8886498347318331905L;

	@Getter
	@Setter
	private List<Collection> drafts;

	@Getter
	@Setter
	private List<Collection> initiated;

	@Getter
	@Setter
	private List<Collection> invitedTo;
}
