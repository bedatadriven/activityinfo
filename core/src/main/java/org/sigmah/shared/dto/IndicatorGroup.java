/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.dto;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.data.BaseModelData;

/**
 * Convenience class for groups of Indicators, which are currently not modeled as
 * entities by as properties of Indicator.
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
		set("activityId",activityId);
	}

	public int getActivityId() {
		return (Integer)get("activityId");
	}

	@Override
	public String getKey() {
		return "group"+getName() + getActivityId();
	}

}
