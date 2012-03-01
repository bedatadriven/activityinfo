/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.database.hibernate.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 
 * @author Alex Bertram
 *
 */
@Entity
public class Location implements java.io.Serializable {

	private int id;
    private LocationType locationType;
	private String locationGuid;
	private Double x;
	private Double y;
	private String name;
	private String axe;
	private Set<Site> sites = new HashSet<Site>(0);
	private Set<AdminEntity> adminEntities = new HashSet<AdminEntity>(0);
    private Date dateCreated;
    private Date dateEdited;
    private long timeEdited;

	public Location() {
	}

    @Id
	@Column(name = "LocationID", unique = true, nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LocationTypeID", nullable = false)
	public LocationType getLocationType() {
		return this.locationType;
	}

	public void setLocationType(LocationType locationType) {
		this.locationType = locationType;
	}

	@Column(name = "LocationGuid", length = 36)
	public String getLocationGuid() {
		return this.locationGuid;
	}

	public void setLocationGuid(String locationGuid) {
		this.locationGuid = locationGuid;
	}

	@Column(name = "X", precision = 7, scale = 0)
	public Double getX() {
		return this.x;
	}

	public void setX(Double x) {
		this.x = x;
	}

	@Column(name = "Y", precision = 7, scale = 0)
	public Double getY() {
		return this.y;
	}

	public void setY(Double y) {
		this.y = y;
	}

	@Column(name = "Name", nullable = false, length = 50)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "Axe", length = 50)
	public String getAxe() {
		return this.axe;
	}

	public void setAxe(String axe) {
		this.axe = axe;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "location")
	public Set<Site> getSites() {
		return this.sites;
	}

	public void setSites(Set<Site> sites) {
		this.sites = sites;
	}

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "LocationAdminLink", 
			joinColumns = { 
				@JoinColumn(name = "LocationId", nullable = false, updatable = false) }, 
			inverseJoinColumns = { 
				@JoinColumn(name = "AdminEntityId", nullable = false, updatable = false) })
	public Set<AdminEntity> getAdminEntities() {
		return this.adminEntities;
	}

	public void setAdminEntities(Set<AdminEntity> adminEntities) {
		this.adminEntities = adminEntities;
	}

	public void setAdminEntity(int levelId, AdminEntity newEntity) {
		
		for(AdminEntity entity : getAdminEntities()) {
			if(entity.getLevel().getId() == levelId) {
				
				if(newEntity == null) {
					getAdminEntities().remove(entity);
				} else if(newEntity.getId() != entity.getId()) {
					getAdminEntities().remove(entity);
					getAdminEntities().add(newEntity);
				}
				
				return;
			}
		}
	
		if(newEntity!=null) {
			getAdminEntities().add(newEntity);
		}
	}

    @Temporal(TemporalType.TIMESTAMP)
    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getDateEdited() {
        return dateEdited;
    }

    public void setDateEdited(Date dateEdited) {
        this.dateEdited = dateEdited;
        this.timeEdited = dateEdited.getTime();
    }
    
    public long getTimeEdited() {
		return timeEdited;
	}

	public void setTimeEdited(long timeEdited) {
		this.timeEdited = timeEdited;
	}

	@PrePersist
    public void onCreate() {
        Date now = new Date();
        setDateCreated(now);
        setDateEdited(now);
        setTimeEdited(now.getTime());
    }

    @PreUpdate
    public void onUpdate() {
        Date now = new Date();
		setDateEdited(now);
        setTimeEdited(now.getTime());
    }
}
