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

import org.activityinfo.shared.command.result.VoidResult;
import org.activityinfo.shared.dto.AdminLevelDTO;
import org.activityinfo.shared.dto.PartnerDTO;
import org.activityinfo.shared.dto.ProjectDTO;
import org.activityinfo.shared.dto.SiteDTO;

import com.extjs.gxt.ui.client.data.RpcMap;

public class UpdateSite implements MutatingCommand<VoidResult>, SiteCommand {

    private int siteId;
    private RpcMap changes;

    public UpdateSite() {

    }

    public UpdateSite(int siteId, RpcMap changes) {
        this.siteId = siteId;
        this.changes = changes;
    }

    public UpdateSite(int siteId, Map<String, Object> changes) {
        this.siteId = siteId;
        this.changes = new RpcMap();
        this.changes.putAll(changes);
    }

    public UpdateSite(SiteDTO original, SiteDTO updated) {
        assert original.getId() == updated.getId();
        this.siteId = updated.getId();
        changes = new RpcMap();
        for (String property : updated.getProperties().keySet()) {
            Object newValue = updated.get(property);
            if (isChanged(original.get(property), newValue)) {
                if (property.equals("partner")) {
                    changes.put("partnerId", newValue == null ? null
                        : ((PartnerDTO) newValue).getId());
                } else if (property.equals("project")) {
                    changes.put("projectId", newValue == null ? null
                        : ((ProjectDTO) newValue).getId());
                } else if (propertyCanBeModified(property)) {
                    changes.put(property, newValue);
                }
            }
        }
    }

    private boolean propertyCanBeModified(String property) {
        return !(property.equals("activityId") || property
            .startsWith(AdminLevelDTO.PROPERTY_PREFIX));
    }

    private boolean isChanged(Object a, Object b) {
        if (a == null) {
            return b != null;
        } else {
            return !a.equals(b);
        }
    }

    @Override
    public int getSiteId() {
        return siteId;
    }

    public void setSiteId(int siteId) {
        this.siteId = siteId;
    }

    @Override
    public RpcMap getProperties() {
        return getChanges();
    }

    public RpcMap getChanges() {
        return changes;
    }

    public void setChanges(RpcMap changes) {
        this.changes = changes;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((changes == null) ? 0 : changes.hashCode());
        result = prime * result + siteId;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        UpdateSite other = (UpdateSite) obj;
        if (changes == null) {
            if (other.changes != null) {
                return false;
            }
        } else if (!changes.equals(other.changes)) {
            return false;
        }
        if (siteId != other.siteId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "UpdateSite{ id=" + siteId + ", changes="
            + changes.getTransientMap().toString() + "}";
    }
}
