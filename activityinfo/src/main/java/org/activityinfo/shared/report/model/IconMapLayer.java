package org.activityinfo.shared.report.model;

import java.util.List;
import java.util.ArrayList;
/*
 * @author Alex Bertram
 */

public class IconMapLayer extends MapLayer {

    private List<Integer> indicatorIds = new ArrayList<Integer>(0);
    private List<Integer> activityIds = new ArrayList<Integer>(0);

    private String icon;
    private boolean clustered = true;

    public IconMapLayer() {
    }

    public List<Integer> getIndicatorIds() {
        return indicatorIds;
    }

    public void setIndicatorIds(List<Integer> indicatorIds) {
        this.indicatorIds = indicatorIds;
    }

    public List<Integer> getActivityIds() {
        return activityIds;
    }

    public void setActivityIds(List<Integer> activityIds) {
        this.activityIds = activityIds;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void addIndicator(int id) {
        indicatorIds.add(id);
    }

    public boolean isClustered() {
        return clustered;
    }

    public void setClustered(boolean clustered) {
        this.clustered = clustered;
    }

    public void addActivityId(int activityId) {
        activityIds.add(activityId);
    }
}
