package co.becuz.filters;

public class FacebookUserDetails {

	private String username;
	private String email;
	private String firstName;
	private String lastName;
	private String imageUrl;
	
	public FacebookUserDetails (String username, String email, String firstName, String lastName, String imageUrl) {
		this.username=username;
		this.email=email;
		this.firstName=firstName;
		this.lastName=lastName;
		this.imageUrl=imageUrl;
	}
	public String getUsername() {
		return username;
	}

	public String getEmail() {
		return email;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getImageUrl() {
		return imageUrl;
	}
}