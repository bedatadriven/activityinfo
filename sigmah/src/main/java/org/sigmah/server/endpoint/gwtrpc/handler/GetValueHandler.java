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
import org.sigmah.server.domain.User;
import org.sigmah.server.domain.element.FlexibleElement;
import org.sigmah.shared.command.GetValue;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.command.result.ValueResult;
import org.sigmah.shared.dto.element.BudgetDistributionElementDTO;
import org.sigmah.shared.dto.element.FilesListElementDTO;
import org.sigmah.shared.dto.element.IndicatorsListElementDTO;
import org.sigmah.shared.dto.element.MessageElementDTO;
import org.sigmah.shared.dto.element.TripletsListElementDTO;
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
     * @param cmd {@link GetValue} command containing the flexible element class, its id, and the project id
     * @param user user connected
     * 
     * @return a {@link ValueResult} object containing the value of the flexible element or containing 
     * {@code null} if there is no value defined for this element.
     */
    @Override
    public CommandResult execute(GetValue cmd, User user) throws CommandException {
    	if (LOG.isDebugEnabled()) {
    		LOG.debug("[execute] Getting value object from the database.");
    	}
    	
    	String queryString = null;
    	String elementClassName = cmd.getElementClassName();
    	Class<? extends Serializable> clazz = null;
    	ValueResult valueResult = new ValueResult();
    	boolean isList = false;
    	
		if (elementClassName.equals(TripletsListElementDTO.class.getName())) {
			queryString = "select tv from TripletValue tv, Value v where tv.idList = v.value and v.parentProject.id = :projectId and v.element.id = :elementId";
			clazz = TripletValueDTO.class;
			isList = true;
		}
		else if (elementClassName.equals(IndicatorsListElementDTO.class.getName())) {
			queryString = "select ilv from IndicatorsListValue ilv, Value v where ilv.id.idList = v.value and v.parentProject.id = :projectId and v.element.id = :elementId";
			clazz = IndicatorsListValueDTO.class;
			isList = true;
		}
		else if (elementClassName.equals(BudgetDistributionElementDTO.class.getName())) {
			queryString = "select bplv from BudgetPartsListValue bplv, Value v where bplv.id = v.value and v.parentProject.id = :projectId and v.element.id = :elementId";
			clazz = BudgetPartsListValueDTO.class;
			isList = true;
		}
		else if (elementClassName.equals(FilesListElementDTO.class.getName())) {
			queryString = "select flv from FilesListValue flv, Value v where flv.id.idList = v.value and v.parentProject.id = :projectId and v.element.id = :elementId";
			clazz = FilesListValueDTO.class;
			isList = true;
		}
		else if (!(elementClassName.equals(MessageElementDTO.class.getName()))) {
			queryString = "select v.value from Value v where v.parentProject.id = :projectId and v.element.id = :elementId";
			clazz = String.class;
		}
		
		if (queryString != null) {
			Query query = em.createQuery(queryString);
			query.setParameter("projectId", cmd.getProjectId());
			query.setParameter("elementId", cmd.getElementId());
	    	
			// Multiple results case
			if (isList) {
				@SuppressWarnings("unchecked")
				List<Object> objectsList = query.getResultList();
				
				List<Serializable> serializablesList = new ArrayList<Serializable>();
				for (Object o : objectsList) {
					serializablesList.add(mapper.map(o, clazz));
				}
				
				valueResult.setValuesObject(serializablesList);
			}
			// Single result case
			else {
				try {
					Object object = query.getSingleResult();
					if (object != null) {
						if (clazz.equals(String.class)) {
							valueResult.setValueObject(String.valueOf(object));
						}
						else {
							valueResult.setValueObject(mapper.map(object, clazz));
						}
					}
				}
				catch (NoResultException nre) {
					if (LOG.isDebugEnabled()) {
			    		LOG.debug("[execute] No value for this flexible element.");
			    	}
				}
			}
			
			valueResult.setListResult(isList);
		}
		
		return valueResult;
		
    }
    
}
