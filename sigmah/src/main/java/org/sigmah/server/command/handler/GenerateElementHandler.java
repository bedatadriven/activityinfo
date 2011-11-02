/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.command.handler;

import org.sigmah.server.database.hibernate.entity.User;
import org.sigmah.server.report.generator.ReportGenerator;
import org.sigmah.shared.command.GenerateElement;
import org.sigmah.shared.command.handler.CommandHandler;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.exception.CommandException;
import org.sigmah.shared.report.model.DateRange;

import com.google.inject.Inject;

/**
 * @author Alex Bertram
 */
public class GenerateElementHandler implements CommandHandler<GenerateElement> {


    private final ReportGenerator generator;

    @Inject
    public GenerateElementHandler(ReportGenerator generator) {
        this.generator = generator;
    }

    public CommandResult execute(GenerateElement cmd, User user) throws CommandException {

        return generator.generateElement(user, cmd.getElement(), null, new DateRange());

    }
}
