/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.dao.hibernate;

import com.google.inject.Inject;
import org.sigmah.server.dao.ReportingPeriodDAO;
import org.sigmah.server.domain.Indicator;
import org.sigmah.server.domain.IndicatorValue;
import org.sigmah.server.domain.ReportingPeriod;

import javax.persistence.EntityManager;

/**
 * @author Alex Bertram
 */
public class ReportingPeriodHibernateDAO extends GenericDAO<ReportingPeriod, Integer> implements ReportingPeriodDAO {

    @Inject
    public ReportingPeriodHibernateDAO(EntityManager em) {
        super(em);
    }

    @Override
    public void addIndicatorValue(int reportingPeriodId, int indicatorId, double value) {
        addValueRow(reportingPeriodId, indicatorId, value);
    }

    @Override
    public void updateIndicatorValue(int reportingPeriodId, int indicatorId, Double value) {
        if (value == null) {
            removeValueRow(reportingPeriodId, indicatorId);
        } else  {
            int rowsAffected = updateValueRow(reportingPeriodId, indicatorId, value);
            if (rowsAffected == 0) {
                addValueRow(reportingPeriodId, indicatorId, value);
            }
        }
    }

    private void addValueRow(int reportingPeriodId, int indicatorId, Double value) {
        IndicatorValue iValue = new IndicatorValue(
                em.getReference(ReportingPeriod.class, reportingPeriodId),
                em.getReference(Indicator.class, indicatorId),
                (Double) value);

        em.persist(iValue);
    }

    private int updateValueRow(int reportingPeriodId, int indicatorId, double value) {
        int rowsAffected;
        rowsAffected = em.createQuery("update IndicatorValue v set v.value = ?1 where " +
                "v.indicator.id = ?2 and " +
                "v.reportingPeriod.id = ?3")
                .setParameter(1, value)
                .setParameter(2, indicatorId)
                .setParameter(3, reportingPeriodId)
                .executeUpdate();
        return rowsAffected;
    }

    private void removeValueRow(int reportingPeriodId, int indicatorId) {
        int rowsAffected = em.createQuery("delete IndicatorValue v where v.indicator.id = ?1 and v.reportingPeriod.id = ?2")
                .setParameter(1, indicatorId)
                .setParameter(2, reportingPeriodId)
                .executeUpdate();

        assert rowsAffected <= 1 : "whoops, deleted too many";
    }


}
