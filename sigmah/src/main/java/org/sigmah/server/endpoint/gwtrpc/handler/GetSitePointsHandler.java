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

package org.sigmah.server.endpoint.gwtrpc.handler;

import com.google.inject.Inject;
import org.hibernate.criterion.Restrictions;
import org.sigmah.server.dao.SiteTableDAO;
import org.sigmah.server.domain.SiteData;
import org.sigmah.server.domain.User;
import org.sigmah.server.report.generator.SiteDataBinder;
import org.sigmah.shared.command.GetSitePoints;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.command.result.SitePointList;
import org.sigmah.shared.dto.BoundingBoxDTO;
import org.sigmah.shared.dto.SitePointDTO;
import org.sigmah.shared.exception.CommandException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 * @see org.sigmah.shared.command.GetSitePoints
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

        BoundingBoxDTO bounds = new BoundingBoxDTO();

        List<SitePointDTO> points = new ArrayList<SitePointDTO>(sites.size());
        for (SiteData site : sites) {

            if (site.hasLatLong()) {

                points.add(new SitePointDTO(site.getId(), site.getLongitude(), site.getLatitude()));
                bounds.grow(site.getLatitude(), site.getLongitude());

            }

        }

        return new SitePointList(bounds, points);

    }
}
