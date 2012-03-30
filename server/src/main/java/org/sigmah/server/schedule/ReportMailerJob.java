/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.schedule;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.log4j.Logger;
import org.sigmah.server.authentication.ServerSideAuthProvider;
import org.sigmah.server.database.hibernate.entity.DomainFilters;
import org.sigmah.server.database.hibernate.entity.ReportDefinition;
import org.sigmah.server.database.hibernate.entity.ReportSubscription;
import org.sigmah.server.mail.MailSender;
import org.sigmah.server.report.ReportParserJaxb;
import org.sigmah.server.report.generator.ReportGenerator;
import org.sigmah.server.report.renderer.itext.RtfReportRenderer;
import org.sigmah.shared.auth.AuthenticatedUser;
import org.sigmah.shared.report.model.Report;
import org.sigmah.shared.report.model.EmailDelivery;
import org.xml.sax.SAXException;

import com.google.inject.Inject;


/**
 * Quartz Job that is run nightly to mail reports to subscribers.
 *
 * @author Alex Bertram
 */
public class ReportMailerJob {

    private final EntityManager em;
    private final ReportGenerator reportGenerator;
    private final RtfReportRenderer rtfReportRenderer;
    private final MailSender mailer;
    
    private final ServerSideAuthProvider authProvider;

    private DateFormat reportDateFormat;
    
    private static final Logger LOGGER = Logger.getLogger(ReportMailerJob.class);

    @Inject
    public ReportMailerJob(EntityManager em, ReportGenerator reportGenerator,
                           RtfReportRenderer rtfReportRenderer, MailSender mailer, ServerSideAuthProvider authProvider) {
        this.em = em;
        this.reportGenerator = reportGenerator;
        this.rtfReportRenderer = rtfReportRenderer;
        this.mailer = mailer;
        this.authProvider = authProvider;

        reportDateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
    }


    public void execute(Date today) {
    	
    	LOGGER.info("Starting nightly mailing job for " + today);

        List<ReportDefinition> reports = em.createQuery("select t from ReportDefinition t")
                .getResultList();

        for (ReportDefinition template : reports) {
            try {
                Report report = ReportParserJaxb.parseXml(template.getXml());
                if (report.getFrequency() == EmailDelivery.MONTHLY) {
                    if (ReportMailerHelper.mailToday(today, report)) {
                        execute(today, template, report);
                    }
                }
            } catch (Exception caught) {
            	LOGGER.error("Exception thrown while processing report " + template.getId()); 
            }
        }
    }

    public void execute(Date today, ReportDefinition def, Report report) throws IOException {

    	// set up authentication for the owner of this report
    	// this allows a user to share reports even with those who 
    	// might not have access to the whole database
    	
    	authProvider.set(new AuthenticatedUser("", def.getOwner().getId(), def.getOwner().getEmail()));
        DomainFilters.applyUserFilter(def.getOwner(), em);

        // render the report to a temp file
        // generate the report
        reportGenerator.generate(def.getOwner(), report, null,
                ReportMailerHelper.computeDateRange(report, today));


        File tempFile = File.createTempFile("report", ".rtf");
        FileOutputStream rtf = new FileOutputStream(tempFile);
        rtfReportRenderer.render(report, rtf);
        rtf.close();

        // loop through report subscriptions that are to be mailed
        // today
        for (ReportSubscription sub : def.getSubscriptions()) {
            try {
                mailReport(sub, report, today, tempFile);
            } catch (Exception e) {
            	LOGGER.error("Report mailing of " + sub.getTemplate().getId() + " failed for user "
                        + sub.getUser().getEmail(), e);
            }
        }

    }

    private void mailReport(ReportSubscription sub,  Report report, Date today, File tempFile) throws IOException, SAXException, EmailException {

    
        EmailAttachment attachment = new EmailAttachment();
        attachment.setName(report.getContent().getFileName() + " " +
                reportDateFormat.format(today) + ".rtf");
        attachment.setDescription(report.getTitle());
        attachment.setPath(tempFile.getAbsolutePath());
        attachment.setDisposition(EmailAttachment.ATTACHMENT);

        // compose both a full html rendering of this report and a short text
        // message for email clients that can't read html

        // email
        
        LOGGER.debug("Sending email to " + sub.getUser().getEmail());
        
        MultiPartEmail email = new MultiPartEmail();
        // email.setHtmlMsg(ReportMailerHelper.composeHtmlEmail(sub, report ));
        email.setMsg(ReportMailerHelper.composeTextEmail(sub, report));
        email.addTo(sub.getUser().getEmail(), sub.getUser().getName());
        email.setSubject("ActivityInfo: " + report.getTitle());
        email.attach(attachment);

        mailer.send(email);
    }
}
