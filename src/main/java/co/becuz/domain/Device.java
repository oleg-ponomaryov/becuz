package co.becuz.domain;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import co.becuz.domain.enums.DeviceType;
import co.becuz.json.JsonDateSerializer;

import java.io.Serializable;
import java.util.Date;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "devices",indexes = { @Index(name = "user_token_idx",
columnList = "user_id,token")})
@EqualsAndHashCode(of = { "id" })
public class Device implements Serializable {

	private static final long serialVersionUID = -8567109877530491869L;

	@Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "id", nullable = false, updatable = false)
	@Getter
	private String id;
     
	@ManyToOne
    @JoinColumn(name = "user_id")	
	@Getter
	@Setter
	private User user;
	
    @Column(name = "device_type", nullable = false)
	@Getter
	@Setter
    private DeviceType deviceType;

    @Column(name = "token", nullable = false)
	@Getter
	@Setter
    private String token;
    
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
