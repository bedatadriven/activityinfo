package org.activityinfo.server.schedule;

import org.quartz.Job;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;

import com.google.inject.Inject;
import com.google.inject.Injector;

public class GuiceJobFactory implements JobFactory {

    private final Injector guice;
 
    @Inject
    public GuiceJobFactory(final Injector guice) {
        this.guice = guice;
    }
 
	@Override
	public Job newJob(TriggerFiredBundle bundle, Scheduler scheduler)
			throws SchedulerException {
        return guice.getInstance(bundle.getJobDetail().getJobClass());
	}
}
