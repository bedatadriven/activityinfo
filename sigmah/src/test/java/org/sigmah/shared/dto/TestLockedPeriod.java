package org.sigmah.shared.dto;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

public class TestLockedPeriod {
	
	@Test
	public void rangeTest() {
		LockedPeriodDTO lockedPeriod = new LockedPeriodDTO();
		lockedPeriod.setFromDate(new Date(2000, 1,1));
		lockedPeriod.setToDate(new Date(2000,1,2));
		lockedPeriod.setEnabled(true);
		
		SiteDTO site = createSite();
		
		ActivityDTO activity = new ActivityDTO(1, "woei");
		activity.getLockedPeriods().add(lockedPeriod);

		Assert.assertTrue("Site should fall within LockedPeriod", site.fallsWithinLockedPeriod(activity));
		
		
		LockedPeriodDTO lockedPeriod1 = new LockedPeriodDTO();
		lockedPeriod1.setFromDate(new Date(2000, 1,1));
		lockedPeriod1.setToDate(new Date(2000,1,2));
		lockedPeriod1.setEnabled(true);
		
		SiteDTO site1 = new SiteDTO(2);
		site1.setDate1(new Date(2000,1,1));
		site1.setDate2(new Date(2000,1,3));
		
		ActivityDTO activity1 = new ActivityDTO(2, "woei");
		activity1.getLockedPeriods().add(lockedPeriod1);

		Assert.assertFalse("Site should NOT fall within LockedPeriod", site1.fallsWithinLockedPeriod(activity1));
		Assert.assertTrue("No affected LockedPeriods should be available", site1.getAffectedLockedPeriods(activity1).size() == 0);
	}

	private SiteDTO createSite() {
		SiteDTO site = new SiteDTO(1);
		site.setDate1(new Date(2000,1,1));
		site.setDate2(new Date(2000,1,2));
		return site;
	}
	
}