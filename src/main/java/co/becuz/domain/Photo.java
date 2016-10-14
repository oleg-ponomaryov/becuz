package co.becuz.domain;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import co.becuz.configuration.SecurityConfiguration;
import co.becuz.json.JsonDateSerializer;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "photos", indexes = { @Index(name = "owner_idx", columnList = "owner_id"), @Index(name = "md5_digest_idx", columnList = "md5_digest")})
@EqualsAndHashCode(of = { "id" })
public class Photo implements Serializable {
	private static final long serialVersionUID = -2762093398070254170L;
	private static final Logger LOG = LoggerFactory.getLogger(Photo.class);
	public static final long EXP_TIME_MILLIS = 31556952000L;
	
	@Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "id", nullable = false, updatable = false)
	@Getter
	private String id;

	@ManyToOne
    @JoinColumn(name = "owner_id")	
	@Getter
	@Setter
	private User owner;
	
    @Column(name = "bucket", nullable = false)
	@Getter
	@Setter
    private String bucket;
    
    @Column(name = "original_key", nullable = false)
	@Getter
	@Setter
    private String originalKey;

    @Column(name = "md5_digest", nullable = false, updatable = false, unique = true)
	@Getter
	@Setter
    private String md5Digest;
    
    @Column(name = "thumbnail_key", nullable = true)
	@Getter
	@Setter
    private String thumbnailKey;
    
    @Column(name = "caption", nullable = true)
	@Getter
	@Setter
    private String caption;
    
    @Column(name = "uploaded_date", nullable = false)
	@Getter
	@Setter
	@JsonSerialize(using=JsonDateSerializer.class)
    private Date uploadedDate;
    
    @Column(name = "description", nullable = true)
	@Getter
	@Setter
    private String description;

    @Column(name = "created", nullable = false, updatable = false)
	@Getter
	@Setter
	@JsonSerialize(using=JsonDateSerializer.class)
	private Date created;

	@Column(name = "updated")
	@Getter
	@Setter
	@JsonSerialize(using=JsonDateSerializer.class)
	private Date updated;
	
	@Transient
	@Getter
	@Setter
	private String photoUrl;
	
	@OneToMany(mappedBy = "photo", cascade = CascadeType.ALL)
	private Set<CollectionPhotos> colectionPhotos = new HashSet<CollectionPhotos>();
	
	@PrePersist
	public void onSave() {
		if (this.created==null) {
			this.created = new Date();
		}
	}

	@PreUpdate
	public void onUpdate() {
		this.updated = new Date();
	}
	
	@PreRemove
	public void onRemove() {
    	DeleteObjectRequest del_req = new DeleteObjectRequest(this.getBucket(), this.getOriginalKey());
    	try {
    		SecurityConfiguration.getS3Client().deleteObject(del_req);
    	}
    	catch (AmazonServiceException ase) {
			LOG.error("AmazonServiceException", ase);
    	} catch (AmazonClientException ace) {
			LOG.error("AmazonClientException", ace);
    	}
	}
}
