package org.activityinfo.shared.dto;

import org.activityinfo.client.page.entry.LockedPeriodSet;
import org.junit.Assert;
import org.junit.Test;

import com.bedatadriven.rebar.time.calendar.LocalDate;

public class LockedPeriodTest {
	
	@Test
	public void rangeTest() {
		UserDatabaseDTO db = new UserDatabaseDTO();
		
		LockedPeriodDTO lockedPeriod = new LockedPeriodDTO();
		lockedPeriod.setFromDate(new LocalDate(2000, 1,1));
		lockedPeriod.setToDate(new LocalDate(2000,1,2));
		lockedPeriod.setEnabled(true);
		
		SiteDTO site = createSite();
		site.setActivityId(1);
		
		ActivityDTO activity = new ActivityDTO(1, "woei");
		activity.setDatabase(db);
		activity.getLockedPeriods().add(lockedPeriod);

		Assert.assertTrue("Site should fall within LockedPeriod", new LockedPeriodSet(activity).isLocked(site));
		
		
		LockedPeriodDTO lockedPeriod1 = new LockedPeriodDTO();
		lockedPeriod1.setFromDate(new LocalDate(2000, 1,1));
		lockedPeriod1.setToDate(new LocalDate(2000,1,2));
		lockedPeriod1.setEnabled(true);
		
		SiteDTO site1 = new SiteDTO(2);
		site1.setActivityId(2);
		site1.setDate1(new LocalDate(2000,1,1));
		site1.setDate2(new LocalDate(2000,1,3));
		
		ActivityDTO activity1 = new ActivityDTO(2, "woei");
		activity1.setDatabase(db);
		activity1.getLockedPeriods().add(lockedPeriod1);

		LockedPeriodSet locks = new LockedPeriodSet(activity1);
		
		Assert.assertFalse("Site should NOT fall within LockedPeriod", locks.isLocked(site1));
		//Assert.assertTrue("No affected LockedPeriods should be available", site1.getAffectedLockedPeriods(activity1).size() == 0);
	}

	private SiteDTO createSite() {
		SiteDTO site = new SiteDTO(1);
		site.setDate1(new LocalDate(2000,1,1));
		site.setDate2(new LocalDate(2000,1,2));
		return site;
	}
	
}