package co.becuz.domain;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "collectionphotos", indexes = { @Index(name = "photo_collections_idx", columnList = "photo_id,collection_id"), @Index(name = "photo_collections_collection_idx", columnList = "collection_id")})

public class CollectionPhotos implements Serializable {

	private static final long serialVersionUID = 5768299756910219095L;

	@Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "id", nullable = false, updatable = false)
	@Getter
	private String id;
    
	@ManyToOne
    @JoinColumn(name = "collection_id")	
	@Getter
	@Setter
	private Collection collection;

	@ManyToOne
    @JoinColumn(name = "photo_id")	
	@Getter
	@Setter
	private Photo photo;

    @Column(name = "created", nullable = false, updatable = false)
	@Getter
	@Setter
    private Date created;

	@Column(name = "updated")
	@Getter
	@Setter
	private Date updated;
		
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
