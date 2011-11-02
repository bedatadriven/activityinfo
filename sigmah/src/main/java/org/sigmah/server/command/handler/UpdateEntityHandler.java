/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.command.handler;

import java.util.Date;
import java.util.Map;

import javax.persistence.EntityManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sigmah.server.database.hibernate.entity.Activity;
import org.sigmah.server.database.hibernate.entity.Attribute;
import org.sigmah.server.database.hibernate.entity.AttributeGroup;
import org.sigmah.server.database.hibernate.entity.Indicator;
import org.sigmah.server.database.hibernate.entity.LockedPeriod;
import org.sigmah.server.database.hibernate.entity.User;
import org.sigmah.server.policy.ActivityPolicy;
import org.sigmah.server.policy.PropertyMap;
import org.sigmah.shared.command.UpdateEntity;
import org.sigmah.shared.command.handler.CommandHandler;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.exception.CommandException;
import org.sigmah.shared.exception.IllegalAccessCommandException;

import com.google.inject.Inject;
import com.google.inject.Injector;

/**
 * @author Alex Bertram
 * @see org.sigmah.shared.command.UpdateEntity
 */
public class UpdateEntityHandler extends BaseEntityHandler implements CommandHandler<UpdateEntity> {

    private final static Log LOG = LogFactory.getLog(UpdateEntityHandler.class);

    private final Injector injector;

    @Inject
    public UpdateEntityHandler(EntityManager em, Injector injector) {
        super(em);
        this.injector = injector;
    }

    @Override
    public CommandResult execute(UpdateEntity cmd, User user) throws CommandException {

        if (LOG.isDebugEnabled()) {
            LOG.debug("[execute] Update command for entity: " + cmd.getEntityName() + ".");
        }

        Map<String, Object> changes = cmd.getChanges().getTransientMap();
        PropertyMap changeMap = new PropertyMap(changes);

        if ("Activity".equals(cmd.getEntityName())) {
            ActivityPolicy policy = injector.getInstance(ActivityPolicy.class);
            policy.update(user, cmd.getId(), changeMap);

        } else if ("AttributeGroup".equals(cmd.getEntityName())) {
            updateAttributeGroup(cmd, changes);

        } else if ("Attribute".equals(cmd.getEntityName())) {
            updateAttribute(user, cmd, changes);

        } else if ("Indicator".equals(cmd.getEntityName())) {
            updateIndicator(user, cmd, changes);

        } else if ("LockedPeriod".equals(cmd.getEntityName())) {
            updateLockedPeriod(user, cmd, changes);
            
        } else {
            throw new RuntimeException("unknown entity type");
        }

        return null;
    }

    private void updateIndicator(User user, UpdateEntity cmd, Map<String, Object> changes)
            throws IllegalAccessCommandException {
        Indicator indicator = em.find(Indicator.class, cmd.getId());

        assertDesignPriviledges(user, indicator.getActivity().getDatabase());

        updateIndicatorProperties(indicator, changes);
    }
    
    private void updateLockedPeriod(User user, UpdateEntity cmd, Map<String, Object> changes) {
    	LockedPeriod lockedPeriod = em.find(LockedPeriod.class, cmd.getId());
    	
    	// TODO: check permissions when updating the LockedPeriod
    	//assertDesignPriviledges(user, database)
    	
    	updateLockedPeriodProperties(lockedPeriod, changes);
    }

    private void updateAttribute(User user, UpdateEntity cmd, Map<String, Object> changes) {
        Attribute attribute = em.find(Attribute.class, cmd.getId());

        // TODO: decide where attributes belong and how to manage them
        // assertDesignPriviledges(user, attribute.get);

        updateAttributeProperties(changes, attribute);
        AttributeGroup ag = em.find(AttributeGroup.class, attribute.getGroup().getId());
        Activity activity = ag.getActivities().iterator().next(); // Assume only one activity for the attr group
        activity.getDatabase().setLastSchemaUpdate(new Date());
    }

    private void updateAttributeGroup(UpdateEntity cmd, Map<String, Object> changes) {
        AttributeGroup group = em.find(AttributeGroup.class, cmd.getId());

        updateAttributeGroupProperties(group, changes);
        
        Activity activity = group.getActivities().iterator().next(); // Assume only one activity for the attr group
        activity.getDatabase().setLastSchemaUpdate(new Date());
    }
}
