package org.sigmah.server.schedule;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;

import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
import org.sigmah.server.database.hibernate.entity.DomainFilters;
import org.sigmah.server.database.hibernate.entity.ReportDefinition;
import org.sigmah.server.database.hibernate.entity.ReportSubscription;
import org.sigmah.server.mail.MailSender;
import org.sigmah.server.report.ReportParserJaxb;
import org.sigmah.server.report.generator.ReportGenerator;
import org.sigmah.server.report.renderer.itext.RtfReportRenderer;
import org.sigmah.shared.report.model.DateRange;
import org.sigmah.shared.report.model.Report;
import org.sigmah.shared.report.model.ReportFrequency;
import org.xml.sax.SAXException;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

@Singleton
public class ServletReportMailer extends HttpServlet {

	private final Injector injector;
	private EntityManager em;
	private ReportGenerator reportGenerator;
	private RtfReportRenderer rtfReportRenderer;
	private MailSender mailer;

	private DateFormat reportDateFormat;
	
	@Inject
	public ServletReportMailer(Injector injector) {
		this.injector = injector;

		reportDateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
	}
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException {
		
		em = injector.getInstance(EntityManager.class);
		reportGenerator = injector.getInstance(ReportGenerator.class);
		rtfReportRenderer = injector.getInstance(RtfReportRenderer.class);
		mailer = injector.getInstance(MailSender.class);
		
		Date today = new Date();
		List<ReportDefinition> reports = em.createQuery(
				"select t from ReportDefinition t").getResultList();

		for (ReportDefinition template : reports) {
			try {
				Report report = ReportParserJaxb.parseXml(template.getXml());
				if (report.getFrequency() == ReportFrequency.Monthly) {
					if (ReportMailerHelper.mailToday(today, report)) {
						execute(today, report, template.getSubscriptions());
					}
				}
			} catch (Exception caught) {
				caught.printStackTrace();
			}
		}

	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException {
		doGet(request, response);
	}

	public void execute(Date today, Report report, Set<ReportSubscription> subs) {

		// calculate the date range for the report
		DateRange dateRange = ReportMailerHelper
				.computeDateRange(report, today);

		// loop through report subscriptions that are to be mailed
		// today
		for (ReportSubscription sub : subs) {

			try {
				mailReport(sub, today);
			} catch (Exception e) {
				System.out.println("Report mailing of "
						+ sub.getTemplate().getId() + " failed for user "
						+ sub.getUser().getEmail());
				e.printStackTrace();
			}
		}

	}

	private void mailReport(ReportSubscription sub, Date today)
			throws IOException, SAXException, EmailException {

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
		attachment.setName(report.getContent().getFileName()
				+ reportDateFormat.format(today) + ".rtf");
		attachment.setDescription(report.getTitle());
		attachment.setPath(tempFile.getAbsolutePath());
		attachment.setDisposition(EmailAttachment.ATTACHMENT);

		// compose both a full html rendering of this report and a short text
		// message for email clients that can't read html

		// email
		MultiPartEmail email = new MultiPartEmail();
		// email.setHtmlMsg(ReportMailerHelper.composeHtmlEmail(sub, report ));
		email.setMsg(ReportMailerHelper.composeTextEmail(sub, report));
		email.addTo(sub.getUser().getEmail(), sub.getUser().getName());
		email.setSubject("ActivityInfo: " + report.getTitle());
		email.attach(attachment);

		mailer.send(email);
	}
}
