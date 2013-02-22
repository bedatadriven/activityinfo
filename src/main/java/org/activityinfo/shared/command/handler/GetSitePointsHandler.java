package org.activityinfo.shared.command.handler;

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

import java.util.ArrayList;
import java.util.List;

import org.activityinfo.shared.command.Filter;
import org.activityinfo.shared.command.GetSitePoints;
import org.activityinfo.shared.command.GetSites;
import org.activityinfo.shared.command.result.SitePointList;
import org.activityinfo.shared.command.result.SiteResult;
import org.activityinfo.shared.dto.SiteDTO;
import org.activityinfo.shared.dto.SitePointDTO;
import org.activityinfo.shared.report.model.DimensionType;
import org.activityinfo.shared.util.mapping.Extents;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class GetSitePointsHandler implements
    CommandHandlerAsync<GetSitePoints, SitePointList> {

    @Override
    public void execute(GetSitePoints command, ExecutionContext context,
        final AsyncCallback<SitePointList> callback) {

        Filter filter = new Filter();
        if (command.getActivityId() != 0) {
            filter.addRestriction(DimensionType.Activity,
                command.getActivityId());
        }

        context.execute(new GetSites(filter), new AsyncCallback<SiteResult>() {
            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            @Override
            public void onSuccess(SiteResult result) {
                callback.onSuccess(toPointList(result.getData()));
            }
        });
    }

    protected SitePointList toPointList(List<SiteDTO> sites) {
        Extents bounds = Extents.empty();

        List<SitePointDTO> points = new ArrayList<SitePointDTO>(sites.size());
        for (SiteDTO site : sites) {
            if (site.hasLatLong()) {
                points.add(new SitePointDTO(site.getId(), site.getLongitude(),
                    site.getLatitude()));
                bounds.grow(site.getLatitude(), site.getLongitude());
            }
        }
        return new SitePointList(bounds, points);
    }
}
