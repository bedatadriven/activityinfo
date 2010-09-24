/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.endpoint.gwtrpc.handler;

import java.io.Serializable;
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
import org.sigmah.shared.domain.User;
import org.sigmah.shared.domain.element.FlexibleElement;
import org.sigmah.shared.dto.value.BudgetPartsListValueDTO;
import org.sigmah.shared.dto.value.FilesListValueDTO;
import org.sigmah.shared.dto.value.IndicatorsListValueDTO;
import org.sigmah.shared.dto.value.TripletValueDTO;
import org.sigmah.shared.exception.CommandException;

import com.google.inject.Inject;

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

        final ValueResult valueResult = new ValueResult();

        // Creates the query to get the value for the flexible element (as
        // string) in the Value table.
        final Query valueQuery = em
                .createQuery("SELECT v.value FROM Value v WHERE v.parentProject.id = :projectId AND v.element.id = :elementId");
        valueQuery.setParameter("projectId", cmd.getProjectId());
        valueQuery.setParameter("elementId", cmd.getElementId());

        // Executes the query and tests if a value exists for this flexible
        // element.
        boolean isValueExisting = true;
        String valueAsString = null;
        try {
            valueAsString = (String) valueQuery.getSingleResult();
            if (valueAsString == null) {
                isValueExisting = false;
            }
        } catch (NoResultException e) {
            isValueExisting = false;
        } catch (ClassCastException e) {
            isValueExisting = false;
        }

        // No value exists for the flexible element.
        if (!isValueExisting) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("[execute] No value for this flexible element #" + cmd.getElementId() + ".");
            }
            return valueResult;
        }

        String queryString = null;
        String elementClassName = cmd.getElementEntityName();
        Class<? extends Serializable> clazz = null;
        boolean isList = false;

        // Creates the sub-select query to get the real value.
        if (elementClassName.equals("element.TripletsListElement")) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("[execute] Case TripletsListElementDTO.");
            }
            queryString = "SELECT tv FROM TripletValue tv WHERE tv.idList = :value";
            clazz = TripletValueDTO.class;
            isList = true;
        } else if (elementClassName.equals("element.IndicatorsListElement")) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("[execute] Case IndicatorsListElementDTO.");
            }
            queryString = "SELECT ilv FROM IndicatorsListValue ilv WHERE ilv.id.idList = :value";
            clazz = IndicatorsListValueDTO.class;
            isList = true;
        } else if (elementClassName.equals("element.BudgetDistributionElement")) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("[execute] Case BudgetDistributionElementDTO.");
            }
            queryString = "SELECT bplv FROM BudgetPartsListValue bplv WHERE bplv.id = :value";
            clazz = BudgetPartsListValueDTO.class;
            isList = true;
        } else if (elementClassName.equals("element.FilesListElement")) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("[execute] Case FilesListElementDTO.");
            }
            queryString = "SELECT flv FROM FilesListValue flv WHERE flv.id.idList = :value";
            clazz = FilesListValueDTO.class;
            isList = true;
        } else if (!(elementClassName.equals("element.MessageElement"))) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("[execute] Case others (but MessageElementDTO).");
            }
            queryString = "";
            clazz = String.class;
            isList = false;
        }

        if (queryString != null) {
            // Multiple results case
            if (isList) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("[execute] Multiple values for the element #" + cmd.getElementId() + ".");
                }

                final Query query = em.createQuery(queryString);
                // The value is casted as a long to perform the sub-select
                // query.
                query.setParameter("value", Long.valueOf(valueAsString));

                @SuppressWarnings("unchecked")
                final List<Object> objectsList = query.getResultList();

                final List<Serializable> serializablesList = new ArrayList<Serializable>();
                for (Object o : objectsList) {
                    serializablesList.add(mapper.map(o, clazz));
                }

                valueResult.setValuesObject(serializablesList);
            }
            // Single result case
            else {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("[execute] Single value for the element #" + cmd.getElementId() + ".");
                }

                if (clazz.equals(String.class)) {
                    valueResult.setValueObject(valueAsString);
                } else {
                    valueResult.setValueObject(mapper.map(valueAsString, clazz));
                }
            }

            valueResult.setListResult(isList);
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("[execute] Returned value = " + valueResult.toString() + ".");
        }

        return valueResult;

    }
}
