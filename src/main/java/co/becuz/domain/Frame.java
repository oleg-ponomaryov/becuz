package co.becuz.domain;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import co.becuz.domain.enums.DisplayType;
import co.becuz.domain.enums.FrameStatus;
import co.becuz.json.JsonDateSerializer;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "frames")
@EqualsAndHashCode(of = { "id" })
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

    @Column(name = "preview_url", nullable = true)
	@Getter
	@Setter
    private String previewImgUrl;

    
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
    
    @Column(name = "child_id")
	@Getter
	@Setter
    private String childId;

    @Column(name = "photo_width")
	@Getter
	@Setter
    private int photoWidth;

    @Column(name = "photo_height")
	@Getter
	@Setter
    private int photoHeight;
    
    @Column(name = "window_url")
	@Getter
	@Setter
    private String photoWindowUrl;
    
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
