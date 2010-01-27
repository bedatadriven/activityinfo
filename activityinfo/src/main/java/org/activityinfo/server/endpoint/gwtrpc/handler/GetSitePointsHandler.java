/*
 * This file is part of ActivityInfo.
 *
 * ActivityInfo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ActivityInfo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ActivityInfo.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2009 Alex Bertram and contributors.
 */

package org.activityinfo.server.endpoint.gwtrpc.handler;

import com.google.inject.Inject;
import org.activityinfo.server.dao.SiteTableDAO;
import org.activityinfo.server.domain.SiteData;
import org.activityinfo.server.domain.User;
import org.activityinfo.server.report.generator.SiteDataBinder;
import org.activityinfo.shared.command.GetSitePoints;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.dto.Bounds;
import org.activityinfo.shared.dto.SitePoint;
import org.activityinfo.shared.dto.SitePointCollection;
import org.activityinfo.shared.exception.CommandException;
import org.hibernate.criterion.Restrictions;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 * @see org.activityinfo.shared.command.GetSitePoints
 */
public class GetSitePointsHandler implements CommandHandler<GetSitePoints> {

    private final SiteTableDAO dao;

    @Inject
    public GetSitePointsHandler(SiteTableDAO dao) {
        this.dao = dao;
    }

    @Override
    public CommandResult execute(GetSitePoints cmd, User user) throws CommandException {

        // query for the sites
        List<SiteData> sites = dao.query(user, Restrictions.eq("activity.id", cmd.getActivityId()), null,
                new SiteDataBinder(), SiteTableDAO.RETRIEVE_NONE, 0, -1);

        Bounds bounds = new Bounds();

        List<SitePoint> points = new ArrayList<SitePoint>(sites.size());
        for (SiteData site : sites) {

            if (site.hasLatLong()) {

                points.add(new SitePoint(site.getLatitude(), site.getLongitude(), site.getId()));
                bounds.grow(site.getLatitude(), site.getLongitude());

            }

        }

        return new SitePointCollection(bounds, points);

    }
}
