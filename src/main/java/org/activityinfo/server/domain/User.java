/*
 * 
 *  This file is part of ActivityInfo.
 *
 *  ActivityInfo is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  ActivityInfo is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with ActivityInfo.  If not, see <http://www.gnu.org/licenses/>.
 *  
 *  Copyright 2009 Alex Bertram
 *  
 */
package org.activityinfo.server.domain;


import java.util.HashSet;
import java.util.Set;
import java.util.Locale;
import javax.persistence.*;

import org.activityinfo.server.domain.util.BCrypt;

/**
 * 
 * @author Alex Bertram
 *
 */
@Entity
@Table(name="UserLogin")	// NOTE: See page 175 in  Bauer/King for info on quoting table names
public class User implements java.io.Serializable {


	private int id;
	private String email;
	private String name;
	private boolean newUser;
	private String locale;
	private String hashedPassword;
	private Set<UserPermission> userPermissions = new HashSet<UserPermission>(0);

	public User() {
	}

    
	public User(String email, String name, String locale) {
		this.email = email;
		this.name = name;
		this.newUser = true;
		this.locale = locale;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "UserId", unique = true, nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "Email", nullable = false, length = 75, unique=true)
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
        Locale[] locales = Locale.getAvailableLocales();
        for(Locale l : locales) {
            if(locale.startsWith(l.getLanguage())) {
                return l;
            }
        }
        return Locale.getDefault();
    }

	/**
	 * @return The hashed password
	 */
	@SuppressWarnings("unused")
	@Column(name = "Password", length = 150)
	private String getHashedPassword() {
		return this.hashedPassword;
	}
	
	@SuppressWarnings("unused")
	private void setHashedPassword(String hashed) {
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
	 * 
	 * @param password
	 * @return true if the plain-text password supplies matches the hashed password in the database
	 */
	public boolean checkPassword(String password) {
		
		// TODO: clean up the mess and remove this hack
		
		if(this.hashedPassword == null)
			return true;
		
		if(this.hashedPassword == null && (password == null || password.length()==0) )
			return true;
		if(this.hashedPassword == null)
			return false;
		
		
		
		return BCrypt.checkpw(password, this.hashedPassword);
	}
	

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object other) {
		if(this == other) return true;
		if(!(other instanceof User)) return false;
		final User that = (User)other;
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
