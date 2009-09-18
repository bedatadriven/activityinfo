package org.activityinfo.server.schedule;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.activityinfo.server.domain.ReportSubscription;
import org.activityinfo.server.domain.DomainFilters;
import org.activityinfo.server.domain.util.EntropicToken;
import org.activityinfo.server.report.generator.ReportGenerator;
import org.activityinfo.server.report.renderer.html.HtmlReportRenderer;
import org.activityinfo.server.report.renderer.itext.RtfReportRenderer;
import org.activityinfo.server.report.ReportParser;
import org.activityinfo.server.report.ServletImageStorageProvider;
import org.activityinfo.server.report.util.HtmlWriter;
import org.activityinfo.server.mail.Mailer;
import org.activityinfo.shared.report.model.Report;
import org.activityinfo.shared.domain.Subscription;
import org.activityinfo.client.Application;
import org.xml.sax.SAXException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.EmailException;

import javax.persistence.EntityManager;
import javax.servlet.ServletContext;
import java.util.*;
import java.text.MessageFormat;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.google.inject.Inject;
/*
 * @author Alex Bertram
 */

public class ReportMailerJob implements Job {

    private final ServletContext context;
    private final EntityManager em;
    private final ReportGenerator reportGenerator;
    private final HtmlReportRenderer htmlReportRenderer;
    private final RtfReportRenderer rtfReportRenderer;
    private final Mailer mailer;

    @Inject
    public ReportMailerJob(EntityManager em, ReportGenerator reportGenerator, HtmlReportRenderer htmlReportRenderer, ServletContext context, RtfReportRenderer rtfReportRenderer, Mailer mailer) {
        this.em = em;
        this.reportGenerator = reportGenerator;
        this.htmlReportRenderer = htmlReportRenderer;
        this.context = context;
        this.rtfReportRenderer = rtfReportRenderer;
        this.mailer = mailer;
    }


    public void execute(JobExecutionContext jobContext) throws JobExecutionException {

        List<ReportSubscription> subs = em.createQuery("select s from ReportSubscription s where s.frequency <> 0")
                                            .getResultList();

        execute(new Date(), subs);
    }

    public void execute(Date today, List<ReportSubscription> subs) {

        // loop through report subscriptions that are to be mailed
        // today


        for(ReportSubscription sub : subs) {
            if(ReportMailerHelper.mailToday(today, sub)) {

                try {
                    mailReport(sub, today);
                } catch(Exception e) {
                    context.log("Report mailing of " + sub.getTemplate().getId() + " failed for user " + sub.getUser().getEmail(), e);
                }
            }
        }

    }

    private String frequencyString(ResourceBundle messages, int frequency) {
        if(frequency == Subscription.DAILY) {
            return messages.getString("daily");
        } else if(frequency == Subscription.WEEKLY) {
            return messages.getString("weekly");
        } else if(frequency == Subscription.MONTHLY) {
            return messages.getString("monthly");
        } else {
            throw new RuntimeException("Invalid frequency = " + frequency);
        }

    }

    private void mailReport(ReportSubscription sub, Date today) throws IOException, SAXException, EmailException {

        // apply the appropriate filters so the user
        // only views the reports they're meant to
        DomainFilters.applyUserFilter(sub.getUser(), em);

        // load the report definition
        Report report = ReportParser.parseXml(sub.getTemplate().getXml());

        // set the report date parameters in function of the
        // date today / subscription frequency


        // generate the report
        reportGenerator.generate(sub.getUser(), report, null,
                ReportMailerHelper.computeDateParameters(report, sub, today));

        String tempPath = context.getRealPath("/temp/");

        // render the report first to an rtf document that the user can
        // download

        // TODO: friendly URLs and stored outside the servlet context
        // (so when someone checks their email 3 months after its been sent
        //  and the context has been replaced with a new version the link is still valid)
        String rtfName = EntropicToken.generate() + ".rtf";
        String rtfFile = tempPath + "/" + rtfName;
        String rtfUrl = "http://www.activityinfo.org/temp/" + rtfName;
        FileOutputStream rtf = new FileOutputStream(rtfFile);
        rtfReportRenderer.render(report, rtf);
        rtf.close();

        // prepare an image storage provider with absolute urls
        // TODO: how do we get the server's name?
        // TODO: to embed or not to embed?
        ServletImageStorageProvider isp = new ServletImageStorageProvider(
                "http://www.activityinfo.org/temp/",
                tempPath);

        // load our resource bundle with localized messages
        ResourceBundle mailMessages =
              ResourceBundle.getBundle("org.activityinfo.server.schedule.MailMessages", sub.getUser().getLocaleObject());

        // compose both a full html rendering of this report and a short text
        // message for email clients that can't read html

        StringBuilder textWriter = new StringBuilder();
        HtmlWriter htmlWriter = new HtmlWriter();

        htmlWriter.startDocument();
        htmlWriter.startDocumentBody();
        String greeting = MessageFormat.format(mailMessages.getString("greeting"), sub.getUser().getName());
        htmlWriter.paragraph(greeting);
        textWriter.append(greeting).append("\n\n");

        String intro;
        if(sub.getInvitingUser() != null) {
            intro = MessageFormat.format(mailMessages.getString("reportIntroInvited"),
                    sub.getInvitingUser().getName(),
                    sub.getInvitingUser().getEmail(),
                    report.getTitle(),
                    frequencyString(mailMessages, sub.getFrequency()));
        } else {
            intro = MessageFormat.format(mailMessages.getString("reportIntro"),
                    report.getTitle(),
                    frequencyString(mailMessages, sub.getFrequency()));

        }
        htmlWriter.paragraph(intro);
        textWriter.append(intro).append("\n\n");

        htmlWriter.paragraph(MessageFormat.format(mailMessages.getString("reportDownloadHtml"),
                rtfUrl));
        textWriter.append(MessageFormat.format(mailMessages.getString("reportDownloadText"),
                rtfUrl));

        htmlWriter.text("<hr>");
        htmlReportRenderer.render(htmlWriter, isp, report);

        htmlWriter.endDocumentBody();
        htmlWriter.endDocument();

        // email
        HtmlEmail email = new HtmlEmail();
        email.setHtmlMsg(htmlWriter.toString());
        email.setTextMsg(textWriter.toString());
        email.addTo(sub.getUser().getEmail(), sub.getUser().getName());
        email.setSubject("ActivityInfo: " + report.getTitle());
        
        mailer.send(email);
    }
}
