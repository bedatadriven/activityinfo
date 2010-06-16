package org.activityinfo.shared.command.result;

import org.activityinfo.shared.map.BaseMap;

import java.util.List;

/**
 *
 * List of <code>BaseMap</code>s returned by the <code>GetBaseMaps</code> command.
 *
 * @see org.activityinfo.shared.map.BaseMap
 * @see org.activityinfo.shared.command.GetBaseMaps
 *
 * @author Alex Bertram
 */
public class BaseMapResult implements CommandResult  {

    List<BaseMap> baseMaps;

    private BaseMapResult() {
    }

    public BaseMapResult(List<BaseMap> baseMaps) {
        this.baseMaps = baseMaps;
    }

    public List<BaseMap> getBaseMaps() {
        return baseMaps;
    }

    public void setBaseMaps(List<BaseMap> baseMaps) {
        this.baseMaps = baseMaps;
    }
}
