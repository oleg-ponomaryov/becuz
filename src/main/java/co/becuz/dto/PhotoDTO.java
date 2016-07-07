package co.becuz.dto;

import java.io.Serializable;
import java.net.URL;

public class PhotoDTO implements Serializable {
	private String photoId;
	private URL expiringUrl;
    private URL expiringThumbnailUrl;
	
	private static final long serialVersionUID = -3600281937042078536L;

	public String getPhotoId() {
		return photoId;
	}
	public void setInageId(String photoId) {
		this.photoId = photoId;
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
