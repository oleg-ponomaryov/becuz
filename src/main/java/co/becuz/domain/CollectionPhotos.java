package co.becuz.domain;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "collections")
public class CollectionPhotos implements Serializable {

	private static final long serialVersionUID = 5768299756910219095L;

	@Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "id", nullable = false, updatable = false)
	@Getter
	private String id;
	
    @Column(name = "collection_id", nullable = false)
	@Getter
	@Setter
    private Collection collection;
    
    @Column(name = "photo_id", nullable = false)
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
