/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.command.handler;

import java.io.IOException;
import java.io.StringWriter;

import javax.servlet.ServletContext;
import javax.xml.bind.JAXBException;

import org.sigmah.server.database.hibernate.dao.ReportDefinitionDAO;
import org.sigmah.server.database.hibernate.entity.User;
import org.sigmah.server.report.ReportParserJaxb;
import org.sigmah.server.report.generator.ReportGenerator;
import org.sigmah.server.report.renderer.itext.HtmlReportRenderer;
import org.sigmah.server.util.logging.LogException;
import org.sigmah.shared.command.Filter;
import org.sigmah.shared.command.RenderReportHtml;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.command.result.HtmlResult;
import org.sigmah.shared.exception.CommandException;
import org.sigmah.shared.report.model.DateRange;
import org.sigmah.shared.report.model.Report;
import org.sigmah.shared.report.model.ReportElement;

import com.google.inject.Inject;

/**
 * @author Alex Bertram
 * @see org.sigmah.shared.command.RenderReportHtml
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
