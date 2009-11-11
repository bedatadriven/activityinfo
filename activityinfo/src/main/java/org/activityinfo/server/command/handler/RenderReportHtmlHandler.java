/*
 * This file is part of ActivityInfo.
 *
 * ActivityInfo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ActivityInfo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ActivityInfo.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2009 Alex Bertram and contributors.
 */

package org.activityinfo.server.command.handler;

import com.google.inject.Inject;
import org.activityinfo.server.dao.ReportDAO;
import org.activityinfo.server.domain.User;
import org.activityinfo.server.report.ReportParserJaxb;
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

import javax.servlet.ServletContext;
import javax.xml.bind.JAXBException;
import java.io.IOException;

/**
 * @see org.activityinfo.shared.command.RenderReportHtml
 *
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
            Report report = ReportParserJaxb.parseXml(xml);

            generator.generate(user, report, null, cmd.getDateRange());

            ServletImageStorageProvider isp = new ServletImageStorageProvider("temp/",
                    servletContext.getRealPath("/temp/"));

            HtmlWriter writer = new HtmlWriter();
            renderer.render(writer, isp, report);

            return new HtmlResult(writer.toString());

        } catch (JAXBException e) {
            e.printStackTrace();
            throw new UnexpectedCommandException();
        } catch (IOException e) {
            e.printStackTrace();
            throw new UnexpectedCommandException();
        }
    }
}
