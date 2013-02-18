package org.activityinfo.server.schedule;

import java.util.Calendar;
import java.util.Date;

import javax.annotation.Nullable;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activityinfo.server.database.hibernate.entity.ReportSubscription;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

@Singleton
public class ReportMailerServlet extends HttpServlet {

	private Provider<ReportMailer> mailerJob;

	@Inject
	public ReportMailerServlet(Provider<ReportMailer> mailerJob) {
		super();
		this.mailerJob = mailerJob;
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException {

		Date today = parseDate(request);
		Predicate<ReportSubscription> filter = parseFilter(request);
	
		mailerJob.get().execute(today, filter);
	}
	
	private Predicate<ReportSubscription> parseFilter(HttpServletRequest req) {
		// for testing purposes, check for an id parameter that will only dispatch
		// a single report
		if(Strings.isNullOrEmpty(req.getParameter("reportId"))) {
			return Predicates.alwaysTrue();
		} 
		
		final int reportId = Integer.parseInt(req.getParameter("reportId"));
		
		return new Predicate<ReportSubscription>() {

			@Override
			public boolean apply(@Nullable ReportSubscription input) {
				return input != null && input.getId().getReportId() == reportId;
			}
		};
	}

	private Date parseDate(HttpServletRequest req) {
		try {
			if(Strings.isNullOrEmpty(req.getParameter("year"))) {
				return new Date();
			}
			int year = Integer.parseInt(req.getParameter("year"));
			int month = Integer.parseInt(req.getParameter("month"));
			int day = Integer.parseInt(req.getParameter("day"));
			
			Calendar date = Calendar.getInstance();
			date.set(Calendar.YEAR, year);
			date.set(Calendar.MONTH, month-1);
			date.set(Calendar.DATE, day);
			return date.getTime();
			
		} catch(NumberFormatException e) {
			return new Date();
		}
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException {
		doGet(request, response);
	}
}
