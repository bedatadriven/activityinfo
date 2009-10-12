package org.activityinfo.shared.dto;

import com.extjs.gxt.ui.client.data.BaseModel;
import org.activityinfo.shared.command.Month;

/**
 *
 * An indicator row is a projection of the {@link org.activityinfo.server.domain.ReportingPeriod},
 * {@link org.activityinfo.server.domain.IndicatorValue} and {@link org.activityinfo.server.domain.Indicator}
 * entities.
 *
 * @author Alex Bertram
 */
public class IndicatorRow extends BaseModel implements DTO {

    private int siteId;
    private int activityId;
    private int indicatorId;

    /**
     *
     * @return The id of the site to which this row belongs.
     */
    public int getSiteId() {
        return siteId;
    }

    public void setSiteId(int siteId) {
        this.siteId = siteId;
    }

    /**
     *
     * @return The id of the activity to which the row's site belongs.
     */
    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    /**
     *
     * @return The id of the indicator
     */
    public int getIndicatorId() {
        return indicatorId;
    }

    public void setIndicatorId(int indicatorId) {
        this.indicatorId = indicatorId;
    }

    /**
     *
     * @return The name of the indicator.
     */
    public String getIndicatorName() {
        return get("indicatorName");
    }

    public void setIndicatorName(String name) {
        set("indicatorName", name);
    }

    /**
     * The value of the 
     *
     * @param year
     * @param month
     * @return
     */
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
