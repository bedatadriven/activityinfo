package org.activityinfo.server.schedule;

import org.activityinfo.server.DeploymentEnvironment;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

import com.google.inject.servlet.ServletModule;

public class QuartzModule extends ServletModule {

	@Override
	protected void configureServlets() {
        serve("/tasks/mailSubscriptions").with(ReportMailerServlet.class);

        if(!DeploymentEnvironment.isAppEngine()) {
	        bind(SchedulerFactory.class).to(StdSchedulerFactory.class);
			bind(Quartz.class).asEagerSingleton();
	        filter("/*").through(QuartzFilter.class);
        }
	}
}
