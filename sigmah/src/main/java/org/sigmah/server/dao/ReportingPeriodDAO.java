/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.dao;

import org.sigmah.server.domain.ReportingPeriod;

/**
 * Data Access Object for {@link org.sigmah.server.domain.ReportingPeriod} domain classes.
 *
 * @author Alex Bertram
 */
public interface ReportingPeriodDAO extends DAO<ReportingPeriod, Integer> {

    /**
     * Efficiently adds an {@link org.sigmah.server.domain.IndicatorValue IndicatorValue} to the given
     * {@link org.sigmah.server.domain.ReportingPeriod}
     * @param reportingPeriodId
     * @param indicatorId
     * @param value
     */
    void addIndicatorValue(int reportingPeriodId, int indicatorId, double value);

    /**
     * Efficiently adds updates an {@link org.sigmah.server.domain.IndicatorValue IndicatorValue} to the given
     * value. If the {@link org.sigmah.server.domain.IndicatorValue IndicatorValue} object does
     * not exist it is inserted.
     *
     * @param reportingPeriodId
     * @param indicatorId
     * @param value
     */
    void updateIndicatorValue(int reportingPeriodId, int indicatorId, Double value);    

}
