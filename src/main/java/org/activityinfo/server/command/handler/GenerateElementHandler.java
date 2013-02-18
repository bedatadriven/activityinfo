/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.command.handler;

import java.util.logging.Logger;

import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.server.report.generator.ReportGenerator;
import org.activityinfo.shared.command.GenerateElement;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.exception.CommandException;
import org.activityinfo.shared.report.model.DateRange;

import com.google.inject.Inject;

/**
 * @author Alex Bertram
 */
public class GenerateElementHandler implements CommandHandler<GenerateElement> {

	private static final Logger LOGGER = Logger.getLogger(GenerateElementHandler.class.getName());
	
    private final ReportGenerator generator;

    @Inject
    public GenerateElementHandler(ReportGenerator generator) {
        this.generator = generator;
    }

    public CommandResult execute(GenerateElement cmd, User user) throws CommandException {

    	LOGGER.info("GenerateElement.element = " + cmd.getElement());
    	
        return generator.generateElement(user, cmd.getElement(), null, new DateRange());

    }
}
