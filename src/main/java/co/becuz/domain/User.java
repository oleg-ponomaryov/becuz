package co.becuz.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.beans.factory.annotation.Autowired;

import co.becuz.domain.enums.Role;
import co.becuz.services.PhotoService;
import co.becuz.social.SocialMediaTypes;

@Entity
@Table(name = "users",
	indexes = { @Index(name = "user_email_idx", columnList = "email"), @Index(name = "user_name_provider_idx",
	columnList = "username,sign_in_provider")})
public class User implements Serializable {
	private static final long serialVersionUID = -4839837450069014606L;

	//@Autowired
	//private PhotoService photoService;
	
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

	@Column(name = "password_hash", nullable = false)
	@Getter
	@Setter
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
