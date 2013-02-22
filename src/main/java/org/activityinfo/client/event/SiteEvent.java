package org.activityinfo.client.event;

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

import org.activityinfo.shared.dto.SiteDTO;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.EventType;

/**
 * Encapsulates details for
 * {@link org.activityinfo.server.database.hibernate.entity.Site} related
 * events.
 * 
 * @author Alex Bertram
 */
public class SiteEvent extends BaseEvent {
    private int siteId;
    private SiteDTO site;

    /**
     * 
     * @param type
     * @param source
     *            the component which fired the event
     * @param site
     */
    public SiteEvent(EventType type, Object source, SiteDTO site) {
        super(type);
        this.setSource(source);
        this.site = site;
        this.siteId = site.getId();
    }

    public SiteEvent(EventType type, Object source, int siteId) {
        super(type);
        this.setSource(source);
        this.siteId = siteId;
    }

    public SiteDTO getSite() {
        return site;
    }

    public int getSiteId() {
        return siteId;
    }
}
