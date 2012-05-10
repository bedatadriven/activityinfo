package org.activityinfo.server.util.date;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.Calendar;
import java.util.Date;

import org.activityinfo.server.util.date.DateUtilCalendarImpl;
import org.activityinfo.shared.report.model.DateRange;
import org.activityinfo.shared.report.model.DateUnit;
import org.junit.Test;

public class DateUtilCalendarImplTest {

	private DateUtilCalendarImpl util = new DateUtilCalendarImpl();
	
	@Test
	public void floorMonth() {
		Calendar fifth = Calendar.getInstance();
		fifth.set(2011, Calendar.MAY, 5);
		
		Date date = util.floor(fifth.getTime(), DateUnit.MONTH);
		
		Calendar floored = Calendar.getInstance();
		floored.setTime(date);
		
		assertThat("year", floored.get(Calendar.YEAR), equalTo(2011));
		assertThat("month", floored.get(Calendar.MONTH), equalTo(Calendar.MAY));
		assertThat("day", floored.get(Calendar.DATE), equalTo(1));
	}
	
	@Test
	public void lastCompleteMonth() {
		Calendar fifth = Calendar.getInstance();
		fifth.set(2011, Calendar.MAY, 5);
		
		DateRange range = util.lastCompleteMonthRange(fifth.getTime());
		assertThat(range.getMinDate().getDate(), equalTo(1));
		assertThat(range.getMaxDate().getDate(), equalTo(30));
	}
	
}
