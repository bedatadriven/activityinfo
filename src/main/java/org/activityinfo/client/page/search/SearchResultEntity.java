package org.activityinfo.client.page.search;

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

import org.activityinfo.shared.report.model.DimensionType;

import com.extjs.gxt.ui.client.data.BaseModelData;

/**
 * Simple tuple for a filter hit
 */
public final class SearchResultEntity extends BaseModelData {

    public SearchResultEntity(int id, String name, String url,
        DimensionType dimensionType) {
        setId(id);
        setName(name);
        setUrl(url);
        setDimensionType(dimensionType);
    }

    public int getId() {
        return (Integer) get("id");
    }

    public void setId(int id) {
        set("id", id);
    }

    public String getName() {
        return (String) get("name");
    }

    public void setName(String name) {
        set("name", name);
    }

    public String getUrl() {
        return (String) get("url");
    }

    public void setUrl(String url) {
        set("url", url);
    }

    public DimensionType getDimension() {
        return (DimensionType) get("dimensionType");
    }

    public void setDimensionType(DimensionType dimensionType) {
        set("dimensionType", dimensionType);
    }
}
