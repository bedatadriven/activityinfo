/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.schedule;

import java.util.Date;

import org.activityinfo.server.database.hibernate.HibernateSessionScope;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.google.inject.Inject;
import com.google.inject.Provider;


/**
 * Quartz Job that is run nightly to mail reports to subscribers.
 */
public class ReportMailerJob implements Job {
	private HibernateSessionScope hibernateScope;
	private Provider<ReportMailer> mailer;
    

    @Inject
    public ReportMailerJob(HibernateSessionScope hibernateScope, 
	    				   Provider<ReportMailer> mailer) {
        this.hibernateScope = hibernateScope;
        this.mailer = mailer;
    }

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		
		hibernateScope.enter();
		try {
			mailer.get().execute(new Date());
		} finally {
			hibernateScope.exit();
		}
	}


    

}
