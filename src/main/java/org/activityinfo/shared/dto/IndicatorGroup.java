package org.activityinfo.shared.dto;

import com.extjs.gxt.ui.client.data.BaseModelData;

import java.util.List;
import java.util.ArrayList;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class IndicatorGroup extends BaseModelData {

    private List<IndicatorModel> indicators = new ArrayList<IndicatorModel>();

    public IndicatorGroup(String name) {
        set("name", name);
    }

    public List<IndicatorModel> getIndicators() {
        return indicators;         
    }

    public void addIndicator(IndicatorModel indicator) {
        indicators.add(indicator);
    }

    public String getName() {
        return get("name");
    }
}
