/*
 * This file is part of ActivityInfo.
 *
 * ActivityInfo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ActivityInfo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ActivityInfo.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2009 Alex Bertram and contributors.
 */
package org.activityinfo.server.domain;


import org.activityinfo.server.auth.SecureTokenGenerator;
import org.activityinfo.server.auth.impl.BCrypt;

import javax.persistence.*;
import java.util.Date;
import java.util.Locale;

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
    private String name;
    private boolean newUser;
    private String locale;
    private String changePasswordKey;
    private Date dateChangePasswordKeyIssued;
    private String hashedPassword;

    public User() {
    }

    /**
     * Initializes this User as a new User with a secure
     * changePasswordKey
     */
    public static User createNewUser(String email, String name, String locale) {
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setNewUser(true);
        user.setLocale(locale);
        user.setChangePasswordKey(SecureTokenGenerator.generate());

        return user;
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

    @Transient
    public Locale getLocaleObject() {
        if(locale != null) {
            Locale[] locales = Locale.getAvailableLocales();
            for (Locale l : locales) {
                if (locale.startsWith(l.getLanguage())) {
                    return l;
                }
            }
        }
        return Locale.getDefault();
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
     * Sets a new password for the user, hashing the password
     * for security
     *
     * @param password the new password in plain text
     */

    public void changePassword(String password) {
        this.hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
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
        if (this == other) return true;
        if (!(other instanceof User)) return false;
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


}
