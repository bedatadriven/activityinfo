package org.activityinfo.shared.dto;

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

import java.util.HashMap;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.google.common.collect.Maps;

public class IndicatorLinkDTO extends BaseModelData implements EntityDTO {

    public static final String ENTITY_NAME = "IndicatorLink";

    public IndicatorLinkDTO() {

    }

    public void setSourceIndicator(int id) {
        set("sourceIndicatorId", id);
    }

    public int getSourceIndicator() {
        return (Integer) get("sourceIndicatorId");
    }

    public void setDestinationIndicator(
        HashMap<Integer, String> destinationIndicators) {
        set("destinationIndicators", destinationIndicators);
    }

    public HashMap<Integer, String> getDestinationIndicator() {
        HashMap<Integer, String> map = (HashMap<Integer, String>) get("destinationIndicators");
        if (map == null) {
            map = Maps.newHashMap();
            setDestinationIndicator(map);
        }
        return map;
    }

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public String getEntityName() {

        return ENTITY_NAME;
    }

    @Override
    public String getName() {
        return ENTITY_NAME;
    }

}
