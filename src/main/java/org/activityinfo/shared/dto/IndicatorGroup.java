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

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.data.BaseModelData;

/**
 * Convenience class for groups of Indicators, which are currently not modeled
 * as entities by as properties of Indicator.
 * 
 * See {@link ActivityDTO#groupIndicators()}
 * 
 * @author Alex Bertram (akbertram@gmail.com)
 */
public final class IndicatorGroup extends BaseModelData implements ProvidesKey {

    private List<IndicatorDTO> indicators = new ArrayList<IndicatorDTO>();

    public IndicatorGroup(String name) {
        set("name", name);
    }

    /**
     * Returns the name of the IndicatorGroup; corresponds to
     * {@link IndicatorDTO#getCategory()}
     * 
     * @return the name of the IndicatorGroup
     */
    public String getName() {
        return get("name");
    }

    public List<IndicatorDTO> getIndicators() {
        return indicators;
    }

    public void addIndicator(IndicatorDTO indicator) {
        indicators.add(indicator);
    }

    public void setActivityId(int activityId) {
        set("activityId", activityId);
    }

    public int getActivityId() {
        return (Integer) get("activityId");
    }

    @Override
    public String getKey() {
        return "group" + getName() + getActivityId();
    }

}
