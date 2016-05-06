package co.becuz.dto;

import java.io.Serializable;
import java.net.URL;

public class ImageDTO implements Serializable {
	private String imageId;
	private URL expiringUrl;
    private URL expiringThumbnailUrl;
	
	private static final long serialVersionUID = -3600281937042078536L;

	public String getImageId() {
		return imageId;
	}
	public void setInageId(String imageId) {
		this.imageId = imageId;
	}
	public URL getExpiringUrl() {
		return expiringUrl;
	}
	public void setExpiringUrl(URL expiringUrl) {
		this.expiringUrl = expiringUrl;
	}
	public URL getExpiringThumbnailUrl() {
		return expiringThumbnailUrl;
	}
	public void setExpiringThumbnailUrl(URL expiringThumbnailUrl) {
		this.expiringThumbnailUrl = expiringThumbnailUrl;
	}
}
