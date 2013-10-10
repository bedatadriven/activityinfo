package org.activityinfo.shared.command;

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

import java.util.Map;
import java.util.Map.Entry;

import org.activityinfo.shared.command.result.CreateResult;
import org.activityinfo.shared.dto.AdminLevelDTO;
import org.activityinfo.shared.dto.SiteDTO;

import com.bedatadriven.rebar.time.calendar.LocalDate;
import com.extjs.gxt.ui.client.data.RpcMap;

public class CreateSite implements MutatingCommand<CreateResult>, SiteCommand {

    private RpcMap properties;

    private CreateLocation nestedCommand;

    // ensure this class is cleared for deserialization
    private LocalDate date;

    public CreateSite() {
        properties = new RpcMap();
    }

    public CreateSite(SiteDTO site) {
        assert site.getId() != 0;
        assert site.getActivityId() != 0;
        assert site.getLocationId() != 0;
        assert site.getPartner() != null;

        properties = new RpcMap();
        for (Entry<String, Object> property : site.getProperties().entrySet()) {
            if (property.getKey().equals("partner")) {
                properties.put("partnerId", site.getPartner().getId());
            } else if (property.getKey().equals("project")) {
                if (site.getProject() != null) {
                    properties.put("projectId", site.getProject().getId());
                }
            } else if (!property.getKey().startsWith(
                AdminLevelDTO.PROPERTY_PREFIX)) {
                properties.put(property.getKey(), property.getValue());
            }
        }
    }

    public CreateSite(Map<String, Object> properties) {
        this.properties = new RpcMap();
        this.properties.putAll(properties);
    }

    @Override
    public int getSiteId() {
        return (Integer) properties.get("id");
    }

    public int getActivityId() {
        return (Integer) properties.get("activityId");
    }

    public int getPartnerId() {
        return (Integer) properties.get("partnerId");
    }

    public int getLocationId() {
        return (Integer) properties.get("locationId");
    }

    public Integer getReportingPeriodId() {
        return (Integer) properties.get("reportingPeriodId");
    }

    public CreateLocation getNestedCommand() {
        return nestedCommand;
    }

    public void setNestedCommand(CreateLocation nestedCommand) {
        this.nestedCommand = nestedCommand;
        this.properties.put("locationId", nestedCommand.getLocationId());
    }

    public boolean hasNestedCommand() {
        return nestedCommand != null;
    }

    @Override
    public RpcMap getProperties() {
        return properties;
    }

    public void setProperties(RpcMap properties) {
        this.properties = properties;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof CreateSite)) {
            return false;
        }
        return this.properties.equals(((CreateSite) obj).properties);
    }

    @Override
    public int hashCode() {
        return properties.hashCode();
    }

    @Override
    public String toString() {
        return "CreateSite{" + properties.toString() + "}";
    }
}
