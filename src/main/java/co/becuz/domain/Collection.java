package co.becuz.domain;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import co.becuz.domain.enums.CollectionStatus;
import co.becuz.domain.enums.FrameOwner;
import co.becuz.json.JsonDateSerializer;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "collections", indexes = { @Index(name = "user_collections_idx", columnList = "user_id"), @Index(name = "frame_collections_idx", columnList = "frame_id")})
@EqualsAndHashCode(of = { "id" })
public class Collection implements Serializable {

	private static final long serialVersionUID = -3566278471874434071L;

	@Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "id", nullable = false, updatable = false)
	@Getter
	private String id;

    @Column(name = "headline", nullable = false)
	@Getter
	@Setter
    private String headline;

	@ManyToOne
    @JoinColumn(name = "user_id")	
	@Getter
	@Setter
	private User user;
    
	@ManyToOne
    @JoinColumn(name = "frame_id")	
	@Getter
	@Setter
	private Frame frame;
    
    @Column(name = "owner_type", nullable = false)
	@Getter
	@Setter
    private FrameOwner ownerType = FrameOwner.OWNER;

    @Column(name = "status", nullable = false)
	@Getter
	@Setter
    private CollectionStatus status = CollectionStatus.DRAFT;
    
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

	@OneToMany(mappedBy = "collection", cascade = CascadeType.ALL)
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
}
