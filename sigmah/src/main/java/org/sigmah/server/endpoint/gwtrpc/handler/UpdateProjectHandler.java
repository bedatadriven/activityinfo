/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sigmah.server.endpoint.gwtrpc.handler;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dozer.Mapper;
import org.sigmah.shared.command.UpdateProject;
import org.sigmah.shared.command.handler.CommandHandler;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.command.result.ValueResultUtils;
import org.sigmah.shared.domain.Country;
import org.sigmah.shared.domain.Deleteable;
import org.sigmah.shared.domain.OrgUnit;
import org.sigmah.shared.domain.Project;
import org.sigmah.shared.domain.User;
import org.sigmah.shared.domain.element.DefaultFlexibleElementType;
import org.sigmah.shared.domain.element.FlexibleElement;
import org.sigmah.shared.domain.value.ListEntity;
import org.sigmah.shared.domain.value.Value;
import org.sigmah.shared.dto.EntityDTO;
import org.sigmah.shared.dto.element.DefaultFlexibleElementDTO;
import org.sigmah.shared.dto.element.FlexibleElementDTO;
import org.sigmah.shared.dto.element.handler.ValueEventWrapper;
import org.sigmah.shared.dto.value.ListEntityDTO;
import org.sigmah.shared.exception.CommandException;

import com.google.inject.Inject;

/**
 * Updates the values of the flexible elements for a specific project.
 * 
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
public class UpdateProjectHandler implements CommandHandler<UpdateProject> {

    private final static Log LOG = LogFactory.getLog(UpdateProjectHandler.class);

    private final EntityManager em;
    private final Mapper mapper;

    @Inject
    public UpdateProjectHandler(EntityManager em, Mapper mapper) {
        this.em = em;
        this.mapper = mapper;
    }

    @Override
    @SuppressWarnings("unchecked")
    public CommandResult execute(UpdateProject cmd, User user) throws CommandException {

        if (LOG.isDebugEnabled()) {
            LOG.debug("[execute] Updates project #" + cmd.getProjectId() + " with following values #"
                    + cmd.getValues().size() + " : " + cmd.getValues());
        }

        // Iterating over the value change events
        final List<ValueEventWrapper> values = cmd.getValues();
        for (final ValueEventWrapper valueEvent : values) {

            // Event parameters.
            final FlexibleElementDTO source = valueEvent.getSourceElement();
            final Serializable updateValue = valueEvent.getValue();

            if (LOG.isDebugEnabled()) {
                LOG.debug("[execute] Updates value of element #" + source.getId() + " (" + source.getEntityName() + ')');
                LOG.debug("[execute] Event of type " + valueEvent.getChangeType() + " with value " + updateValue + " ("
                        + updateValue.getClass() + ')');
            }

            // Case of the default flexible element which values arent't stored
            // like other values. These values impact directly the project.
            if (source instanceof DefaultFlexibleElementDTO) {

                final DefaultFlexibleElementDTO defaultElement = (DefaultFlexibleElementDTO) source;

                if (LOG.isDebugEnabled()) {
                    LOG.debug("[execute] Default element case '" + defaultElement.getType() + "'.");
                }

                // Saves the value and switch to the next value.
                saveDefaultElement(cmd.getProjectId(), defaultElement.getType(), updateValue);
                continue;
            }

            // Retrieving the current value
            final Value currentValue = retrieveValue(cmd.getProjectId(), (long) source.getId(), user);

            // Unique value of the flexible element.
            if (!(updateValue instanceof ListEntityDTO)) {

                if (LOG.isDebugEnabled()) {
                    LOG.debug("[execute] Basic value case.");
                }

                currentValue.setValue(updateValue.toString());
            }
            // Special case : this value is a part of a list which is the true
            // value of the flexible element.
            // (only used for the TripletValue class for the moment)
            else {

                if (LOG.isDebugEnabled()) {
                    LOG.debug("[execute] List value case.");
                }

                // The value of the element is a list of ids (default
                // separated).
                final List<Long> ids = ValueResultUtils.splitValuesAsLong(currentValue.getValue());

                if (LOG.isDebugEnabled()) {
                    LOG.debug("[execute] The current list of ids is : " + ids + ".");
                }

                // Cast the update value (as a DTO).
                final ListEntityDTO item = (ListEntityDTO) updateValue;

                Class<? extends ListEntity> clazz;
                try {
                    // Computes the respective entity class name.
                    clazz = (Class<? extends ListEntity>) Class.forName(Project.class.getPackage().getName() + '.'
                            + item.getEntityName());
                } catch (ClassNotFoundException e) {
                    // Unable to find the entity class, the event is ignored.
                    LOG.error("[execute] Unable to find the entity class : '" + item.getEntityName() + "'.");
                    continue;
                }

                switch (valueEvent.getChangeType()) {
                case ADD: {

                    if (LOG.isDebugEnabled()) {
                        LOG.debug("[execute] Adds a element to the list.");
                    }

                    // Adds the element.
                    ListEntity entity = mapper.map(item, clazz);
                    entity = em.merge(entity);

                    if (LOG.isDebugEnabled()) {
                        LOG.debug("[execute] Successfully create the entity with id #" + entity.getId() + ".");
                    }

                    // Updates the value.
                    ids.add(entity.getId());
                    currentValue.setValue(ValueResultUtils.mergeElements(ids));

                }
                    break;
                case REMOVE: {

                    if (LOG.isDebugEnabled()) {
                        LOG.debug("[execute] Removes a element from the list.");
                    }

                    // Retrieves the element.
                    final EntityDTO asEntityDTO = (EntityDTO) item;
                    final ListEntity entity = em.find(clazz, (long) asEntityDTO.getId());

                    if (entity instanceof Deleteable) {

                        // Marks the entity as deleted.
                        ((Deleteable) entity).delete();
                        em.merge(entity);

                        if (LOG.isDebugEnabled()) {
                            LOG.debug("[execute] Successfully remove the entity with id #" + entity.getId() + ".");
                        }

                        // Updates the value.
                        ids.remove(entity.getId());
                        currentValue.setValue(ValueResultUtils.mergeElements(ids));

                    } else {
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("[execute] The element isn't deletable, the event is ignored.");
                        }
                        continue;
                    }

                    break;
                }
                case EDIT: {

                    if (LOG.isDebugEnabled()) {
                        LOG.debug("[execute] Edits a element from the list.");
                    }

                    // Retrieves the element.
                    final ListEntity entity = mapper.map(item, clazz);
                    em.merge(entity);

                    if (LOG.isDebugEnabled()) {
                        LOG.debug("[execute] Successfully edit the entity with id #" + entity.getId() + ".");
                    }
                }
                    break;
                default:

                    if (LOG.isDebugEnabled()) {
                        LOG.debug("[execute] Unknown command " + valueEvent.getChangeType() + ".");
                    }

                    break;
                }

                if (LOG.isDebugEnabled()) {
                    LOG.debug("[execute] The new list of ids is : " + ids + ".");
                }
            }

            // Store the value.
            em.merge(currentValue);
        }

        return null;
    }

    /**
     * Retrieves the value for the given project and the given element. If there
     * isn't yet a value, it will be created.
     * 
     * @param projectId
     *            The project id.
     * @param source
     *            The source element.
     * @param user
     *            The user which launch the command.
     * @return The value.
     */
    public Value retrieveValue(int projectId, long elementId, User user) {

        // Retrieving the current value
        final Query query = em
                .createQuery("SELECT v FROM Value v WHERE v.containerId = :projectId and v.element.id = :elementId");
        query.setParameter("projectId", projectId);
        query.setParameter("elementId", elementId);

        Value currentValue = null;

        try {
            currentValue = (Value) query.getSingleResult();
        } catch (NoResultException nre) {
            // No current value
        }

        // Update operation.
        if (currentValue != null) {

            if (LOG.isDebugEnabled()) {
                LOG.debug("[execute] Retrieves a value for element #" + elementId + ".");
            }

            currentValue.setLastModificationAction('U');
        }
        // Create operation
        else {

            if (LOG.isDebugEnabled()) {
                LOG.debug("[execute] Creates a value for element #" + elementId + ".");
            }

            currentValue = new Value();
            currentValue.setLastModificationAction('C');

            // Parent element
            final FlexibleElement element = em.find(FlexibleElement.class, elementId);
            currentValue.setElement(element);

            // Container
            currentValue.setContainerId(projectId);
        }

        // Updates the value's fields.
        currentValue.setLastModificationDate(new Date());
        currentValue.setLastModificationUser(user);

        return currentValue;
    }

    /**
     * Updates the current project with the new value of a default element.
     * 
     * @param id
     *            The project id.
     * @param type
     *            The type of the default element.
     * @param value
     *            The new value.
     */
    private void saveDefaultElement(int id, DefaultFlexibleElementType type, Serializable value) {

        // All default values are managed as strings.
        // See DefaultFlexibleElementDTO.getComponent();
        if (value == null || !(value instanceof String)) {
            LOG.error("[saveDefaultElement] The value isn't a string and cannot be considered.");
            return;
        }

        final String stringValue = (String) value;

        // Retrieves container.
        final Project project = em.find(Project.class, id);
        final OrgUnit orgUnit = em.find(OrgUnit.class, id);

        if (project == null && orgUnit == null) {
            LOG.error("[saveDefaultElement] Container with id '" + id + "' not found.");
            return;
        }

        if (project != null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("[saveDefaultElement] Found project with code '" + project.getName() + "'.");
            }
        } else {
            if (LOG.isDebugEnabled()) {
                LOG.debug("[saveDefaultElement] Found org unit with code '" + orgUnit.getName() + "'.");
            }
        }

        switch (type) {
        case CODE:

            if (project != null) {
                project.setName(stringValue);
            } else {
                orgUnit.setName(stringValue);
            }

            if (LOG.isDebugEnabled()) {
                LOG.debug("[saveDefaultElement] Set container code to '" + stringValue + "'.");
            }
            break;
        case TITLE:

            if (project != null) {
                project.setFullName(stringValue);
            } else {
                orgUnit.setFullName(stringValue);
            }

            if (LOG.isDebugEnabled()) {
                LOG.debug("[saveDefaultElement] Set container full name to '" + stringValue + "'.");
            }
            break;
        case START_DATE: {

            // Decodes timestamp.
            if (project != null) {
                if ("".equals(stringValue)) {

                    project.setStartDate(null);

                    if (LOG.isDebugEnabled()) {
                        LOG.debug("[saveDefaultElement] Set container start date to null.");
                    }
                } else {

                    final long timestamp = Long.valueOf(stringValue);
                    final Date date = new Date(timestamp);
                    project.setStartDate(date);

                    if (LOG.isDebugEnabled()) {
                        LOG.debug("[saveDefaultElement] Set container start date to '" + date + "'.");
                    }
                }
            }
        }
            break;
        case END_DATE: {

            // Decodes timestamp.
            if (project != null) {
                if ("".equals(stringValue)) {

                    project.setEndDate(null);

                    if (LOG.isDebugEnabled()) {
                        LOG.debug("[saveDefaultElement] Set container end date to null.");
                    }
                } else {

                    final long timestamp = Long.valueOf(stringValue);
                    final Date date = new Date(timestamp);
                    project.setEndDate(date);

                    if (LOG.isDebugEnabled()) {
                        LOG.debug("[saveDefaultElement] Set container end date to '" + date + "'.");
                    }
                }
            }
        }
            break;
        case BUDGET: {
            // Decodes doubles.
            final String[] budgets = stringValue.split("\\|");
            final double plannedBudget = Double.parseDouble(budgets[0]);
            final double spendBudget = Double.parseDouble(budgets[1]);
            final double receivedBudget = Double.parseDouble(budgets[2]);

            if (project != null) {
                project.setPlannedBudget(plannedBudget);
                project.setSpendBudget(spendBudget);
                project.setReceivedBudget(receivedBudget);
            } else {
                orgUnit.setPlannedBudget(plannedBudget);
                orgUnit.setSpendBudget(spendBudget);
                orgUnit.setReceivedBudget(receivedBudget);
            }

            if (LOG.isDebugEnabled()) {
                LOG.debug("[saveDefaultElement] Set container budget to '" + plannedBudget + "|" + spendBudget + "|"
                        + receivedBudget + "'.");
            }
        }
            break;
        case COUNTRY: {

            if (project != null) {

                // Retrieves country.
                final Country country = em.find(Country.class, Integer.valueOf(stringValue));
                project.setCountry(country);

                if (LOG.isDebugEnabled()) {
                    LOG.debug("[saveDefaultElement] Set container country to '" + country.getName() + "'.");
                }
            }
        }
            break;
        case OWNER: {

            if (project != null) {

                // Retrieves user.
                final Query q = em.createQuery("SELECT u from User u WHERE u.email = :emailVal");
                q.setParameter("emailVal", stringValue);

                final User user;
                try {
                    user = (User) q.getSingleResult();
                } catch (NoResultException e) {
                    LOG.error("[saveDefaultElement] Unknown user with email '" + stringValue + "'.", e);
                    return;
                }

                project.setOwner(user);

                if (LOG.isDebugEnabled()) {
                    LOG.debug("[saveDefaultElement] Set container owner to '" + user.getEmail() + "'.");
                }
            }
        }
            break;
        default:
            LOG.error("[saveDefaultElement] Unknown type '" + type + "' for the default flexible elements.");
            return;
        }

        // Updates container.
        if (project != null) {
            em.merge(project);
        } else {
            em.merge(orgUnit);
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("[saveDefaultElement] Updates the container.");
        }
    }
}
