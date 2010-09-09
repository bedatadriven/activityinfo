/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.domain;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * Describes a user
 *
 * @author Alex Bertram
 */
@Entity
@Table(name = "UserLogin")
// We want to avoid calling this table 'User' as it is a reserved word in some dialects
// of SQL
@NamedQueries({
        @NamedQuery(name = "findUserByEmail", query = "select u from User u where u.email = :email"),
        @NamedQuery(name = "findUserByChangePasswordKey", query = "select u from User u where u.changePasswordKey = :key")
})
public class User implements java.io.Serializable {
    private int id;
    private String email;
    private String firstName;
    private String name;
    private boolean newUser;
    private String locale;
    private String changePasswordKey;
    private Date dateChangePasswordKeyIssued;
    private String hashedPassword;
	private PrivacyLevel privacyLevel;

    public User() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "UserId", unique = true, nullable = false)
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "Email", nullable = false, length = 75, unique = true)
    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "Name", nullable = false, length = 50)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "FirstName", nullable = true, length = 50)
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Column(name = "NewUser", nullable = false)
    public boolean isNewUser() {
        return this.newUser;
    }

    public void setNewUser(boolean newUser) {
        this.newUser = newUser;
    }

    @Column(name = "Locale", nullable = false, length = 10)
    public String getLocale() {
        return this.locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    /**
     * Gets the user's password, hashed with the BCrypt algorithm.
     *
     * @return The hashed password
     */
    @Column(name = "Password", length = 150)
    public String getHashedPassword() {
        return this.hashedPassword;
    }

    /**
     * Sets the user's password, should be hashed with the BCrypt algorithm
     *
     * @param hashed The hashed password
     */
    public void setHashedPassword(String hashed) {
        this.hashedPassword = hashed;
    }

    /**
     * Gets the secure key required to change the user's password. This
     * is a random 128-bit key that can be safely sent to the user by email.
     */
    @Column(length = 34, nullable = true)
    public String getChangePasswordKey() {
        return changePasswordKey;
    }

    public void setChangePasswordKey(String changePasswordKey) {
        this.changePasswordKey = changePasswordKey;
    }

    /**
     * Gets the date on which the password key was issued; the application
     * should not let users change passwords with really old keys.
     */
    public Date getDateChangePasswordKeyIssued() {
        return dateChangePasswordKeyIssued;
    }

    public void setDateChangePasswordKeyIssued(Date dateChangePasswordKeyIssued) {
        this.dateChangePasswordKeyIssued = dateChangePasswordKeyIssued;
    }

    public void clearChangePasswordKey() {
        this.setChangePasswordKey(null);
        this.setDateChangePasswordKeyIssued(null);
    }

    /* (non-Javadoc)
      * @see java.lang.Object#equals(java.lang.Object)
      */

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof User)) {
            return false;
        }
        final User that = (User) other;
        return this.getEmail().equals(that.getEmail());
    }

    /* (non-Javadoc)
      * @see java.lang.Object#hashCode()
      */

    @Override
    public int hashCode() {
		return getEmail().hashCode();
	}

    @ManyToOne(optional = true)
	@JoinColumn(name = "privacy_level", nullable = true)
	public PrivacyLevel getPrivacyLevel() {
		return privacyLevel;
	}
	
	public void setPrivacyLevel(PrivacyLevel privacyLevel) {
		this.privacyLevel = privacyLevel;
	}

	@Override
	public String toString() {
		if (firstName != null && name != null) {
			return firstName + " " + name;
		}
		else {
			return "(null user)";
		}
	}
}
