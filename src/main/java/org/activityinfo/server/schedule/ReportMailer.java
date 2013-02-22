package org.activityinfo.server.schedule;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.MessagingException;
import javax.persistence.EntityManager;

import org.activityinfo.server.authentication.ServerSideAuthProvider;
import org.activityinfo.server.database.hibernate.entity.DomainFilters;
import org.activityinfo.server.database.hibernate.entity.ReportSubscription;
import org.activityinfo.server.mail.MailSender;
import org.activityinfo.server.mail.MessageBuilder;
import org.activityinfo.server.report.ReportParserJaxb;
import org.activityinfo.server.report.generator.ReportGenerator;
import org.activityinfo.server.report.renderer.itext.RtfReportRenderer;
import org.activityinfo.shared.report.model.DateRange;
import org.activityinfo.shared.report.model.Report;
import org.xml.sax.SAXException;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.inject.Inject;

public class ReportMailer {

    private static final Logger LOGGER = Logger.getLogger(ReportMailer.class
        .getName());

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
        execute(today, Predicates.<ReportSubscription>alwaysTrue());
    }

    public void execute(Date today, Predicate<ReportSubscription> filter) {

        LOGGER.info("Starting nightly mailing job for " + today);

        List<ReportSubscription> subscriptions = em.createQuery(
            "select t from ReportSubscription t")
            .getResultList();

        for (ReportSubscription subscription : subscriptions) {
            try {
                if (ReportMailerHelper.mailToday(today, subscription)) {
                    Report report = ReportParserJaxb.parseXml(subscription
                        .getTemplate().getXml());
                    execute(today, subscription, report);
                }
            } catch (Exception caught) {
                LOGGER.log(
                    Level.SEVERE,
                    "Exception thrown while processing report "
                        + subscription.getId(), caught);
            }
        }
    }

    public void execute(Date today, ReportSubscription sub, Report report)
        throws IOException {

        // set up authentication for the subscriber of this report

        authProvider.set(sub.getUser());
        DomainFilters.applyUserFilter(sub.getUser(), em);

        // render the report to a temp file
        // generate the report
        reportGenerator.generate(sub.getUser(), report, null, new DateRange());

        ByteArrayOutputStream rtf = new ByteArrayOutputStream();
        rtfReportRenderer.render(report, rtf);
        rtf.close();

        try {
            mailReport(sub, report, today, rtf.toByteArray());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Report mailing of "
                + sub.getTemplate().getId() + " failed for user "
                + sub.getUser().getEmail(), e);
        }
    }

    private void mailReport(ReportSubscription sub, Report report, Date today,
        byte[] content)
        throws IOException, SAXException, MessagingException {

        LOGGER.log(Level.INFO, "Sending email to " + sub.getUser().getEmail());

        MessageBuilder email = new MessageBuilder();
        email.to(sub.getUser().getEmail(), sub.getUser().getName());
        email.subject("ActivityInfo: " + report.getTitle());
        email.addPart()
            .withText(ReportMailerHelper.composeTextEmail(sub, report));

        email.addPart()
            .withContent(content, "text/enriched")
            .withFileName(report.getContent().getFileName() + " " +
                reportDateFormat.format(today) + ".rtf");

        mailer.send(email.build());
    }
}
