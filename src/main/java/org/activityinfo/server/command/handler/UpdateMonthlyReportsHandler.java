package org.activityinfo.server.command.handler;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import java.util.Calendar;
import java.util.Comparator;
import java.util.Map;

import javax.persistence.EntityManager;

import org.activityinfo.client.local.command.handler.KeyGenerator;
import org.activityinfo.server.database.hibernate.entity.Indicator;
import org.activityinfo.server.database.hibernate.entity.IndicatorValue;
import org.activityinfo.server.database.hibernate.entity.ReportingPeriod;
import org.activityinfo.server.database.hibernate.entity.Site;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.server.event.sitechange.ChangeType;
import org.activityinfo.server.event.sitehistory.SiteHistoryProcessor;
import org.activityinfo.shared.command.Month;
import org.activityinfo.shared.command.UpdateMonthlyReports;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.command.result.VoidResult;
import org.activityinfo.shared.dto.IndicatorDTO;
import org.activityinfo.shared.exception.CommandException;

import com.google.common.collect.Maps;
import com.google.inject.Inject;

/**
 * @author Alex Bertram
 * @see org.activityinfo.shared.command.UpdateMonthlyReports
 */
public class UpdateMonthlyReportsHandler implements
    CommandHandler<UpdateMonthlyReports> {

    private final EntityManager em;
    private final KeyGenerator keyGenerator;
    private final SiteHistoryProcessor siteHistoryProcessor;

    @Inject
    public UpdateMonthlyReportsHandler(EntityManager em,
        KeyGenerator keyGenerator, SiteHistoryProcessor siteHistoryProcessor) {
        this.em = em;
        this.keyGenerator = keyGenerator;
        this.siteHistoryProcessor = siteHistoryProcessor;
    }

    public static void main(String[] args) {
        String o1 ="I123M2009-1";
        System.out.println();
        System.out.println(o1.substring(o1.indexOf('-') + 1));
    }

    @Override
    public CommandResult execute(UpdateMonthlyReports cmd, User user)
        throws CommandException {

        Site site = em.find(Site.class, cmd.getSiteId());
        if (site == null) {
            throw new CommandException(cmd, "site " + cmd.getSiteId() + " not found for user " + user.getEmail());
        }

        Map<Month, ReportingPeriod> periods = Maps.newHashMap();
        Map<String, Object> siteHistoryChangeMap = createChangeMap();
        
        for (ReportingPeriod period : site.getReportingPeriods()) {
            periods.put(HandlerUtil.monthFromRange(period.getDate1(),
                period.getDate2()), period);
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

            siteHistoryChangeMap.put(
                IndicatorDTO.getPropertyName(change.getIndicatorId(), change.getMonth()),
                change.getValue());
        }
        
        siteHistoryProcessor.persistHistory(site, user, ChangeType.UPDATE, siteHistoryChangeMap);

        return new VoidResult();
    }

    public void updateIndicatorValue(EntityManager em, ReportingPeriod period,
        int indicatorId, Double value, boolean creating) {

        if (value == null && !creating) {
            int rowsAffected = em.createQuery(
                    "delete IndicatorValue v where v.indicator.id = ?1 and v.reportingPeriod.id = ?2")
                .setParameter(1, indicatorId)
                .setParameter(2, period.getId())
                .executeUpdate();

            assert rowsAffected <= 1 : "whoops, deleted too many";

        } else if (value != null) {
            int rowsAffected = 0;

            if (!creating) {
                rowsAffected = em.createQuery(
                        "update IndicatorValue v set v.value = ?1 where " +
                            "v.indicator.id = ?2 and " +
                            "v.reportingPeriod.id = ?3")
                    .setParameter(1, value)
                    .setParameter(2, indicatorId)
                    .setParameter(3, period.getId())
                    .executeUpdate();
            }

            if (rowsAffected == 0) {
                IndicatorValue iValue =
                    new IndicatorValue(period, em.getReference(Indicator.class, indicatorId), value);
                em.persist(iValue);
            }
        }
    }

    private Map<String, Object> createChangeMap() {
        return Maps.newTreeMap(new Comparator<String>() {
            @Override
            // comparing eg. I345M2009-7, first part as string, part after the dash as number
            public int compare(String o1, String o2) {
                int result = 
                    o1.substring(0, o1.indexOf('-')).compareToIgnoreCase(o2.substring(0, o2.indexOf('-')));
                if (result == 0) {
                    String m1 = o1.substring(o1.indexOf('-') + 1);
                    String m2 = o2.substring(o2.indexOf('-') + 1);
                    result = Integer.valueOf(m1).compareTo(Integer.valueOf(m2));
                }
                return result;
            }
        });
    }
}
