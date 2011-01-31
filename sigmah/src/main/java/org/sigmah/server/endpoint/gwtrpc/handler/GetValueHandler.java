/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.endpoint.gwtrpc.handler;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dozer.Mapper;
import org.sigmah.shared.command.GetValue;
import org.sigmah.shared.command.handler.CommandHandler;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.command.result.ValueResult;
import org.sigmah.shared.command.result.ValueResultUtils;
import org.sigmah.shared.domain.User;
import org.sigmah.shared.domain.element.FlexibleElement;
import org.sigmah.shared.dto.value.BudgetPartsListValueDTO;
import org.sigmah.shared.dto.value.FileDTO;
import org.sigmah.shared.dto.value.IndicatorsListValueDTO;
import org.sigmah.shared.dto.value.ListableValue;
import org.sigmah.shared.dto.value.TripletValueDTO;
import org.sigmah.shared.exception.CommandException;

import com.google.inject.Inject;
import org.sigmah.shared.dto.report.ReportReference;
import org.sigmah.shared.domain.history.HistoryToken;

/**
 * Handler getting the value of a {@link FlexibleElement}.
 * 
 * @author Denis Colliot (dcolliot@ideia.fr)
 */
public class GetValueHandler implements CommandHandler<GetValue> {

    private final static Log LOG = LogFactory.getLog(GetValueHandler.class);

    private final EntityManager em;
    private final Mapper mapper;

    @Inject
    public GetValueHandler(EntityManager em, Mapper mapper) {
        this.em = em;
        this.mapper = mapper;
    }

    /**
     * Gets a flexible element value from the database.
     * 
     * @param cmd
     *            {@link GetValue} command containing the flexible element
     *            class, its id, and the project id
     * @param user
     *            user connected
     * 
     * @return a {@link ValueResult} object containing the value of the flexible
     *         element or containing {@code null} if there is no value defined
     *         for this element.
     */
    @Override
    public CommandResult execute(GetValue cmd, User user) throws CommandException {

        if (LOG.isDebugEnabled()) {
            LOG.debug("[execute] Getting value object from the database.");
            LOG.debug("[execute] GetValue command = " + cmd.toString() + ".");
        }

        // Command result.
        final ValueResult valueResult = new ValueResult();

        // Amendment
        String historyValue = null;
        if(cmd.getAmendmentId() != null) {
            final Query tokenQuery = em.createQuery("SELECT a.values FROM Amendment a WHERE a.id = :amendmentId");
            tokenQuery.setParameter("amendmentId", cmd.getAmendmentId());

            final List<HistoryToken> tokens = (List<HistoryToken>) tokenQuery.getResultList();
            if(tokens != null) {
                for(final HistoryToken token : tokens) {
                    if(token.getElementId() == cmd.getElementId())
                        historyValue = token.getValue();
                }
            }
        }

        // --------------------------------------------------------------------
        // STEP 1 : gets the string value (regardless of the element).
        // --------------------------------------------------------------------

        // Creates the query to get the value for the flexible element (as
        // string) in the Value table.
        final Query valueQuery = em
                .createQuery("SELECT v.value FROM Value v WHERE v.containerId = :projectId AND v.element.id = :elementId");
        valueQuery.setParameter("projectId", cmd.getProjectId());
        valueQuery.setParameter("elementId", cmd.getElementId());

        // Executes the query and tests if a value exists for this flexible
        // element.
        boolean isValueExisting = true;
        String valueAsString = null;
        try {
            valueAsString = (String) valueQuery.getSingleResult();
            if (valueAsString == null || valueAsString.equals("")) {
                isValueExisting = false;
            }
        } catch (NoResultException e) {
            isValueExisting = false;
        } catch (ClassCastException e) {
            isValueExisting = false;
        }

        // Overriding the value by the old one if we have to display an amendment
        if(historyValue != null) {
            valueAsString = historyValue;
            isValueExisting = true;
            
            valueResult.setAmendment(true);
        }

        // No value exists for the flexible element.
        if (!isValueExisting) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("[execute] No value for this flexible element #" + cmd.getElementId() + ".");
            }
            return valueResult;
        }

        // --------------------------------------------------------------------
        // STEP 2 : gets the true values (depending of the element).
        // Can be a list of id with requires a sub-select query.
        // --------------------------------------------------------------------

        Query query = null;
        String elementClassName = cmd.getElementEntityName();
        Class<? extends ListableValue> dtoClazz = null;
        boolean isList = false;

        // Creates the sub-select query to get the true value.
        if (elementClassName.equals("element.TripletsListElement")) {

            if (LOG.isDebugEnabled()) {
                LOG.debug("[execute] Case TripletsListElementDTO.");
            }

            dtoClazz = TripletValueDTO.class;
            isList = true;

            query = em.createQuery("SELECT tv FROM TripletValue tv WHERE tv.id IN (:idsList)");
            query.setParameter("idsList", ValueResultUtils.splitValuesAsLong(valueAsString));

        } else if (elementClassName.equals("element.IndicatorsListElement")) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("[execute] Case IndicatorsListElementDTO.");
            }

            dtoClazz = IndicatorsListValueDTO.class;
            isList = true;

            query = em.createQuery("SELECT ilv FROM IndicatorsListValue ilv WHERE ilv.id.idList = :value");
            query.setParameter("value", Long.valueOf(valueAsString));

        } else if (elementClassName.equals("element.BudgetDistributionElement")) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("[execute] Case BudgetDistributionElementDTO.");
            }

            dtoClazz = BudgetPartsListValueDTO.class;
            isList = true;

            query = em.createQuery("SELECT bplv FROM BudgetPartsListValue bplv WHERE bplv.id = :value");
            query.setParameter("value", Long.valueOf(valueAsString));

        } else if (elementClassName.equals("element.FilesListElement")) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("[execute] Case FilesListElementDTO.");
            }

            dtoClazz = FileDTO.class;
            isList = true;

            query = em.createQuery("SELECT f FROM File f WHERE f.id IN (:idsList)");
            query.setParameter("idsList", ValueResultUtils.splitValuesAsInteger(valueAsString));

        } else if (elementClassName.equals("element.ReportListElement")) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("[execute] Case ReportListElementDTO.");
            }

            dtoClazz = ReportReference.class;
            isList = true;

            query = em.createQuery("SELECT r FROM ProjectReport r WHERE r.id IN (:idList)");
            query.setParameter("idList", ValueResultUtils.splitValuesAsInteger(valueAsString));

        } else if (!(elementClassName.equals("element.MessageElement"))) {

            if (LOG.isDebugEnabled()) {
                LOG.debug("[execute] Case others (but MessageElementDTO).");
            }

            dtoClazz = ListableValue.class;
            isList = false;

        }

        // --------------------------------------------------------------------
        // STEP 3 : fill the command result with the values.
        // --------------------------------------------------------------------

        // No value for this kind of elements.
        if (dtoClazz == null) {
            return valueResult;
        }

        // Multiple results case
        if (isList) {

            if (LOG.isDebugEnabled()) {
                LOG.debug("[execute] Multiple values for the element #" + cmd.getElementId() + ".");
            }

            @SuppressWarnings("unchecked")
            final List<Object> objectsList = query.getResultList();

            final List<ListableValue> serializablesList = new ArrayList<ListableValue>();
            for (Object o : objectsList) {
                serializablesList.add(mapper.map(o, dtoClazz));
            }

            valueResult.setValuesObject(serializablesList);
        }
        // Single result case
        else {

            if (LOG.isDebugEnabled()) {
                LOG.debug("[execute] Single value for the element #" + cmd.getElementId() + ".");
            }

            // A single value is always interpreted as a string.
            valueResult.setValueObject(valueAsString);
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("[execute] Returned value = " + valueResult + ".");
        }

        return valueResult;
    }

}
