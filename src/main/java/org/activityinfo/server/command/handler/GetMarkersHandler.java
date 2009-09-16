package org.activityinfo.server.command.handler;

import org.activityinfo.server.dao.hibernate.SiteTableDAO;
import org.activityinfo.server.domain.User;
import org.activityinfo.server.report.generator.SiteDataBinder;
import org.activityinfo.shared.command.GetMarkers;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.dto.SiteMarker;
import org.activityinfo.shared.dto.SiteMarkerCollection;
import org.activityinfo.shared.exception.CommandException;
import org.activityinfo.shared.report.content.SiteData;

import com.google.inject.Inject;

import java.util.List;
import java.util.ArrayList;

/**
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
