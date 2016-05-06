package co.becuz.dto.response;

import java.io.Serializable;

public class StaticImage implements Serializable {
	private static final long serialVersionUID = -6537252702381939944L;

	private String url;
    private boolean initial;
	private String md5;
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public boolean isInitial() {
		return initial;
	}
	public void setInitial(boolean initial) {
		this.initial = initial;
	}
	public String getMd5() {
		return md5;
	}
	public void setMd5(String md5) {
		this.md5 = md5;
	}
}
