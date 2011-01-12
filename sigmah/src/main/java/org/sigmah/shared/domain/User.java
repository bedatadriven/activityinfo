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
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.sigmah.shared.domain.profile.OrgUnitProfile;

/**
 * Describes a user
 * 
 * @author Alex Bertram
 */
@Entity
@Table(name = "UserLogin")
// We want to avoid calling this table 'User' as it is a reserved word in some
// dialects
// of SQL
@NamedQueries({
        @NamedQuery(name = "findUserByEmail", query = "select u from User u where u.email = :email"),
        @NamedQuery(name = "findUserByChangePasswordKey", query = "select u from User u where u.changePasswordKey = :key") })
public class User implements java.io.Serializable {

    private static final long serialVersionUID = 6486007767204653799L;

    private int id;
    private String email;
    private String firstName;
    private String name;
    private boolean newUser;
    private String locale;
    private String changePasswordKey;
    private Date dateChangePasswordKeyIssued;
    private String hashedPassword;
    private Organization organization;
    private OrgUnitProfile orgUnitWithProfiles;

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
     * @param hashed
     *            The hashed password
     */
    public void setHashedPassword(String hashed) {
        this.hashedPassword = hashed;
    }

    /**
     * Gets the secure key required to change the user's password. This is a
     * random 128-bit key that can be safely sent to the user by email.
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

    /*
     * (non-Javadoc)
     * 
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

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */

    @Override
    public int hashCode() {
        return getEmail().hashCode();
    }

    @ManyToOne(optional = true)
    @JoinColumn(name = "id_organization", nullable = true)
    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    @OneToOne(mappedBy = "user", optional = true)
    public OrgUnitProfile getOrgUnitWithProfiles() {
        return orgUnitWithProfiles;
    }

    public void setOrgUnitWithProfiles(OrgUnitProfile orgUnitWithProfiles) {
        this.orgUnitWithProfiles = orgUnitWithProfiles;
    }

    @Override
    public String toString() {
        if (firstName != null && name != null) {
            return firstName + " " + name;
        } else {
            return "(null user)";
        }
    }

    /**
     * Gets the formatted complete name of a user.
     * <ul>
     * <li>If the user has a first name and a last name, returns '<i>John
     * Doe</i>'.</li>
     * <li>If the user hasn't a first name and has a last name, returns
     * '<i>Doe</i>'.</li>
     * <li>If the user has neither a first name or a last name, returns an empty
     * string.</li>
     * </ul>
     * 
     * @param user
     *            The user
     * @return The complete name.
     */
    public static String getUserCompleteName(final User user) {

        final StringBuilder sb = new StringBuilder();
        if (user.firstName != null) {
            sb.append(user.firstName);
            sb.append(' ');
        }
        if (user.name != null) {
            sb.append(user.name);
        }

        return sb.toString();
    }

    /**
     * Gets the formatted complete name of a user.
     * <ul>
     * <li>If the user has a first name and a last name, returns '<i>John
     * Doe</i>'.</li>
     * <li>If the user hasn't a first name and has a last name, returns
     * '<i>Doe</i>'.</li>
     * <li>If the user has neither a first name or a last name, returns an empty
     * string.</li>
     * </ul>
     * 
     * @param firstName
     *            The user first name.
     * @param lastName
     *            The user last name.
     * @return The complete name.
     */
    public static String getUserCompleteName(final String firstName, final String lastName) {

        final StringBuilder sb = new StringBuilder();
        if (firstName != null) {
            sb.append(firstName);
            sb.append(' ');
        }
        if (lastName != null) {
            sb.append(lastName);
        }

        return sb.toString();
    }

    /**
     * Gets the formatted short name of a user.
     * <ul>
     * <li>If the user has a first name and a last name, returns '<i>J.
     * Doe</i>'.</li>
     * <li>If the user hasn't a first name and has a last name, returns
     * '<i>Doe</i>'.</li>
     * <li>If the user has neither a first name or a last name, returns an empty
     * string.</li>
     * </ul>
     * 
     * @param user
     *            The user
     * @return The short name.
     */
    public static String getUserShortName(final User user) {

        final StringBuilder sb = new StringBuilder();
        if (user.firstName != null) {
            sb.append(user.firstName.charAt(0));
            sb.append(". ");
        }
        if (user.name != null) {
            sb.append(user.name);
        }

        return sb.toString();
    }

    /**
     * Gets the formatted short name of a user.
     * <ul>
     * <li>If the user has a first name and a last name, returns '<i>J.
     * Doe</i>'.</li>
     * <li>If the user hasn't a first name and has a last name, returns
     * '<i>Doe</i>'.</li>
     * <li>If the user has neither a first name or a last name, returns an empty
     * string.</li>
     * </ul>
     * 
     * @param firstName
     *            The user first name.
     * @param lastName
     *            The user last name.
     * @return The short name.
     */
    public static String getUserShortName(final String firstName, final String lastName) {

        final StringBuilder sb = new StringBuilder();
        if (firstName != null) {
            sb.append(firstName.charAt(0));
            sb.append(". ");
        }
        if (lastName != null) {
            sb.append(lastName);
        }

        return sb.toString();
    }
}
