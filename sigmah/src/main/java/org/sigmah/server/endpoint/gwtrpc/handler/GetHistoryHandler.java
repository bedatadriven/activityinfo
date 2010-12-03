package org.sigmah.server.endpoint.gwtrpc.handler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.dozer.Mapper;
import org.sigmah.shared.command.GetHistory;
import org.sigmah.shared.command.handler.CommandHandler;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.command.result.HistoryResult;
import org.sigmah.shared.domain.User;
import org.sigmah.shared.domain.history.HistoryToken;
import org.sigmah.shared.dto.history.HistoryTokenDTO;
import org.sigmah.shared.exception.CommandException;

import com.google.inject.Inject;

public class GetHistoryHandler implements CommandHandler<GetHistory> {

    private final Mapper mapper;
    private final EntityManager em;

    @Inject
    public GetHistoryHandler(EntityManager em, Mapper mapper) {
        this.mapper = mapper;
        this.em = em;
    }

    @Override
    public CommandResult execute(GetHistory cmd, User user) throws CommandException {

        // Gets query parameters.
        final long elementId = cmd.getElementId();
        final Date maxDate = cmd.getMaxDate();

        // Builds query.
        final StringBuilder sb = new StringBuilder();
        sb.append("SELECT h FROM HistoryToken h WHERE h.elementId = :elementId");
        if (maxDate != null) {
            sb.append(" AND h.date >= :maxDate");
        }
        sb.append(" ORDER BY h.date DESC");

        final Query query = em.createQuery(sb.toString());
        query.setParameter("elementId", elementId);
        if (maxDate != null) {
            query.setParameter("maxDate", maxDate);
        }

        // Retrieves query results and map results.
        @SuppressWarnings("unchecked")
        final List<HistoryToken> tokens = (List<HistoryToken>) query.getResultList();
        final ArrayList<HistoryTokenDTO> tokensDTO = new ArrayList<HistoryTokenDTO>();

        if (tokens != null) {
            for (final HistoryToken token : tokens) {
                tokensDTO.add(mapper.map(token, HistoryTokenDTO.class));
            }
        }

        return new HistoryResult(tokensDTO);
    }
}
