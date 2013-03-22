package org.activityinfo.server.event.sitechange;

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

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withUrl;

import org.activityinfo.server.event.CommandEvent;
import org.activityinfo.server.event.CommandEventListener;
import org.activityinfo.server.event.ServerEventBus;
import org.activityinfo.shared.command.CreateSite;
import org.activityinfo.shared.command.UpdateSite;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.inject.Inject;

/**
 * regarded as too spammy for now, replaced by email digest
 */
public class SiteChangeListener extends CommandEventListener {

    @Inject
    @SuppressWarnings("unchecked")
    public SiteChangeListener(ServerEventBus serverEventBus) {
        super(serverEventBus, CreateSite.class, UpdateSite.class);
    }

    @Override
    public void onEvent(CommandEvent event) {
        Integer userId = event.getUserId();
        Integer siteId = event.getSiteId();

        if (siteId != null && userId != null) {
            onEvent(event, userId, siteId);
        } else {
            LOGGER.warning("event fired without site and/or user!");
        }
    }

    protected void onEvent(CommandEvent event, int userId, int siteId) {
        boolean isNew = isNew(event);

        Queue queue = QueueFactory.getQueue("commandevent");
        queue.add(withUrl(SiteChangeServlet.ENDPOINT)
            .param(SiteChangeServlet.PARAM_SITE, String.valueOf(siteId))
            .param(SiteChangeServlet.PARAM_USER, String.valueOf(userId))
            .param(SiteChangeServlet.PARAM_NEW, String.valueOf(isNew)));
    }

    protected boolean isNew(CommandEvent event) {
        return (event.getCommand() instanceof CreateSite);
    }
}
