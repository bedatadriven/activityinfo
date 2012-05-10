/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.command.handler;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;

import org.activityinfo.client.offline.command.handler.KeyGenerator;
import org.activityinfo.shared.command.Month;
import org.activityinfo.shared.command.UpdateMonthlyReports;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.command.result.VoidResult;
import org.activityinfo.shared.exception.CommandException;
import org.sigmah.server.database.hibernate.entity.Indicator;
import org.sigmah.server.database.hibernate.entity.IndicatorValue;
import org.sigmah.server.database.hibernate.entity.ReportingPeriod;
import org.sigmah.server.database.hibernate.entity.Site;
import org.sigmah.server.database.hibernate.entity.User;

import com.google.inject.Inject;

/**
 * @author Alex Bertram
 * @see org.activityinfo.shared.command.UpdateMonthlyReports
 */
public class UpdateMonthlyReportsHandler implements CommandHandler<UpdateMonthlyReports> {

    private final EntityManager em;
    private final KeyGenerator keyGenerator;

    @Inject
    public UpdateMonthlyReportsHandler(EntityManager em, KeyGenerator keyGenerator) {
        this.em = em;
        this.keyGenerator = keyGenerator;
    }

    public CommandResult execute(UpdateMonthlyReports cmd, User user) throws CommandException {

        Site site = em.find(Site.class, cmd.getSiteId());

        Map<Month, ReportingPeriod> periods = new HashMap<Month, ReportingPeriod>();

        for (ReportingPeriod period : site.getReportingPeriods()) {
            periods.put(HandlerUtil.monthFromRange(period.getDate1(), period.getDate2()), period);
        }

        for (UpdateMonthlyReports.Change change : cmd.getChanges()) {

            ReportingPeriod period = periods.get(change.getMonth());
            if (period == null) {
                period = new ReportingPeriod(site);
                period.setId(keyGenerator.generateInt());
                
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, change.getMonth().getYear());
                calendar.set(Calendar.MONTH, change.getMonth().getMonth() - 1);
                calendar.set(Calendar.DATE, 1);
                period.setDate1(calendar.getTime());

                calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
                period.setDate2(calendar.getTime());

                em.persist(period);

                periods.put(change.getMonth(), period);
            }

            updateIndicatorValue(em, period, change.getIndicatorId(), change.getValue(), false);
        }

        return new VoidResult();

    }


    public void updateIndicatorValue(EntityManager em, ReportingPeriod period, int indicatorId, Double value, boolean creating) {


        if (value == null && !creating) {
            int rowsAffected = em.createQuery("delete IndicatorValue v where v.indicator.id = ?1 and v.reportingPeriod.id = ?2")
                    .setParameter(1, indicatorId)
                    .setParameter(2, period.getId())
                    .executeUpdate();

            assert rowsAffected <= 1 : "whoops, deleted too many";

        } else if (value != null) {

            int rowsAffected = 0;

            if (!creating) {
                rowsAffected = em.createQuery("update IndicatorValue v set v.value = ?1 where " +
                        "v.indicator.id = ?2 and " +
                        "v.reportingPeriod.id = ?3")
                        .setParameter(1, (Double) value)
                        .setParameter(2, indicatorId)
                        .setParameter(3, period.getId())
                        .executeUpdate();
            }

            if (rowsAffected == 0) {

                IndicatorValue iValue = new IndicatorValue(
                        period,
                        em.getReference(Indicator.class, indicatorId),
                        (Double) value);

                em.persist(iValue);

            }
        }

    }

}
