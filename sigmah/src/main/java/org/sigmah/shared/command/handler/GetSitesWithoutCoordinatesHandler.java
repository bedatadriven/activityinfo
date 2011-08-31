package org.sigmah.shared.command.handler;

import java.util.HashSet;
import java.util.Set;

import org.sigmah.shared.command.GetLocationsWithoutGpsCoordinates;
import org.sigmah.shared.command.GetSites;
import org.sigmah.shared.command.GetSitesWithoutCoordinates;
import org.sigmah.shared.command.result.LocationsWithoutGpsResult;
import org.sigmah.shared.command.result.SiteResult;
import org.sigmah.shared.dao.Filter;
import org.sigmah.shared.dto.LocationDTO;
import org.sigmah.shared.report.model.DimensionType;

import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.data.SortInfo;
import com.google.gwt.user.client.rpc.AsyncCallback;

/*
 * Gets a list of sites where the referenced location does not have coordinates
 * 
 * 1. grab list of locations without X/Y
 * 2. create filter
 * 3. grab 10 latest sites using filter
*/
public class GetSitesWithoutCoordinatesHandler implements CommandHandlerAsync<GetSitesWithoutCoordinates, SiteResult> {

	@Override
	public void execute(GetSitesWithoutCoordinates command,
			final ExecutionContext context, final AsyncCallback<SiteResult> callback) {
		
		context.execute(new GetLocationsWithoutGpsCoordinates(), new AsyncCallback<LocationsWithoutGpsResult>() {
			@Override
			public void onFailure(Throwable caught) {
				callback.onFailure(caught);
			}
			@Override
			public void onSuccess(LocationsWithoutGpsResult result) {
				Filter filter = new Filter();
				Set<Integer> locationIds = new HashSet<Integer>();
				for (LocationDTO location : result.getData()) {
					locationIds.add(location.getId());
				}
				filter.addRestriction(DimensionType.Location, locationIds);
				
				GetSites getSites = new GetSites();
				getSites.setLimit(10);
				getSites.setSortInfo(new SortInfo("DateEdited", SortDir.DESC));
				getSites.setFilter(filter);
				context.execute(getSites, new AsyncCallback<SiteResult>() {
					@Override
					public void onFailure(Throwable caught) {
						callback.onFailure(caught);
					}
					@Override
					public void onSuccess(SiteResult result) {
						callback.onSuccess(result);
					}
				});
			}
		});
	}

}
