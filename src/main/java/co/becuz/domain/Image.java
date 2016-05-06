package co.becuz.domain;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "image")
public class Image implements Serializable {
	private static final long serialVersionUID = -2762093398070254170L;

	@Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "id", nullable = false, updatable = false)
    private String id;
	
	@OneToOne(cascade=CascadeType.ALL, fetch = FetchType.EAGER)
    private User owner;
    
    @Column(name = "bucket", nullable = false)
    private String bucket;
    
    @Column(name = "original_key", nullable = false)
    private String originalKey;

    @Column(name = "md5_digest", nullable = false, updatable = false, unique = true)
    private String md5Digest;
    
    @Column(name = "thumbnail_key", nullable = true)
    private String thumbnailKey;
    
    @Column(name = "title", nullable = true)
    private String title;
    
    @Column(name = "uploaded_date", nullable = false)
    private Date uploadedDate;
    
    @Column(name = "description", nullable = true)
    private String description;

    @Column(name = "created", nullable = false, updatable = false)
    private Date created;

	@Column(name = "updated")
    private Date updated;
    
    // Getters and setters

    public String getMd5Digest() {
		return md5Digest;
	}

	public void setMd5Digest(String digest) {
		this.md5Digest = digest;
	}
	
	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public String getBucket() {
		return bucket;
	}

	public void setBucket(String bucket) {
		this.bucket = bucket;
	}

	public String getOriginalKey() {
		return originalKey;
	}

	public void setOriginalKey(String originalKey) {
		this.originalKey = originalKey;
	}

	public String getThumbnailKey() {
		return thumbnailKey;
	}

	public void setThumbnailKey(String thumbnailKey) {
		this.thumbnailKey = thumbnailKey;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getUploadedDate() {
		return uploadedDate;
	}

	public void setUploadedDate(Date uploadedDate) {
		this.uploadedDate = uploadedDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
			if (this.created==null) {
				this.created = new Date();
			}
		}

		@PreUpdate
		public void onUpdate() {
			this.updated = new Date();
		}
}
