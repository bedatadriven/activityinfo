/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.command.handler;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

import javax.servlet.ServletContext;
import javax.xml.bind.JAXBException;

import org.sigmah.server.database.hibernate.dao.ReportDefinitionDAO;
import org.sigmah.server.database.hibernate.entity.User;
import org.sigmah.server.report.ReportParserJaxb;
import org.sigmah.server.report.ServletImageStorageProvider;
import org.sigmah.server.report.generator.ReportGenerator;
import org.sigmah.server.report.renderer.itext.HtmlReportRenderer;
import org.sigmah.server.util.logging.LogException;
import org.sigmah.shared.command.RenderReportHtml;
import org.sigmah.shared.command.handler.CommandHandler;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.command.result.HtmlResult;
import org.sigmah.shared.exception.CommandException;
import org.sigmah.shared.report.model.Report;

import com.google.inject.Inject;

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

    @LogException
    public CommandResult execute(RenderReportHtml cmd, User user) throws CommandException {

        String xml = reportDAO.findById(cmd.getTemplateId()).getXml();

        try {
            Report report = ReportParserJaxb.parseXml(xml);

            generator.generate(user, report, null, cmd.getDateRange());

            StringWriter writer = new StringWriter();
            renderer.render(report, writer);

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
                tempPath.getAbsolutePath(), null);
        return isp;
    }
}
