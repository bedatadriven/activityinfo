/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sigmah.server.endpoint.gwtrpc.handler;

import com.google.inject.Inject;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dozer.Mapper;
import org.sigmah.server.domain.Deleteable;
import org.sigmah.server.domain.Project;
import org.sigmah.server.domain.User;
import org.sigmah.server.domain.element.FlexibleElement;
import org.sigmah.server.domain.value.ListElementItem;
import org.sigmah.server.domain.value.Value;
import org.sigmah.shared.command.UpdateProject;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.dto.element.FlexibleElementDTO;
import org.sigmah.shared.dto.element.handler.ValueEvent;
import org.sigmah.shared.dto.element.handler.ValueEventWrapper;
import org.sigmah.shared.dto.value.ListElementItemDTO;
import org.sigmah.shared.exception.CommandException;

/**
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
    public CommandResult execute(UpdateProject cmd, User user) throws CommandException {
        // TODO: Break up this method in smaller pieces
        LOG.debug("Update " + cmd.getProjectId() + '/' + cmd.getValues().size() + " : " + cmd.getValues());

        // Iterating over the value change events
        final List<ValueEventWrapper> values = cmd.getValues();
        for (final ValueEventWrapper valueEvent : values) {
            // The modified object
            final FlexibleElementDTO source = valueEvent.getSourceElement();

            LOG.debug("Element " + source.getId() + " (" + source.getEntityName() + ')');

            if (valueEvent.getValues() == null) {
                // Single value
                LOG.debug(valueEvent.getChangeType()+" value " + valueEvent.getValue() + " (" + valueEvent.getValue().getClass() + ')');

                // Retrieving the current value
                final Query query = em.createQuery("SELECT v FROM Value v WHERE v.parentProject.id = :projectId and v.element.id = :elementId");
                query.setParameter("projectId", cmd.getProjectId());
                query.setParameter("elementId", (long) source.getId());

                final Value currentValue;
                Object object = null;

                try {
                    object = query.getSingleResult();
                } catch (NoResultException nre) {
                    // No current value
                }

                if (object != null) {
                    currentValue = (Value) object;
                    currentValue.setLastModificationAction('U'); // Update operation
                } else {
                    currentValue = new Value();
                    currentValue.setLastModificationAction('C'); // Creation of the value

                    // Parent element
                    currentValue.setElement(em.find(FlexibleElement.class, (long) source.getId()));

                    // Parent project
                    final Project project = new Project();
                    project.setId(cmd.getProjectId());
                    currentValue.setParentProject(project);
                }

                // Special case : TripletsList
                if (valueEvent.getValue() instanceof ListElementItemDTO) {
                    // TODO: Handle properly the "add" and "remove" methods
                    try {
                        final ListElementItemDTO item = (ListElementItemDTO) valueEvent.getValue();
                        final Class<? extends ListElementItem> clazz = (Class<? extends ListElementItem>) Class.forName(User.class.getPackage().getName() + '.' + item.getEntityName());

                        ListElementItem entity = mapper.map(item, clazz);

                        if(valueEvent.getChangeType() == ValueEvent.ChangeType.REMOVE) {
                            // Removes the object if it is deleteable
                            if(entity instanceof Deleteable) {
                                ((Deleteable)entity).delete();
                            } else {
                                LOG.debug("Entity not deleteable : " + entity.getClass());
                            }

                        } else {
                            // Adding / Editing...
                            Long listId = null;
                            if (currentValue.getValue() == null) {
                                // New triplet list
                                final String entityName;
                                int packageIndex = item.getEntityName().lastIndexOf('.');
                                if(packageIndex != -1)
                                    entityName = item.getEntityName().substring(packageIndex+1);
                                else
                                    entityName = item.getEntityName();

                                final Query listQuery = em.createQuery("SELECT MAX(e.idList) FROM " + entityName + " e");
                                try {
                                    listId = (Long) listQuery.getSingleResult();
                                } catch (NoResultException nre) {
                                    // No current value
                                }

                                if (listId == null) {
                                    listId = 0L;
                                }

                                listId++;

                                // Defining the new id
                                currentValue.setValue(listId.toString());

                            } else {
                                listId = Long.parseLong(currentValue.getValue());
                            }
                            entity.setIdList(listId);
                        }
                        
                        em.merge(entity);

                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(UpdateProjectHandler.class.getName()).log(Level.SEVERE, null, ex);
                    }

                } else {
                    currentValue.setValue(valueEvent.getValue().toString());
                }

                currentValue.setLastModificationDate(new Date());
                currentValue.setLastModificationUser(user);

                em.merge(currentValue);
            } else {
                // Multiple values
                LOG.debug("Element " + source.getId() + " (" + source.getEntityName() + ')');
                LOG.debug("List: " + valueEvent.getValues());

                // TODO: Handle or delete this
            }
        }

        return null;
    }

    /**
     * List identifier.
     */
    private static class Key {
        private Class<?> clazz;
        private int id;

        public Key(Class<?> clazz, int id) {
            this.clazz = clazz;
            this.id = id;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Key other = (Key) obj;
            if (this.clazz != other.clazz && (this.clazz == null || !this.clazz.equals(other.clazz))) {
                return false;
            }
            if (this.id != other.id) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 37 * hash + (this.clazz != null ? this.clazz.hashCode() : 0);
            hash = 37 * hash + this.id;
            return hash;
        }
    }
}
