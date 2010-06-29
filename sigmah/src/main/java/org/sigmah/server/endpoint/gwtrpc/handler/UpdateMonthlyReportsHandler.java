/*
 * This file is part of ActivityInfo.
 *
 * ActivityInfo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ActivityInfo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ActivityInfo.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2009 Alex Bertram and contributors.
 */

package org.sigmah.server.endpoint.gwtrpc.handler;

import com.google.inject.Inject;
import org.sigmah.server.domain.*;
import org.sigmah.shared.command.Month;
import org.sigmah.shared.command.UpdateMonthlyReports;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.command.result.VoidResult;
import org.sigmah.shared.exception.CommandException;

import javax.persistence.EntityManager;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Alex Bertram
 * @see org.sigmah.shared.command.UpdateMonthlyReports
 */
public class UpdateMonthlyReportsHandler implements CommandHandler<UpdateMonthlyReports> {

    private final EntityManager em;

    @Inject
    public UpdateMonthlyReportsHandler(EntityManager em) {
        this.em = em;
    }

    public CommandResult execute(UpdateMonthlyReports cmd, User user) throws CommandException {

        Site site = em.find(Site.class, cmd.getSiteId());

        Map<Month, ReportingPeriod> periods = new HashMap<Month, ReportingPeriod>();

        for (ReportingPeriod period : site.getReportingPeriods()) {
            periods.put(HandlerUtil.monthFromRange(period.getDate1(), period.getDate2()), period);
        }

        for (UpdateMonthlyReports.Change change : cmd.getChanges()) {

            ReportingPeriod period = periods.get(change.month);
            if (period == null) {
                period = new ReportingPeriod(site);

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, change.month.getYear());
                calendar.set(Calendar.MONTH, change.month.getMonth() - 1);
                calendar.set(Calendar.DATE, 1);
                period.setDate1(calendar.getTime());

                calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
                period.setDate2(calendar.getTime());

                em.persist(period);

                periods.put(change.month, period);
            }

            updateIndicatorValue(em, period, change.indicatorId, change.value, false);
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
