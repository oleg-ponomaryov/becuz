package co.becuz.domain;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import co.becuz.domain.enums.FrameOwner;
import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "collections")
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
	
    @Column(name = "user_id", nullable = false)
	@Getter
	@Setter
    private User user;
    
    @Column(name = "frame_id", nullable = false)
	@Getter
	@Setter
    private Frame frame;

    @Column(name = "owner_type", nullable = false)
	@Getter
	@Setter
    private FrameOwner ownerType = FrameOwner.OWNER;

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
