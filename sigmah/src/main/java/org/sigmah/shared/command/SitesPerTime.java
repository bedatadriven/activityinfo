package org.sigmah.shared.command;

import java.io.Serializable;
import java.util.List;

import org.sigmah.shared.command.SitesPerTime.SitesPerTimeResult;
import org.sigmah.shared.command.result.CommandResult;

import com.google.common.collect.Lists;

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
		private List<YearResult> years = Lists.newArrayList();
		
		public SitesPerTimeResult() {
		}

		public SitesPerTimeResult(List<YearResult> data) {
			super();
			this.years = data;
		}

		public List<YearResult> getYears() {
			return years;
		}
		
		public static class YearResult implements Serializable {
			private List<MonthResult> months = Lists.newArrayList();
			private int year;

			public YearResult(int year) {
				this.year=year;
			}
			public YearResult() {
			}
			public int getYear() {
				return year;
			}
			public List<MonthResult> getMonths() {
				return months;
			}
			public void addMonth(MonthResult month) {
				months.add(month);
			}
		}
		public static class MonthResult implements Serializable {
			private int month;
			private int amountSites;
			
			public MonthResult() {
			}
			public MonthResult(int month, int amountSites) {
				super();
				this.month = month;
				this.amountSites = amountSites;
			}
			public int getMonth() {
				return month;
			}
			public int getAmountSites() {
				return amountSites;
			}
		}
	}
}
