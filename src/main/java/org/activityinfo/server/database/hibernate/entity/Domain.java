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

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Transient;

/**
 * Defines a custom domain for ActivityInfo that has its own branding, titles,
 * and scaffolding template.
 */
@Entity
public class Domain implements Serializable {
    private static final long serialVersionUID = 241542892559897521L;


    public static final String DEFAULT_HOST = "www.activityinfo.org";
    public static final String DEFAULT_TITLE = "ActivityInfo";
    public static final Domain DEFAULT = new Domain(DEFAULT_HOST, DEFAULT_TITLE);

    private String host;
    private String title;

    private String scaffolding;
    private String homePageBody;
    private boolean signUpAllowed;
    
    public Domain() {
    }

    public Domain(String host, String title) {
        super();
        this.host = host;
        this.title = title;
    }

    @Id
    @Column(name = "host", unique = true, nullable = false, length = 100)
    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    @Column(name = "title", nullable = false, length = 100)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    @Lob
	public String getScaffolding() {
		return scaffolding;
	}

	public void setScaffolding(String scaffolding) {
		this.scaffolding = scaffolding;
	}
	
	public String getHomePageBody() {
		return homePageBody;
	}

	public void setHomePageBody(String homePageBody) {
		this.homePageBody = homePageBody;
	}

	public boolean isSignUpAllowed() {
		return signUpAllowed;
	}

	public void setSignUpAllowed(boolean signUpAllowed) {
		this.signUpAllowed = signUpAllowed;
	}

	@Override
    public String toString() {
        return host;
    }
}
