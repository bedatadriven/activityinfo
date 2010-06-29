/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
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
