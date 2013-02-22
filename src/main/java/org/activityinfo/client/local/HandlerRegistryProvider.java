package org.activityinfo.client.local;

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

import org.activityinfo.client.local.command.HandlerRegistry;
import org.activityinfo.shared.command.CreateLocation;
import org.activityinfo.shared.command.CreateSite;
import org.activityinfo.shared.command.DeleteSite;
import org.activityinfo.shared.command.GeneratePivotTable;
import org.activityinfo.shared.command.GetAdminEntities;
import org.activityinfo.shared.command.GetLocations;
import org.activityinfo.shared.command.GetPartnersWithSites;
import org.activityinfo.shared.command.GetSchema;
import org.activityinfo.shared.command.GetSiteAttachments;
import org.activityinfo.shared.command.GetSites;
import org.activityinfo.shared.command.PivotSites;
import org.activityinfo.shared.command.SearchLocations;
import org.activityinfo.shared.command.UpdateSite;
import org.activityinfo.shared.command.handler.CreateLocationHandler;
import org.activityinfo.shared.command.handler.CreateSiteHandler;
import org.activityinfo.shared.command.handler.DeleteSiteHandler;
import org.activityinfo.shared.command.handler.GeneratePivotTableHandler;
import org.activityinfo.shared.command.handler.GetAdminEntitiesHandler;
import org.activityinfo.shared.command.handler.GetLocationsHandler;
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
        SearchLocationsHandler searchLocationsHandler,
        // SearchHandler searchHandler,
        PivotSitesHandler pivotSitesHandler,
        GeneratePivotTableHandler generatePivotTableHandler,
        GetLocationsHandler getLocationsHandler,
        DeleteSiteHandler deleteSiteHandler,
        GetSiteAttachmentsHandler getSiteAttachmentsHandler) {

        registry = new HandlerRegistry();
        registry.registerHandler(GetSchema.class, schemaHandler);
        registry.registerHandler(GetSites.class, sitesHandler);
        registry.registerHandler(GetAdminEntities.class, adminHandler);
        registry.registerHandler(GetPartnersWithSites.class,
            partnersWithSitesHandler);
        registry.registerHandler(CreateSite.class, createSiteHandler);
        registry.registerHandler(UpdateSite.class, updateSiteHandler);
        registry.registerHandler(CreateLocation.class, createLocationHandler);
        // registry.registerHandler(Search.class, searchHandler);
        registry.registerHandler(SearchLocations.class, searchLocationsHandler);
        registry.registerHandler(GeneratePivotTable.class,
            generatePivotTableHandler);
        registry.registerHandler(PivotSites.class, pivotSitesHandler);
        registry.registerHandler(GetLocations.class, getLocationsHandler);
        registry.registerHandler(DeleteSite.class, deleteSiteHandler);
        registry.registerHandler(GetSiteAttachments.class,
            getSiteAttachmentsHandler);
    }

    @Override
    public HandlerRegistry get() {
        return registry;
    }
}
