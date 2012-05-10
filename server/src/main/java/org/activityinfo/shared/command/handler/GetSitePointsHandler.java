/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.shared.command.handler;

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
import org.activityinfo.shared.util.mapping.BoundingBoxDTO;

import com.google.gwt.user.client.rpc.AsyncCallback;


public class GetSitePointsHandler implements CommandHandlerAsync<GetSitePoints, SitePointList> {


	@Override
	public void execute(GetSitePoints command, ExecutionContext context,
			final AsyncCallback<SitePointList> callback) {

    	Filter filter = new Filter();
    	if(command.getActivityId() != 0) {
    		filter.addRestriction(DimensionType.Activity, command.getActivityId());
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
        BoundingBoxDTO bounds = BoundingBoxDTO.empty();

        List<SitePointDTO> points = new ArrayList<SitePointDTO>(sites.size());
        for (SiteDTO site : sites) {
            if (site.hasLatLong()) {
                points.add(new SitePointDTO(site.getId(), site.getLongitude(), site.getLatitude()));
                bounds.grow(site.getLatitude(), site.getLongitude());
            }
        }
        return new SitePointList(bounds, points);
	}
}
