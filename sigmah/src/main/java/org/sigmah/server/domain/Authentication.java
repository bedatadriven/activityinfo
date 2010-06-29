/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.domain;

// Generated Apr 9, 2009 7:58:20 AM by Hibernate Tools 3.2.2.GA

import javax.persistence.*;
import java.util.Date;

/**
 * @author Alex Bertram
 */
@Entity
public class Authentication implements java.io.Serializable {

    private String id;
    private User user;
    private Date dateCreated;
    private Date dateLastActive;

    public Authentication() {

    }

    /**
     * Creates a new session object for the given user, with
     * a secure session id and starting at the current time
     *
     * @param user
     */
    public Authentication(User user) {
        //	setId(SecureTokenGenerator.generate());
        setUser(user);
        setDateCreated(new Date());
        setDateLastActive(new Date());
    }

    /**
     * Gets the secure id of this Authentication, which is a 128-bit random number
     * represented as a 32-character hexadecimal string.
     *
     * @return the id of this authentication
     */
    @Id
    @Column(name = "AuthToken", unique = true, nullable = false, length = 32)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SecureSequenceGenerator")
    @org.hibernate.annotations.GenericGenerator(name = "SecureSequenceGenerator",
            strategy = "org.sigmah.server.auth.SecureSequenceGenerator")
    public String getId() {
        return this.id;
    }

    public void setId(String sessionId) {
        this.id = sessionId;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "UserId", nullable = false)
    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column
    public Date getDateCreated() {
        return this.dateCreated;
    }

    private void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column
    public Date getDateLastActive() {
        return this.dateLastActive;
    }

    public void setDateLastActive(Date dateLastActive) {
        this.dateLastActive = dateLastActive;
    }

    public long minutesSinceLastActivity() {
        return ((new Date()).getTime() - getDateLastActive().getTime()) / 1000 / 60;
    }

    @Transient
    public boolean isExpired() {
        // TODO: when do we invalidate tokens?
        //	return minutesSinceLastActivity() > 30;
        return false;
    }

    public void setDateLastActive() {
        setDateLastActive(new Date());
	}
}
