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

package org.activityinfo.server.command.handler;

import com.google.inject.Inject;
import org.activityinfo.server.dao.hibernate.SiteTableDAO;
import org.activityinfo.server.domain.User;
import org.activityinfo.server.report.generator.SiteDataBinder;
import org.activityinfo.shared.command.GetMarkers;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.dto.SiteMarker;
import org.activityinfo.shared.dto.SiteMarkerCollection;
import org.activityinfo.shared.exception.CommandException;
import org.activityinfo.shared.report.content.SiteData;

import java.util.ArrayList;
import java.util.List;

/**
 * @see org.activityinfo.shared.command.GetMarkers
 *
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class GetMarkersHandler implements CommandHandler<GetMarkers> {

    private final SiteTableDAO siteDAO;

    @Inject
    public GetMarkersHandler(SiteTableDAO siteDAO) {
        this.siteDAO = siteDAO;
    }

    @Override
    public CommandResult execute(GetMarkers cmd, User user) throws CommandException {

        // query for the sites
        List<SiteData> sites = siteDAO.query(user, null, null, new SiteDataBinder(), SiteTableDAO.RETRIEVE_NONE, 0, -1);

        // define our map
        //TiledMap map = new TiledMap(500, 500, new LatLng(0,0), cmd.getZoomLevel());

       // List<Marker<SiteData>> markers = MarkerUtil.cluster(map, sites, 10);

        List<SiteMarker> markers = new ArrayList<SiteMarker>(sites.size());
        for(SiteData site : sites) {

            if(site.hasLatLong()) {

                int ids[] = new int[1];
                ids[0] = site.getId();

                SiteMarker dto = new SiteMarker(ids,
                        (float)site.getLongitude(), (float)site.getLatitude());

                markers.add(dto);
            }

        }

        return new SiteMarkerCollection(markers);

    }
}
