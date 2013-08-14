package org.activityinfo.server.event.sitehistory;

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

import org.activityinfo.server.event.CommandEvent;
import org.activityinfo.server.event.ServerEventBus;
import org.activityinfo.server.event.sitechange.SiteChangeListener;

import com.google.inject.Inject;

public class SiteHistoryListener extends SiteChangeListener {
    private final SiteHistoryProcessor siteHistoryProcessor;

    @Inject
    public SiteHistoryListener(ServerEventBus serverEventBus, SiteHistoryProcessor siteHistoryProcessor) {
        super(serverEventBus);
        this.siteHistoryProcessor = siteHistoryProcessor;
    }

    @Override
    protected void onEvent(CommandEvent event, final int userId, final int siteId) {
        siteHistoryProcessor.process(event.getCommand(), userId, siteId);
    }
}
