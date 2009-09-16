package org.activityinfo.server.command.handler;

import org.activityinfo.server.dao.hibernate.SiteTableDAO;
import org.activityinfo.server.domain.User;
import org.activityinfo.server.report.generator.SiteDataBinder;
import org.activityinfo.shared.command.GetSitePoints;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.dto.*;
import org.activityinfo.shared.exception.CommandException;
import org.activityinfo.shared.report.content.SiteData;
import org.hibernate.criterion.Restrictions;
import com.google.inject.Inject;

import java.util.List;
import java.util.ArrayList;

/**
 * @author Alex Bertram (akbertram@gmail.com)
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
        List<SiteData> sites = dao.query(user,  Restrictions.eq("activity.id", cmd.getActivityId()), null,
                new SiteDataBinder(), SiteTableDAO.RETRIEVE_NONE, 0, -1);

        Bounds bounds = new Bounds();

        List<SitePoint> points = new ArrayList<SitePoint>(sites.size());
        for(SiteData site : sites) {

            if(site.hasLatLong()) {

                points.add(new SitePoint(site.getLatitude(), site.getLongitude(), site.getId()));
                bounds.grow(site.getLatitude(), site.getLongitude());

            }

        }

        return new SitePointCollection(bounds, points);

    }
}
