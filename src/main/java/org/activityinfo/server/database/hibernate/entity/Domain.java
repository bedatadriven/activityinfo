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
import java.net.MalformedURLException;
import java.net.URL;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
public class Domain implements Serializable {
    private static final long serialVersionUID = 241542892559897521L;

    private static String GCS_BASEPATH = "//commondatastorage.googleapis.com/";
    private static String GCS_PAGE_PROTOCOL = "http:";
    private static String GCS_PAGE_NAME = "/index.html";

    public static final String DEFAULT_HOST = "www.activityinfo.org";
    public static final String DEFAULT_TITLE = "ActivityInfo";

    private String host;
    private String title;
    private String bucket;

    public Domain() {
    }

    public Domain(String host, String title, String bucket) {
        super();
        this.host = host;
        this.title = title;
        this.bucket = bucket;
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

    @Column(name = "bucket", nullable = false, length = 100)
    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    @Transient
    public String getResourceBasePath() {
        return GCS_BASEPATH + bucket;
    }

    @Transient
    public URL getGCSPageURL() {
        URL result = null;
        try {
            result = new URL(GCS_PAGE_PROTOCOL + getResourceBasePath() + GCS_PAGE_NAME);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public String toString() {
        return host + ": '" + title + "' [" + bucket + "]";
    }
}
