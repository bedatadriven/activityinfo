package org.activityinfo.client.page.entry;

import java.util.Collection;
import java.util.Date;

import org.activityinfo.shared.dto.ActivityDTO;
import org.activityinfo.shared.dto.LockedPeriodDTO;
import org.activityinfo.shared.dto.ProjectDTO;
import org.activityinfo.shared.dto.SchemaDTO;
import org.activityinfo.shared.dto.SiteDTO;
import org.activityinfo.shared.dto.UserDatabaseDTO;

import com.bedatadriven.rebar.time.calendar.LocalDate;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;


public class LockedPeriodSet {
	

	private Multimap<Integer, LockedPeriodDTO> activityLocks = HashMultimap.create();
	private Multimap<Integer, LockedPeriodDTO> projectLocks = HashMultimap.create();

	public LockedPeriodSet(SchemaDTO schema) {
		for(UserDatabaseDTO db : schema.getDatabases()) {
			indexLocks(db);
		}
	}


	public LockedPeriodSet(ActivityDTO activity) {
		indexLocks(activity.getDatabase());
	}
	
	private void indexLocks(UserDatabaseDTO db) {
		for(ActivityDTO activity : db.getActivities()) {
			addEnabled(activityLocks, activity.getId(),  db.getLockedPeriods());
			addEnabled(activityLocks, activity.getId(),  activity.getLockedPeriods());
		}
		for(ProjectDTO project : db.getProjects()) {
			addEnabled(projectLocks, project.getId(), project.getLockedPeriods());
		}
	}
	
	private void addEnabled(Multimap<Integer,LockedPeriodDTO> map, int key, Iterable<LockedPeriodDTO> periods) {
		for(LockedPeriodDTO lock : periods) {
			if(lock.isEnabled()) {
				map.put(key, lock);
			}
		}
	}
	
	public boolean isLocked(SiteDTO site) {
		if(isActivityLocked(site.getActivityId(), site.getDate2())) {
			return true;
		}
		if(site.getProject() != null) {
			
			if(dateRangeLocked(site.getDate2(), projectLocks.get(site.getProject().getId()))) {
				return true;
			}
		}
		return false;
	}

	public boolean isActivityLocked(int activityId, LocalDate date) {
		return dateRangeLocked(date, activityLocks.get(activityId));
	}

	public boolean isActivityLocked(int activityId, Date date) {
		return isActivityLocked(activityId, new LocalDate(date));
	}

	public boolean isProjectLocked(int projectId, LocalDate date) {
		return dateRangeLocked(date, projectLocks.get(projectId));
	}
	
	public boolean isProjectLocked(int projectId, Date date) {
		return isProjectLocked(projectId, new LocalDate(date));
	}

	private boolean dateRangeLocked(LocalDate date,
			Collection<LockedPeriodDTO> locks) {
	
		for(LockedPeriodDTO lock : locks) {
			if(lock.fallsWithinPeriod(date)) {
				return true;
			}
		}
		return false;
	}
}
