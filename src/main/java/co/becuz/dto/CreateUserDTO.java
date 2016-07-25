package co.becuz.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import co.becuz.domain.User;

public class CreateUserDTO implements Serializable {

	private static final long serialVersionUID = 9086134307990744001L;

	@Getter
	@Setter
	private User user;

	@Getter
	@Setter
	private String appId;

	@Getter
	@Setter
	private String iv;
}
