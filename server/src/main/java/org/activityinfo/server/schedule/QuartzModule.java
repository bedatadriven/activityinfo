package org.activityinfo.server.schedule;

import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

import com.google.inject.servlet.ServletModule;

public class QuartzModule extends ServletModule {

	@Override
	protected void configureServlets() {
		bind(SchedulerFactory.class).to(StdSchedulerFactory.class);
		bind(Quartz.class).asEagerSingleton();
        serve("/tasks/mailSubscriptions").with(ReportMailerServlet.class);
        
	}

}
