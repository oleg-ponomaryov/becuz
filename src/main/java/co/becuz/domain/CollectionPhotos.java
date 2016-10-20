package co.becuz.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import co.becuz.json.JsonDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity
@Table(name = "collectionphotos", indexes = { @Index(name = "photo_collections_idx", columnList = "photo_id,collection_id"), @Index(name = "photo_collections_collection_idx", columnList = "collection_id")})
@EqualsAndHashCode(of = { "id" })
public class CollectionPhotos implements Serializable {

	private static final long serialVersionUID = 5768299756910219095L;

	@Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "id", nullable = false, updatable = false)
	@Getter
	private String id;
    
	@ManyToOne
    @JoinColumn	
	@Getter
	@Setter
	private Collection collection;

	@ManyToOne(cascade = {CascadeType.REMOVE,CascadeType.DETACH,CascadeType.MERGE,CascadeType.REFRESH})
    @JoinColumn(name = "photo_id")	
	@Getter
	@Setter
	private Photo photo;

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
