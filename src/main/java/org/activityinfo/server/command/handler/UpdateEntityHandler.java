

package org.activityinfo.server.command.handler;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import java.util.Date;
import java.util.Map;
import java.util.logging.Logger;

import javax.persistence.EntityManager;

import org.activityinfo.server.command.handler.crud.ActivityPolicy;
import org.activityinfo.server.command.handler.crud.PropertyMap;
import org.activityinfo.server.database.hibernate.entity.Activity;
import org.activityinfo.server.database.hibernate.entity.Attribute;
import org.activityinfo.server.database.hibernate.entity.AttributeGroup;
import org.activityinfo.server.database.hibernate.entity.Indicator;
import org.activityinfo.server.database.hibernate.entity.LockedPeriod;
import org.activityinfo.server.database.hibernate.entity.Target;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.shared.command.UpdateEntity;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.exception.CommandException;
import org.activityinfo.shared.exception.IllegalAccessCommandException;

import com.google.inject.Inject;
import com.google.inject.Injector;

/**
 * @author Alex Bertram
 * @see org.activityinfo.shared.command.UpdateEntity
 */
public class UpdateEntityHandler extends BaseEntityHandler implements CommandHandler<UpdateEntity> {

    private final static Logger LOG = Logger.getLogger(UpdateEntityHandler.class.getName());

    private final Injector injector;

    @Inject
    public UpdateEntityHandler(EntityManager em, Injector injector) {
        super(em);
        this.injector = injector;
    }

    @Override
    public CommandResult execute(UpdateEntity cmd, User user) throws CommandException {

        LOG.fine("[execute] Update command for entity: " + cmd.getEntityName() + ".");
       

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
            
        } else if("Target".equals(cmd.getEntityName())){
        	updateTarget(user, cmd, changes);
        } else {
            throw new RuntimeException("unknown entity type");
        }

        return null;
    }

    private void updateIndicator(User user, UpdateEntity cmd, Map<String, Object> changes)
            throws IllegalAccessCommandException {
        Indicator indicator = entityManager().find(Indicator.class, cmd.getId());

        assertDesignPriviledges(user, indicator.getActivity().getDatabase());

        updateIndicatorProperties(indicator, changes);
    }
    
    private void updateLockedPeriod(User user, UpdateEntity cmd, Map<String, Object> changes) {
    	LockedPeriod lockedPeriod = entityManager().find(LockedPeriod.class, cmd.getId());
    	
    	// TODO: check permissions when updating the LockedPeriod
    	//assertDesignPriviledges(user, database)
    	
    	updateLockedPeriodProperties(lockedPeriod, changes);
    }

    private void updateAttribute(User user, UpdateEntity cmd, Map<String, Object> changes) {
        Attribute attribute = entityManager().find(Attribute.class, cmd.getId());

        // TODO: decide where attributes belong and how to manage them
        // assertDesignPriviledges(user, attribute.get);

        updateAttributeProperties(changes, attribute);
        AttributeGroup ag = entityManager().find(AttributeGroup.class, attribute.getGroup().getId());
        Activity activity = ag.getActivities().iterator().next(); // Assume only one activity for the attr group
        activity.getDatabase().setLastSchemaUpdate(new Date());
    }

    private void updateAttributeGroup(UpdateEntity cmd, Map<String, Object> changes) {
        AttributeGroup group = entityManager().find(AttributeGroup.class, cmd.getId());

        updateAttributeGroupProperties(group, changes);
        
        Activity activity = group.getActivities().iterator().next(); // Assume only one activity for the attr group
        activity.getDatabase().setLastSchemaUpdate(new Date());
    }
    
    private void updateTarget(User user, UpdateEntity cmd, Map<String, Object> changes){
   		// 	TODO: check permissions when updating the Target
    	Target target = entityManager().find(Target.class, cmd.getId());
    	
    	updateTargetProperties(target, changes);

    	target.getUserDatabase().setLastSchemaUpdate(new Date());
    }
}
