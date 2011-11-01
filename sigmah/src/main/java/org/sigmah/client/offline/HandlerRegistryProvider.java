package org.sigmah.client.offline;

import org.sigmah.client.offline.command.HandlerRegistry;
import org.sigmah.shared.command.AddLocation;
import org.sigmah.shared.command.CreateSite;
import org.sigmah.shared.command.GeneratePivotTable;
import org.sigmah.shared.command.GetAdminEntities;
import org.sigmah.shared.command.GetLocations;
import org.sigmah.shared.command.GetPartnersWithSites;
import org.sigmah.shared.command.GetSchema;
import org.sigmah.shared.command.GetSites;
import org.sigmah.shared.command.GetSitesWithoutCoordinates;
import org.sigmah.shared.command.PivotSites;
import org.sigmah.shared.command.SitesPerAdminEntity;
import org.sigmah.shared.command.SitesPerTime;
import org.sigmah.shared.command.UpdateSite;
import org.sigmah.shared.command.handler.AddLocationHandler;
import org.sigmah.shared.command.handler.CreateSiteHandler;
import org.sigmah.shared.command.handler.GeneratePivotTableHandler;
import org.sigmah.shared.command.handler.GetAdminEntitiesHandler;
import org.sigmah.shared.command.handler.GetLocationsHandler;
import org.sigmah.shared.command.handler.GetPartnersWithSitesHandler;
import org.sigmah.shared.command.handler.GetSchemaHandler;
import org.sigmah.shared.command.handler.GetSitesHandler;
import org.sigmah.shared.command.handler.GetSitesWithoutCoordinatesHandler;
import org.sigmah.shared.command.handler.PivotSitesHandler;
import org.sigmah.shared.command.handler.SitesPerAdminEntityHandler;
import org.sigmah.shared.command.handler.SitesPerTimeHandler;
import org.sigmah.shared.command.handler.UpdateSiteHandler;

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
            AddLocationHandler addLocationHandler,
            SitesPerAdminEntityHandler sitesPerAdminEntityHandler,
            SitesPerTimeHandler sitesPerTimeHandler,
            GetLocationsHandler getLocationsHandler,
            //SearchHandler searchHandler,
            GetSitesWithoutCoordinatesHandler sitesWithoutCoordinateHandler,
            PivotSitesHandler pivotSitesHandler,
            GeneratePivotTableHandler generatePivotTableHandler) {
		
		registry = new HandlerRegistry();
    	registry.registerHandler(GetSchema.class, schemaHandler);
    	registry.registerHandler(GetSites.class, sitesHandler);
    	registry.registerHandler(GetAdminEntities.class, adminHandler);
    	registry.registerHandler(GetPartnersWithSites.class, partnersWithSitesHandler);
    	registry.registerHandler(CreateSite.class, createSiteHandler);
    	registry.registerHandler(UpdateSite.class, updateSiteHandler);
    	registry.registerHandler(AddLocation.class, addLocationHandler);
    	registry.registerHandler(SitesPerAdminEntity.class, sitesPerAdminEntityHandler);
    	registry.registerHandler(SitesPerTime.class, sitesPerTimeHandler);
    	//registry.registerHandler(Search.class, searchHandler);
    	registry.registerHandler(GetSitesWithoutCoordinates.class, sitesWithoutCoordinateHandler);
    	registry.registerHandler(GetLocations.class, getLocationsHandler);
    	registry.registerHandler(GeneratePivotTable.class, generatePivotTableHandler);
    	registry.registerHandler(PivotSites.class, pivotSitesHandler);
	}

	@Override
	public HandlerRegistry get() {
		return registry;
	}	
}
