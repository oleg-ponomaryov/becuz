package co.becuz.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import co.becuz.social.SocialMediaTypes;

@Entity
@Table(name = "users")
public class User implements Serializable {
	private static final long serialVersionUID = -4839837450069014606L;

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@Column(name = "id", nullable = false, updatable = false)
	private String id;

	@Column(name = "email", nullable = false, unique = true)
	private String email;

	@Column(name = "password_hash", nullable = false)
	private String passwordHash;

	@Column(name = "role", nullable = false)
	@Enumerated(EnumType.STRING)
	private Role role;

	@Enumerated(EnumType.STRING)
	@Column(name = "sign_in_provider", length = 20)
	private SocialMediaTypes signinprovider;

	@Column(name = "username")
	private String username;

	@Column(name = "imageurl", length = 500)
	private String imageUrl;

	@Column(name = "created")
	private Date created;

	@Column(name = "updated")
	private Date updated;

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String url) {
		this.imageUrl = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String name) {
		this.username = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public SocialMediaTypes getSigninprovider() {
		return signinprovider;
	}

	public void setSigninprovider(SocialMediaTypes type) {
		this.signinprovider = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	@PrePersist
	public void onSave() {
		if (this.created == null) {
			this.created = new Date();
		}
	}

	@PreUpdate
	public void onUpdate() {
		this.updated = new Date();
	}

	@Override
	public String toString() {
		return "User{" + "id=" + getId() + ", email='"
				+ email.replaceFirst("@.*", "@***") + ", passwordHash='"
				+ passwordHash.substring(0, 10) + ", role=" + role + '}';
	}
}
