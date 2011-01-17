package org.sigmah.server.endpoint.gwtrpc.handler;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.sigmah.shared.command.GetHistory;
import org.sigmah.shared.command.handler.CommandHandler;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.command.result.HistoryResult;
import org.sigmah.shared.domain.User;
import org.sigmah.shared.domain.history.HistoryToken;
import org.sigmah.shared.dto.history.HistoryTokenDTO;
import org.sigmah.shared.dto.history.HistoryTokenListDTO;
import org.sigmah.shared.exception.CommandException;

import com.google.inject.Inject;

public class GetHistoryHandler implements CommandHandler<GetHistory> {

    private final EntityManager em;

    @Inject
    public GetHistoryHandler(EntityManager em) {
        this.em = em;
    }

    @Override
    public CommandResult execute(GetHistory cmd, User user) throws CommandException {

        // Gets query parameters.
        final long elementId = cmd.getElementId();
        final int projectId = cmd.getProjectId();
        final Date maxDate = cmd.getMaxDate();

        // Builds query.
        final StringBuilder sb = new StringBuilder();
        sb.append("SELECT h FROM HistoryToken h WHERE h.elementId = :elementId AND h.projectId = :projectId");
        if (maxDate != null) {
            sb.append(" AND h.date >= :maxDate");
        }
        sb.append(" ORDER BY h.date DESC");

        final Query query = em.createQuery(sb.toString());
        query.setParameter("elementId", elementId);
        query.setParameter("projectId", projectId);
        if (maxDate != null) {
            query.setParameter("maxDate", maxDate);
        }

        // Retrieves query results and map results.
        @SuppressWarnings("unchecked")
        final List<HistoryToken> tokens = (List<HistoryToken>) query.getResultList();

        final HashMap<Date, HistoryTokenListDTO> mappedTokensDTO = new HashMap<Date, HistoryTokenListDTO>();
        final ArrayList<HistoryTokenListDTO> tokensDTO = new ArrayList<HistoryTokenListDTO>();

        if (tokens != null) {
            for (final HistoryToken token : tokens) {

                HistoryTokenListDTO list = mappedTokensDTO.get(token.getDate());

                if (list == null) {
                    list = new HistoryTokenListDTO();
                    list.setDate(token.getDate());

                    final User owner = token.getUser();
                    if (owner != null) {
                        list.setUserEmail(owner.getEmail());
                        list.setUserFirstName(owner.getFirstName());
                        list.setUserName(owner.getName());
                    }

                    mappedTokensDTO.put(token.getDate(), list);
                    tokensDTO.add(list);
                }

                if (list.getTokens() == null) {
                    list.setTokens(new ArrayList<HistoryTokenDTO>());
                }

                list.getTokens().add(new HistoryTokenDTO(token.getValue(), token.getType()));
            }
        }

        return new HistoryResult(tokensDTO);
    }
}
