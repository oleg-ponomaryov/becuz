package co.becuz.domain;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import co.becuz.domain.enums.DisplayType;
import co.becuz.domain.enums.FrameStatus;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "frames")
public class Frame implements Serializable {

	private static final long serialVersionUID = -4877835913432049038L;

	@Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "id", nullable = false, updatable = false)
	@Getter
	private String id;

    @Column(name = "url", nullable = true)
	@Getter
	@Setter
    private String url;
    
    @Column(name = "display_type", nullable = true)
	@Getter
	@Setter
    private DisplayType displayType;

    @Column(name = "priority", nullable = false)
	@Getter
	@Setter
    private int priority=0;
    
    @Column(name = "isdefault")
	@Getter
	@Setter
    private boolean isdefault = true;

    @Column(name = "status", nullable = false)
	@Getter
	@Setter
    private FrameStatus status = FrameStatus.OPEN;

    @Column(name = "description", nullable = true)
	@Getter
	@Setter
    private String description;

    @Column(name = "created", nullable = false, updatable = false)
	@Getter
	@Setter
    private Date created;

	@Column(name = "updated")
	@Getter
	@Setter
	private Date updated;

	@OneToMany(mappedBy = "frame", cascade = CascadeType.ALL)
	private Set<Collection> colections = new HashSet<Collection>();
	
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
