package org.activityinfo.shared.dto;

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

import org.activityinfo.client.page.entry.LockedPeriodSet;
import org.junit.Assert;
import org.junit.Test;

import com.bedatadriven.rebar.time.calendar.LocalDate;

public class LockedPeriodTest {

    @Test
    public void rangeTest() {
        UserDatabaseDTO db = new UserDatabaseDTO();

        LockedPeriodDTO lockedPeriod = new LockedPeriodDTO();
        lockedPeriod.setFromDate(new LocalDate(2000, 1, 1));
        lockedPeriod.setToDate(new LocalDate(2000, 1, 2));
        lockedPeriod.setEnabled(true);

        SiteDTO site = new SiteDTO(1);
        site.setDate1(new LocalDate(2000, 1, 1));
        site.setDate2(new LocalDate(2000, 1, 2));
        site.setActivityId(1);

        ActivityDTO activity = new ActivityDTO(1, "woei");
        activity.setDatabase(db);
        activity.getLockedPeriods().add(lockedPeriod);

        db.getActivities().add(activity);

        Assert.assertTrue("Site should fall within LockedPeriod",
            new LockedPeriodSet(activity).isLocked(site));

        LockedPeriodDTO lockedPeriod1 = new LockedPeriodDTO();
        lockedPeriod1.setFromDate(new LocalDate(2000, 1, 1));
        lockedPeriod1.setToDate(new LocalDate(2000, 1, 2));
        lockedPeriod1.setEnabled(true);

        SiteDTO site1 = new SiteDTO(2);
        site1.setActivityId(2);
        site1.setDate1(new LocalDate(2000, 1, 1));
        site1.setDate2(new LocalDate(2000, 1, 3));

        ActivityDTO activity1 = new ActivityDTO(2, "woei");
        activity1.setDatabase(db);
        activity1.getLockedPeriods().add(lockedPeriod1);

        LockedPeriodSet locks = new LockedPeriodSet(activity1);

        Assert.assertFalse("Site should NOT fall within LockedPeriod",
            locks.isLocked(site1));
        // Assert.assertTrue("No affected LockedPeriods should be available",
        // site1.getAffectedLockedPeriods(activity1).size() == 0);
    }

}