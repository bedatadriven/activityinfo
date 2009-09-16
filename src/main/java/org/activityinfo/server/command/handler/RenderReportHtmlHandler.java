package org.activityinfo.server.command.handler;

import org.activityinfo.server.dao.ReportDAO;
import org.activityinfo.server.domain.User;
import org.activityinfo.server.report.ReportParser;
import org.activityinfo.server.report.ServletImageStorageProvider;
import org.activityinfo.server.report.generator.ReportGenerator;
import org.activityinfo.server.report.renderer.html.HtmlReportRenderer;
import org.activityinfo.server.report.util.HtmlWriter;
import org.activityinfo.shared.command.RenderReportHtml;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.command.result.HtmlResult;
import org.activityinfo.shared.exception.CommandException;
import org.activityinfo.shared.exception.UnexpectedCommandException;
import org.activityinfo.shared.report.model.Report;
import org.xml.sax.SAXException;
import com.google.inject.Inject;

import javax.servlet.ServletContext;
import java.io.IOException;
/*
 * @author Alex Bertram
 */

public class RenderReportHtmlHandler implements CommandHandler<RenderReportHtml> {

    private final ReportGenerator generator;
    private final ReportDAO reportDAO;
    private final HtmlReportRenderer renderer;
    private final ServletContext servletContext;

    @Inject
    public RenderReportHtmlHandler(ReportGenerator generator, ReportDAO reportDAO, HtmlReportRenderer renderer, ServletContext servletContext) {
        this.generator = generator;
        this.reportDAO = reportDAO;
        this.renderer = renderer;
        this.servletContext = servletContext;
    }

    public CommandResult execute(RenderReportHtml cmd, User user) throws CommandException {

        String xml = reportDAO.getXmlById(cmd.getTemplateId());

        try {
            Report report = ReportParser.parseXml(xml);

            generator.generate(user, report, null, cmd.getParameters().getTransientMap());

            ServletImageStorageProvider isp = new ServletImageStorageProvider("temp/",
                    servletContext.getRealPath("/temp/"));

            HtmlWriter writer = new HtmlWriter();
            renderer.render(writer, isp, report);

            return new HtmlResult(writer.toString());

        } catch (SAXException e) {
            e.printStackTrace();
            throw new UnexpectedCommandException();
        } catch (IOException e) {
            e.printStackTrace();
            throw new UnexpectedCommandException();
        }
    }
}
