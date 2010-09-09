/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.endpoint.gwtrpc.handler;

import com.google.inject.Inject;
import org.sigmah.server.dao.ReportDefinitionDAO;
import org.sigmah.server.report.ReportParserJaxb;
import org.sigmah.server.report.ServletImageStorageProvider;
import org.sigmah.server.report.generator.ReportGenerator;
import org.sigmah.server.report.renderer.html.HtmlReportRenderer;
import org.sigmah.server.report.util.HtmlWriter;
import org.sigmah.shared.command.RenderReportHtml;
import org.sigmah.shared.command.handler.CommandHandler;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.command.result.HtmlResult;
import org.sigmah.shared.domain.User;
import org.sigmah.shared.exception.CommandException;
import org.sigmah.shared.report.model.Report;

import javax.servlet.ServletContext;
import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;

/**
 * @author Alex Bertram
 * @see org.sigmah.shared.command.RenderReportHtml
 */
public class RenderReportHtmlHandler implements CommandHandler<RenderReportHtml> {

    private final ReportGenerator generator;
    private final ReportDefinitionDAO reportDAO;
    private final HtmlReportRenderer renderer;
    private final ServletContext servletContext;

    @Inject
    public RenderReportHtmlHandler(ReportGenerator generator, ReportDefinitionDAO reportDAO, HtmlReportRenderer renderer, ServletContext servletContext) {
        this.generator = generator;
        this.reportDAO = reportDAO;
        this.renderer = renderer;
        this.servletContext = servletContext;
    }

    public CommandResult execute(RenderReportHtml cmd, User user) throws CommandException {

        String xml = reportDAO.findById(cmd.getTemplateId()).getXml();

        try {
            Report report = ReportParserJaxb.parseXml(xml);

            generator.generate(user, report, null, cmd.getDateRange());

            HtmlWriter writer = new HtmlWriter();
            renderer.render(writer, createServletImageProvider(), report);

            return new HtmlResult(writer.toString());
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ServletImageStorageProvider createServletImageProvider() {
        File tempPath = new File(servletContext.getRealPath("/temp/"));
        if(!tempPath.exists()) {
            tempPath.mkdir();
        }
        ServletImageStorageProvider isp = new ServletImageStorageProvider("temp/",
                tempPath.getAbsolutePath());
        return isp;
    }
}
