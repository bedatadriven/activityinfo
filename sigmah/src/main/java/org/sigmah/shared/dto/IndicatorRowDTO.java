/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.dto;

import com.extjs.gxt.ui.client.data.BaseModel;
import org.sigmah.shared.command.Month;

/**
 *
 * Projection DTO of the {@link org.sigmah.shared.domain.ReportingPeriod ReportingPeriod},
 * {@link org.sigmah.shared.domain.IndicatorValue IndicatorValue} and
 * {@link org.sigmah.shared.domain.Indicator Indicator}
 * entities.
 * 
 * Each IndicatorRowDTO contains values for a single {@link org.sigmah.shared.domain.Site Site},
 * and a single Indicator, but values (stored as properties) for a series of 
 * {@link org.sigmah.shared.domain.ReportingPeriod ReportingPeriod}
 * 
 *
 * @author Alex Bertram
 */
public final class IndicatorRowDTO extends BaseModel implements DTO {

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
     * @return The id of the {@link org.sigmah.shared.domain.Activity Activity}
     * to which the row's {@link org.sigmah.shared.domain.Site Site}
     * belongs.
     */
    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    /**
     *
     * @return The id of the {@link org.sigmah.shared.domain.Indicator Indicator}
     */
    public int getIndicatorId() {
        return indicatorId;
    }

    public void setIndicatorId(int indicatorId) {
        this.indicatorId = indicatorId;
    }

    /**
     *
     * @return The name of the {@link org.sigmah.shared.domain.Indicator}.
     */
    public String getIndicatorName() {
        return get("indicatorName");
    }

    public void setIndicatorName(String name) {
        set("indicatorName", name);
    }

    /**
     * The value of the Indicator for the
     * {@link org.sigmah.shared.domain.ReportingPeriod ReportingPeriod}
     * corresponding to the given <code>year</code> and <code>month</code>
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
