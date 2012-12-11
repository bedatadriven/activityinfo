/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.database.hibernate.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

@Entity
public class SiteHistory implements java.io.Serializable {
	private static final long serialVersionUID = 4677949631016278657L;
	
	private int id;
	private Site site;
	private User user;
	private String json;
	private long timeCreated;
	private boolean isNew;

    public SiteHistory() {
	}

	@Id
	@Column(name = "id", unique = true, nullable = false)
	@GeneratedValue(strategy=GenerationType.AUTO)
	public int getId() {
		return this.id;
	}

	public void setId(int siteId) {
		this.id = siteId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "siteId", nullable = false)
	public Site getSite() {
		return this.site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId", nullable = false)
    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

	@Lob
	public String getJson() {
		return this.json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	@Basic
	public long getTimeCreated() {
		return timeCreated;
	}
	
	public void setTimeCreated(long timeCreated) {
		this.timeCreated = timeCreated;
	}
	
    @Column(name = "isnew", nullable = false)
    public boolean isNew() {
        return this.isNew;
    }

    public void setNew(boolean isNew) {
        this.isNew = isNew;
    }

}
