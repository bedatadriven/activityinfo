package org.activityinfo.shared.dto;

import com.extjs.gxt.ui.client.data.BaseModel;

import org.activityinfo.server.domain.IndicatorValue;
import org.activityinfo.shared.command.Month;

public class IndicatorRow extends BaseModel implements DTO {

    private int siteId;
    private int activityId;
    private int indicatorId;

    public int getSiteId() {
        return siteId;
    }

    public void setSiteId(int siteId) {
        this.siteId = siteId;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public int getIndicatorId() {
        return indicatorId;
    }

    public void setIndicatorId(int indicatorId) {
        this.indicatorId = indicatorId;
    }

    public String getIndicatorName() {
        return get("indicatorName");
    }

    public void setIndicatorName(String name) {
        set("indicatorName", name);
    }

    public Double getValue(int year, int month) {
        return get(propertyName(year, month));
    }

    public void setValue(int year, int month, Double value) {
        set(propertyName(year, month), value);
    }

    public static String propertyName(int year, int month) {
        return "M" + year + "-" + month;
    }

    public static String propertyName(Month month) {
        return propertyName(month.getYear(), month.getMonth());
    }

    public void setValue(Month month, Double value) {
        setValue(month.getYear(), month.getMonth(), value);
    }

    public static Month monthForProperty(String property) {
        return Month.parseMonth(property.substring(1));
    }
}
