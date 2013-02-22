package org.activityinfo.server.command.handler.sync;

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

import org.activityinfo.server.database.hibernate.entity.ReportingPeriod;
import org.activityinfo.server.database.hibernate.entity.Site;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.shared.command.GetSyncRegionUpdates;
import org.activityinfo.shared.command.result.SyncRegionUpdate;
import org.json.JSONException;

import com.bedatadriven.rebar.sync.server.JpaUpdateBuilder;

public class SiteTableUpdateBuilder implements UpdateBuilder {

    public static final String CURRENT_VERSION = "2";
    private final JpaUpdateBuilder builder = new JpaUpdateBuilder();

    @Override
    public SyncRegionUpdate build(User user, GetSyncRegionUpdates request)
        throws JSONException {
        SyncRegionUpdate update = new SyncRegionUpdate();
        update.setComplete(true);
        update.setVersion(CURRENT_VERSION);

        if (!CURRENT_VERSION.equals(request.getLocalVersion())) {

            builder.createTableIfNotExists(Site.class);
            builder.createTableIfNotExists(ReportingPeriod.class);
            builder
                .executeStatement("create index if not exists site_activity on site (ActivityId)");

            // TODO: fix rebar to handle these types of classes correctly
            builder
                .executeStatement("create table if not exists AttributeValue (SiteId integer, AttributeId integer, Value integer)");
            builder
                .executeStatement("create table if not exists IndicatorValue (ReportingPeriodId integer, IndicatorId integer, Value real)");
            update.setSql(builder.asJson());
        }

        return update;
    }
}
