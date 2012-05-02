package org.sigmah.server.schedule;

import com.google.inject.servlet.ServletModule;

public class QuartzModule extends ServletModule {

	@Override
	protected void configureServlets() {
		bind(Quartz.class).asEagerSingleton();
        serve("/tasks/mailSubscriptions").with(ReportMailerServlet.class);
	}

}
