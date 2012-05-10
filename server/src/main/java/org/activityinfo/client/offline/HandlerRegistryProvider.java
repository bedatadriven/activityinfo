package org.activityinfo.client.offline;

import org.activityinfo.client.offline.command.HandlerRegistry;
import org.activityinfo.shared.command.CreateLocation;
import org.activityinfo.shared.command.CreateSite;
import org.activityinfo.shared.command.GeneratePivotTable;
import org.activityinfo.shared.command.GetAdminEntities;
import org.activityinfo.shared.command.GetLocation;
import org.activityinfo.shared.command.GetPartnersWithSites;
import org.activityinfo.shared.command.GetSchema;
import org.activityinfo.shared.command.GetSiteAttachments;
import org.activityinfo.shared.command.GetSites;
import org.activityinfo.shared.command.PivotSites;
import org.activityinfo.shared.command.SearchLocations;
import org.activityinfo.shared.command.UpdateSite;
import org.activityinfo.shared.command.handler.CreateLocationHandler;
import org.activityinfo.shared.command.handler.CreateSiteHandler;
import org.activityinfo.shared.command.handler.GeneratePivotTableHandler;
import org.activityinfo.shared.command.handler.GetAdminEntitiesHandler;
import org.activityinfo.shared.command.handler.GetLocationHandler;
import org.activityinfo.shared.command.handler.GetPartnersWithSitesHandler;
import org.activityinfo.shared.command.handler.GetSchemaHandler;
import org.activityinfo.shared.command.handler.GetSiteAttachmentsHandler;
import org.activityinfo.shared.command.handler.GetSitesHandler;
import org.activityinfo.shared.command.handler.PivotSitesHandler;
import org.activityinfo.shared.command.handler.SearchLocationsHandler;
import org.activityinfo.shared.command.handler.UpdateSiteHandler;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class HandlerRegistryProvider implements Provider<HandlerRegistry> {

	private final HandlerRegistry registry; 
	
	@Inject
	public HandlerRegistryProvider(
			GetSchemaHandler schemaHandler,
            GetSitesHandler sitesHandler,
            GetAdminEntitiesHandler adminHandler,
            GetPartnersWithSitesHandler partnersWithSitesHandler,
            CreateSiteHandler createSiteHandler,
            UpdateSiteHandler updateSiteHandler,
            CreateLocationHandler createLocationHandler,
            SearchLocationsHandler getLocationsHandler,
            //SearchHandler searchHandler,
            PivotSitesHandler pivotSitesHandler,
            GeneratePivotTableHandler generatePivotTableHandler,
            GetLocationHandler getLocationHandler,
            GetSiteAttachmentsHandler getSiteAttachmentsHandler) {
		
		registry = new HandlerRegistry();
    	registry.registerHandler(GetSchema.class, schemaHandler);
    	registry.registerHandler(GetSites.class, sitesHandler);
    	registry.registerHandler(GetAdminEntities.class, adminHandler);
    	registry.registerHandler(GetPartnersWithSites.class, partnersWithSitesHandler);
    	registry.registerHandler(CreateSite.class, createSiteHandler);
    	registry.registerHandler(UpdateSite.class, updateSiteHandler);
    	registry.registerHandler(CreateLocation.class, createLocationHandler);
    	//registry.registerHandler(Search.class, searchHandler);
    	registry.registerHandler(SearchLocations.class, getLocationsHandler);
    	registry.registerHandler(GeneratePivotTable.class, generatePivotTableHandler);
    	registry.registerHandler(PivotSites.class, pivotSitesHandler);
    	registry.registerHandler(GetLocation.class, getLocationHandler);
    	registry.registerHandler(GetSiteAttachments.class, getSiteAttachmentsHandler);
	}

	@Override
	public HandlerRegistry get() {
		return registry;
	}	
}
