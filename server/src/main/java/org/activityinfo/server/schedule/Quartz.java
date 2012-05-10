package org.activityinfo.server.schedule;

import static org.quartz.CronScheduleBuilder.dailyAtHourAndMinute;
import static org.quartz.TriggerBuilder.newTrigger;
import static org.quartz.TriggerKey.triggerKey;

import org.apache.log4j.Logger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;

import com.google.inject.Inject;

public class Quartz {

	private static final Logger LOGGER = Logger.getLogger(Quartz.class.getName());
	
	private final Scheduler scheduler;
	
	@Inject
    public Quartz(final SchedulerFactory factory, final GuiceJobFactory jobFactory) throws SchedulerException
    {
        scheduler = factory.getScheduler();
        scheduler.setJobFactory(jobFactory);
        scheduleJobs();
        scheduler.start();
    }

	private void scheduleJobs() throws SchedulerException {
        
		JobDetail job = JobBuilder.newJob(ReportMailerJob.class)
			.withIdentity("reportMailer")
			.build();
		
		Trigger trigger = newTrigger()
			.withIdentity(triggerKey("nightly", "nightlyGroup"))
			.withSchedule(dailyAtHourAndMinute(0, 30))
			.startNow()
			.build();
		
		scheduler.scheduleJob(job, trigger);
        
	}
}
