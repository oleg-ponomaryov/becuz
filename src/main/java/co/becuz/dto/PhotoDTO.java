package co.becuz.dto;

import java.io.Serializable;
import java.net.URL;

import co.becuz.domain.User;
import lombok.Getter;
import lombok.Setter;

public class PhotoDTO implements Serializable {
	private static final long serialVersionUID = -3600281937042078536L;

	@Getter
	@Setter
	private String id;
	
	@Getter
	@Setter
	private URL expiringUrl;

	@Getter
	@Setter
	private URL expiringThumbnailUrl;
	
	@Getter
	@Setter
	private String md5Digest;

	@Getter
	@Setter
    private String caption;

	@Getter
	@Setter
    private User owner;
}
