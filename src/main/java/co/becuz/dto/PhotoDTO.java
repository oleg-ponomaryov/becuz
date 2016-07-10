package co.becuz.dto;

import java.io.Serializable;
import java.net.URL;

import lombok.Getter;
import lombok.Setter;

public class PhotoDTO implements Serializable {
	private static final long serialVersionUID = -3600281937042078536L;

	@Getter
	@Setter
	private String photoId;
	
	@Getter
	@Setter
	private URL expiringUrl;

	@Getter
	@Setter
	private URL expiringThumbnailUrl;
	
}
