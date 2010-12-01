package org.sigmah.server.endpoint.gwtrpc.handler;

import java.util.ArrayList;

import javax.persistence.EntityManager;

import org.dozer.Mapper;
import org.sigmah.shared.command.UpdateMonitoredPoints;
import org.sigmah.shared.command.handler.CommandHandler;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.command.result.MonitoredPointsResultList;
import org.sigmah.shared.domain.User;
import org.sigmah.shared.domain.reminder.MonitoredPoint;
import org.sigmah.shared.dto.reminder.MonitoredPointDTO;
import org.sigmah.shared.exception.CommandException;

import com.google.inject.Inject;

public class UpdateMonitoredPointsHandler implements CommandHandler<UpdateMonitoredPoints> {

    private final EntityManager em;
    private final Mapper mapper;

    @Inject
    public UpdateMonitoredPointsHandler(EntityManager em, Mapper mapper) {
        this.em = em;
        this.mapper = mapper;
    }

    @Override
    public CommandResult execute(UpdateMonitoredPoints cmd, User user) throws CommandException {

        final ArrayList<MonitoredPointDTO> resultList = new ArrayList<MonitoredPointDTO>();

        MonitoredPoint point;
        for (final MonitoredPointDTO pointDTO : cmd.getList()) {

            // Retrieves entity.
            point = em.find(MonitoredPoint.class, pointDTO.getId());

            // Updates it.
            point.setCompletionDate(pointDTO.getCompletionDate());
            point.setExpectedDate(pointDTO.getExpectedDate());
            point.setLabel(pointDTO.getLabel());

            // Saves it.
            point = em.merge(point);

            resultList.add(mapper.map(point, MonitoredPointDTO.class));
        }

        return new MonitoredPointsResultList(resultList);
    }
}
