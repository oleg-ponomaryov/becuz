package co.becuz.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import co.becuz.domain.enums.Role;
import co.becuz.social.SocialMediaTypes;

@Entity
@Table(name = "users",
	indexes = { @Index(name = "user_email_idx", columnList = "email"), @Index(name = "user_name_provider_idx",
	columnList = "username,sign_in_provider")})
public class User implements Serializable {
	private static final long serialVersionUID = -4839837450069014606L;

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@Getter
	@Column(name = "id", nullable = false, updatable = false)
	private String id;

	@Column(name = "email", nullable = false, unique = true)
	@Getter
	@Setter
	private String email;

	@Transient
	//@JsonIgnore
	private String password;
	
	@Column(name = "password_hash", nullable = false)
	@Getter
	@Setter
	@JsonIgnore
	private String passwordHash;

	@Column(name = "role", nullable = false)
	@Enumerated(EnumType.STRING)
	@Getter
	@Setter
	private Role role;

	@Enumerated(EnumType.STRING)
	@Column(name = "sign_in_provider", length = 20)
	@Getter
	@Setter
	private SocialMediaTypes signinprovider;

	@Column(name = "username")
	@Getter
	@Setter
	private String username;

	@Column(name = "firstname")
	@Getter
	@Setter
	private String firstname;

	@Column(name = "lastname")
	@Getter
	@Setter
	private String lastname;
	
	@Column(name = "dob")
	@Getter
	@Setter
	private Date dob;
	
	@Column(name = "photourl", length = 500)
	@Getter
	@Setter
	private String photoUrl;

	@Column(name = "created")
	@Getter
	@Setter
	private Date created;

	@Column(name = "updated")
	@Getter
	@Setter
	private Date updated;

	@JsonIgnore
	public String getPassword() {
		return password;
	}

	@JsonProperty
	public void setPassword(String pass) {
		this.password=pass;
	}
	
	@OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
	private Set<Photo> photos = new HashSet<Photo>();

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private Set<Collection> colections = new HashSet<Collection>();
	
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
