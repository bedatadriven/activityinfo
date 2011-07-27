/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.schedule;

import java.util.Date;

import javax.servlet.ServletContext;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerUtils;
import org.quartz.spi.JobFactory;

import com.google.inject.Inject;

/*
 * @author Alex Bertram
 */
public class Jobs {

    private JobFactory jobFactory;
    private ServletContext context;

    @Inject
    public Jobs(ServletContext context, JobFactory jobFactory) {
        this.jobFactory = jobFactory;
        this.context = context;
    }

    public void schedule() {

        SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();

        try {
            Scheduler sched = schedFact.getScheduler();
            sched.setJobFactory(jobFactory);
            sched.start();

            JobDetail jobDetail = new JobDetail("reportMailerJob",
                    null,
                    ReportMailerJob.class);

            Trigger trigger = TriggerUtils.makeDailyTrigger(0, 0); // fire every day at midnight
            trigger.setStartTime(TriggerUtils.getEvenHourDate(new Date()));  // start on the next even hour
            trigger.setName("myTrigger");

            sched.scheduleJob(jobDetail, trigger);

            context.log("Scheduler initialized successfully");

        } catch (SchedulerException e) {
            context.log("Schedulder failed to initialize!", e);
        }
    }
}
