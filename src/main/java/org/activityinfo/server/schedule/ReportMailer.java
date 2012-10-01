package org.activityinfo.server.schedule;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;

import org.activityinfo.server.authentication.ServerSideAuthProvider;
import org.activityinfo.server.database.hibernate.entity.DomainFilters;
import org.activityinfo.server.database.hibernate.entity.ReportSubscription;
import org.activityinfo.server.mail.MailSender;
import org.activityinfo.server.report.ReportParserJaxb;
import org.activityinfo.server.report.generator.ReportGenerator;
import org.activityinfo.server.report.renderer.itext.RtfReportRenderer;
import org.activityinfo.shared.auth.AuthenticatedUser;
import org.activityinfo.shared.report.model.DateRange;
import org.activityinfo.shared.report.model.Report;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
import org.xml.sax.SAXException;

import com.google.inject.Inject;

public class ReportMailer {
	
    private static final Logger LOGGER = Logger.getLogger(ReportMailerJob.class.getName());

	private final EntityManager em;
	private final ReportGenerator reportGenerator;
	private final RtfReportRenderer rtfReportRenderer;
	private final MailSender mailer;

	private final ServerSideAuthProvider authProvider;

	private DateFormat reportDateFormat;

	@Inject
	public ReportMailer(EntityManager em, ReportGenerator reportGenerator,
			RtfReportRenderer rtfReportRenderer, MailSender mailer,
			ServerSideAuthProvider authProvider) {
		super();
		this.em = em;
		this.reportGenerator = reportGenerator;
		this.rtfReportRenderer = rtfReportRenderer;
		this.mailer = mailer;
		this.authProvider = authProvider;

		reportDateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);

	}
	public void execute(Date today) {

		LOGGER.info("Starting nightly mailing job for " + today);

		List<ReportSubscription> subscriptions = em.createQuery("select t from ReportSubscription t")
				.getResultList();

		for (ReportSubscription subscription : subscriptions) {
			try {
				if (ReportMailerHelper.mailToday(today, subscription)) {
					Report report = ReportParserJaxb.parseXml(subscription.getTemplate().getXml());
					execute(today, subscription, report);
				}
			} catch (Exception caught) {
				LOGGER.log(Level.SEVERE, "Exception thrown while processing report " + subscription.getId(), caught); 
			}
		}
	}

	public void execute(Date today, ReportSubscription sub, Report report) throws IOException {

		// set up authentication for the subscriber of this report

		authProvider.set(new AuthenticatedUser("", sub.getUser().getId(), sub.getUser().getEmail()));
		DomainFilters.applyUserFilter(sub.getUser(), em);

		// render the report to a temp file
		// generate the report
		reportGenerator.generate(sub.getUser(), report, null, new DateRange());


		File tempFile = File.createTempFile("report", ".rtf");
		FileOutputStream rtf = new FileOutputStream(tempFile);
		rtfReportRenderer.render(report, rtf);
		rtf.close();

		// loop through report subscriptions that are to be mailed
		// today
		try {
			mailReport(sub, report, today, tempFile);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Report mailing of " + sub.getTemplate().getId() + " failed for user "
					+ sub.getUser().getEmail(), e);
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

		LOGGER.log(Level.INFO, "Sending email to " + sub.getUser().getEmail());

		MultiPartEmail email = new MultiPartEmail();
		// email.setHtmlMsg(ReportMailerHelper.composeHtmlEmail(sub, report ));
		email.setMsg(ReportMailerHelper.composeTextEmail(sub, report));
		email.addTo(sub.getUser().getEmail(), sub.getUser().getName());
		email.setSubject("ActivityInfo: " + report.getTitle());
		email.attach(attachment);

		mailer.send(email);
	}
}
