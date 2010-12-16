package org.sigmah.server.endpoint.gwtrpc.handler;

import java.util.ArrayList;

import javax.persistence.EntityManager;

import org.dozer.Mapper;
import org.sigmah.shared.command.UpdateReminders;
import org.sigmah.shared.command.handler.CommandHandler;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.command.result.RemindersResultList;
import org.sigmah.shared.domain.User;
import org.sigmah.shared.domain.reminder.Reminder;
import org.sigmah.shared.dto.reminder.ReminderDTO;
import org.sigmah.shared.exception.CommandException;

import com.google.inject.Inject;

public class UpdateRemindersHandler implements CommandHandler<UpdateReminders> {

    private final EntityManager em;
    private final Mapper mapper;

    @Inject
    public UpdateRemindersHandler(EntityManager em, Mapper mapper) {
        this.em = em;
        this.mapper = mapper;
    }

    @Override
    public CommandResult execute(UpdateReminders cmd, User user) throws CommandException {

        final ArrayList<ReminderDTO> resultList = new ArrayList<ReminderDTO>();

        Reminder reminder;
        for (final ReminderDTO reminderDTO : cmd.getList()) {

            // Retrieves entity.
            reminder = em.find(Reminder.class, reminderDTO.getId());

            // Updates it.
            reminder.setCompletionDate(reminderDTO.getCompletionDate());
            reminder.setExpectedDate(reminderDTO.getExpectedDate());
            reminder.setLabel(reminderDTO.getLabel());

            // Saves it.
            reminder = em.merge(reminder);

            resultList.add(mapper.map(reminder, ReminderDTO.class));
        }

        return new RemindersResultList(resultList);
    }
}