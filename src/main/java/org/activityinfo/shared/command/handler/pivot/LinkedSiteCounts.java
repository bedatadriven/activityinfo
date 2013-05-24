package org.activityinfo.shared.command.handler.pivot;

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

import org.activityinfo.shared.db.Tables;

import com.bedatadriven.rebar.sql.client.query.SqlQuery;

/**
 * Base table for counting the number of sites that match a certain criteria. This subclass behaves just like the
 * SiteCounts query, but only selects the linked sites (via database -> activity -> indicator -> indicatorlink ->
 * indicator -> activity -> site) instead of the sites that are directly linked to the database or activity the filter
 * is set to (via database -> activity -> site).
 */
public class LinkedSiteCounts extends SiteCounts {
    @Override
    protected void addActivityJoin(SqlQuery query) {
        // select only the linked sites
        query.innerJoin(Tables.INDICATOR, "SrcInd").on(
            "SrcInd.ActivityId = Site.ActivityId");
        query.innerJoin(Tables.INDICATOR_LINK, "IndLink").on(
            "SrcInd.IndicatorId = IndLink.SourceIndicatorId");
        query.innerJoin(Tables.INDICATOR, "DestInd").on(
            "IndLink.DestinationIndicatorId = DestInd.IndicatorId");
        query.leftJoin(Tables.ACTIVITY, "Activity").on(
            "Activity.ActivityId = DestInd.ActivityId");
    }
}
