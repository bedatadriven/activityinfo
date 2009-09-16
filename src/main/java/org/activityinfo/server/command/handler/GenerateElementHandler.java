package org.activityinfo.server.command.handler;

import org.activityinfo.server.domain.User;
import org.activityinfo.server.report.generator.ReportGenerator;
import org.activityinfo.shared.command.GenerateElement;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.exception.CommandException;

import com.google.inject.Inject;

import java.util.HashMap;
/*
 * @author Alex Bertram
 */

public class GenerateElementHandler implements CommandHandler<GenerateElement> {


    private final ReportGenerator generator;

    @Inject
    public GenerateElementHandler(ReportGenerator generator) {
        this.generator = generator;
    }

    public CommandResult execute(GenerateElement cmd, User user) throws CommandException {

        HashMap<String, Object> params = new HashMap<String, Object>();

        return generator.generateElement(user, cmd.getElement(), null, params);
       
    }
}
