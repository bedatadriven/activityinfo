package org.sigmah.shared.command;

import java.util.Map;

import org.sigmah.shared.command.SitesPerTime.SitesPerTimeResult;
import org.sigmah.shared.command.result.CommandResult;

import com.google.common.collect.Maps;

/** Request a list of years, in turn having a list of months with amount of sites for that month */
public class SitesPerTime implements Command<SitesPerTimeResult> {
	private int activityId;
	
	public SitesPerTime() {
	}

	public SitesPerTime(int activityId) {
		this.activityId = activityId;
	}

	public int getActivityId() {
		return activityId;
	}

	public static class SitesPerTimeResult implements CommandResult {
		//          Year		 Month	  Amount/month
		private Map<Integer, Map<Integer, Integer>> years = Maps.newHashMap(); 
		
		public SitesPerTimeResult() {
		}

		public SitesPerTimeResult(Map<Integer, Map<Integer, Integer>> data) {
			super();
			this.years = data;
		}

		public Map<Integer, Map<Integer, Integer>> getYears() {
			return years;
		}
	}
}
