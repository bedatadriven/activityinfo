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

// Generated Apr 9, 2009 7:58:20 AM by Hibernate Tools 3.2.2.GA

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * 
 * @author Alex Bertram
 *
 */
@Entity
public class UserPermission implements java.io.Serializable, SchemaElement {

	private int id;
	private Partner partner;
	private UserDatabase database;
	private User user;
	private boolean allowView;
	private boolean allowViewAll;
	private boolean allowEdit;
	private boolean allowEditAll;
	private boolean allowDesign;

    public UserPermission() {
	}
	
	public UserPermission(UserDatabase database, User user) {
		this.database = database;
		this.user= user;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "UserPermissionId", unique = true, nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PartnerId", nullable = false)
	public Partner getPartner() {
		return this.partner;
	}

	public void setPartner(Partner partner) {
		this.partner = partner;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DatabaseId", nullable = false, updatable = false)
	public UserDatabase getDatabase() {
		return this.database;
	}

	public void setDatabase(UserDatabase database) {
		this.database = database;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "UserId", nullable = false, updatable = false)
	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Column(name = "AllowView", nullable = false)
	public boolean isAllowView() {
		return this.allowView;
	}

	public void setAllowView(boolean allowView) {
		this.allowView = allowView;
	}

	@Column(name = "AllowViewAll", nullable = false)
	public boolean isAllowViewAll() {
		return this.allowViewAll;
	}

	public void setAllowViewAll(boolean allowViewAll) {
		this.allowViewAll = allowViewAll;
	}

	@Column(name = "AllowEdit", nullable = false)
	public boolean isAllowEdit() {
		return this.allowEdit;
	}

	public void setAllowEdit(boolean allowEdit) {
		this.allowEdit = allowEdit;
	}

	@Column(name = "AllowEditAll", nullable = false)
	public boolean isAllowEditAll() {
		return this.allowEditAll;
	}

	public void setAllowEditAll(boolean allowEditAll) {
		this.allowEditAll = allowEditAll;
	}

	@Column(name = "AllowDesign", nullable = false)
	public boolean isAllowDesign() {
		return this.allowDesign;
	}

	public void setAllowDesign(boolean allowDesign) {
		this.allowDesign = allowDesign;
	}
}
