package org.activityinfo.server.schedule;

import com.google.inject.Inject;
import org.activityinfo.server.domain.DomainFilters;
import org.activityinfo.server.domain.ReportDefinition;
import org.activityinfo.server.domain.ReportSubscription;
import org.activityinfo.server.mail.Mailer;
import org.activityinfo.server.report.ReportParserJaxb;
import org.activityinfo.server.report.generator.ReportGenerator;
import org.activityinfo.server.report.renderer.itext.RtfReportRenderer;
import org.activityinfo.shared.report.model.DateRange;
import org.activityinfo.shared.report.model.Report;
import org.activityinfo.shared.report.model.ReportFrequency;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.xml.sax.SAXException;

import javax.persistence.EntityManager;
import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;


/**
 *
 * Quartz Job that is run nightly to mail reports to subscribers.
 *
 * @author Alex Bertram
 */
public class ReportMailerJob implements Job {

    private final EntityManager em;
    private final ReportGenerator reportGenerator;
    private final RtfReportRenderer rtfReportRenderer;
    private final Mailer mailer;

    private DateFormat reportDateFormat;

    @Inject
    public ReportMailerJob(EntityManager em, ReportGenerator reportGenerator,
                           RtfReportRenderer rtfReportRenderer, Mailer mailer) {
        this.em = em;
        this.reportGenerator = reportGenerator;
        this.rtfReportRenderer = rtfReportRenderer;
        this.mailer = mailer;

        reportDateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
    }


    public void execute(JobExecutionContext jobContext) throws JobExecutionException {

        Date today = new Date();
        List<ReportDefinition> reports = em.createQuery("select t from ReportDefinition t")
                                            .getResultList();

        for(ReportDefinition template : reports) {
            try {
                Report report = ReportParserJaxb.parseXml(template.getXml());
                if(report.getFrequency() == ReportFrequency.Monthly) {
                    if(ReportMailerHelper.mailToday(today, report)) {
                        execute(today, report, template.getSubscriptions());
                    }
                }
            } catch(Throwable caught) {
                caught.printStackTrace();
            }
        }
    }

    public void execute(Date today, Report report, Set<ReportSubscription> subs) {

        // calculate the date range for the report
        DateRange dateRange = ReportMailerHelper.computeDateRange(report, today);

        // loop through report subscriptions that are to be mailed
        // today
       for(ReportSubscription sub : subs) {

            try {
                mailReport(sub, today);
            } catch(Exception e) {
                System.out.println("Report mailing of " + sub.getTemplate().getId() + " failed for user "
                        + sub.getUser().getEmail());
                e.printStackTrace();
            }
        }

    }

    private void mailReport(ReportSubscription sub, Date today) throws IOException, SAXException, EmailException {

        // apply the appropriate filters so the user
        // only views the reports they're meant to
        DomainFilters.applyUserFilter(sub.getUser(), em);

        // load the report definition
        Report report = null;
        try {
            report = ReportParserJaxb.parseXml(sub.getTemplate().getXml());
        } catch (JAXBException e) {
            e.printStackTrace();
            return;
        }

        // generate the report
        reportGenerator.generate(sub.getUser(), report, null,
               ReportMailerHelper.computeDateRange(report, today));

        // render the report to a temporary path and create the
        // attachement

        File tempFile = File.createTempFile("report", ".rtf");
        FileOutputStream rtf = new FileOutputStream(tempFile);
        rtfReportRenderer.render(report, rtf);
        rtf.close();

        EmailAttachment attachment = new EmailAttachment();
        attachment.setName(report.getContent().getFileName() +
                reportDateFormat.format(today) + ".rtf");
        attachment.setDescription(report.getTitle());
        attachment.setPath(tempFile.getAbsolutePath());
        attachment.setDisposition(EmailAttachment.ATTACHMENT);

        // compose both a full html rendering of this report and a short text
        // message for email clients that can't read html

        // email
        MultiPartEmail email = new MultiPartEmail();
       // email.setHtmlMsg(ReportMailerHelper.composeHtmlEmail(sub, report ));
        email.setMsg(ReportMailerHelper.composeTextEmail(sub, report ));
        email.addTo(sub.getUser().getEmail(), sub.getUser().getName());
        email.setSubject("ActivityInfo: " + report.getTitle());
        email.attach(attachment);

        mailer.send(email);
    }
}
