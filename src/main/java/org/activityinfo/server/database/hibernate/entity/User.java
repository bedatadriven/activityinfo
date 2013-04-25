package org.activityinfo.server.database.hibernate.entity;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.mindrot.bcrypt.BCrypt;

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
    private String name;
    private String email;
    private String organization;
    private String jobtitle;
    private String locale;
    private String changePasswordKey;
    private Date dateChangePasswordKeyIssued;
    private String hashedPassword;
    private boolean emailNotification;

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

    @Column(name = "Organization", nullable = true, length = 100)
    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    @Column(name = "Jobtitle", nullable = true, length = 100)
    public String getJobtitle() {
        return jobtitle;
    }

    public void setJobtitle(String jobtitle) {
        this.jobtitle = jobtitle;
    }

    @Column(name = "EmailNotification", nullable = false)
    public boolean isEmailNotification() {
        return this.emailNotification;
    }

    public void setEmailNotification(boolean emailNotification) {
        this.emailNotification = emailNotification;
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

    public void changePassword(String newPlaintextPassword) {
        this.hashedPassword = BCrypt.hashpw(newPlaintextPassword,
            BCrypt.gensalt());
    }

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

    @Override
    public int hashCode() {
        return getEmail().hashCode();
    }

    @Override
    public String toString() {
        if (name != null) {
            return name;
        } else {
            return "user" + id;
        }
    }

}
