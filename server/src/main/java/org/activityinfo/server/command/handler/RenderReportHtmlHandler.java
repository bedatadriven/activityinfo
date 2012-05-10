/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.command.handler;

import java.io.IOException;
import java.io.StringWriter;

import javax.servlet.ServletContext;
import javax.xml.bind.JAXBException;

import org.activityinfo.server.database.hibernate.dao.ReportDefinitionDAO;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.server.report.ReportParserJaxb;
import org.activityinfo.server.report.generator.ReportGenerator;
import org.activityinfo.server.report.renderer.itext.HtmlReportRenderer;
import org.activityinfo.server.util.logging.LogException;
import org.activityinfo.shared.command.Filter;
import org.activityinfo.shared.command.RenderReportHtml;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.command.result.HtmlResult;
import org.activityinfo.shared.exception.CommandException;
import org.activityinfo.shared.report.model.DateRange;
import org.activityinfo.shared.report.model.Report;
import org.activityinfo.shared.report.model.ReportElement;

import com.google.inject.Inject;

/**
 * @author Alex Bertram
 * @see org.activityinfo.shared.command.RenderReportHtml
 */
public class RenderReportHtmlHandler implements CommandHandler<RenderReportHtml> {

    private final ReportGenerator generator;
    private final HtmlReportRenderer renderer;
   

    @Inject
    public RenderReportHtmlHandler(ReportGenerator generator,  HtmlReportRenderer renderer) {
        this.generator = generator;
        this.renderer = renderer;
    }

    @Override
	@LogException
    public CommandResult execute(RenderReportHtml cmd, User user) throws CommandException {

    	ReportElement model = cmd.getModel();
    	
    	//  don't show the title: it will be rendered by the container
    	model.setTitle(null);
    	
		generator.generateElement(user, model, new Filter(), new DateRange());
        StringWriter writer = new StringWriter();
        try {
			renderer.render(model, writer);
		} catch (IOException e) {
			throw new CommandException(e);
		}
        return new HtmlResult(writer.toString());
        
    }

}
