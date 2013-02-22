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

import javax.persistence.EntityManager;

import org.activityinfo.server.command.handler.crud.ActivityPolicy;
import org.activityinfo.server.command.handler.crud.PropertyMap;
import org.activityinfo.server.command.handler.crud.UserDatabasePolicy;
import org.activityinfo.server.database.hibernate.entity.Activity;
import org.activityinfo.server.database.hibernate.entity.Attribute;
import org.activityinfo.server.database.hibernate.entity.AttributeGroup;
import org.activityinfo.server.database.hibernate.entity.Indicator;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.shared.command.CreateEntity;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.command.result.CreateResult;
import org.activityinfo.shared.exception.CommandException;
import org.activityinfo.shared.exception.IllegalAccessCommandException;

import com.google.inject.Inject;
import com.google.inject.Injector;

public class CreateEntityHandler extends BaseEntityHandler implements
    CommandHandler<CreateEntity> {

    private final Injector injector;

    @Inject
    public CreateEntityHandler(EntityManager em, Injector injector) {
        super(em);
        this.injector = injector;
    }

    @Override
    public CommandResult execute(CreateEntity cmd, User user)
        throws CommandException {

        Map<String, Object> properties = cmd.getProperties().getTransientMap();
        PropertyMap propertyMap = new PropertyMap(cmd.getProperties()
            .getTransientMap());

        if ("UserDatabase".equals(cmd.getEntityName())) {
            UserDatabasePolicy policy = injector
                .getInstance(UserDatabasePolicy.class);
            return new CreateResult((Integer) policy.create(user, propertyMap));
        } else if ("Activity".equals(cmd.getEntityName())) {
            ActivityPolicy policy = injector.getInstance(ActivityPolicy.class);
            return new CreateResult((Integer) policy.create(user, propertyMap));
        } else if ("AttributeGroup".equals(cmd.getEntityName())) {
            return createAttributeGroup(cmd, properties);
        } else if ("Attribute".equals(cmd.getEntityName())) {
            return createAttribute(cmd, properties);
        } else if ("Indicator".equals(cmd.getEntityName())) {
            return createIndicator(user, cmd, properties);
        } else {
            throw new CommandException("Invalid entity class "
                + cmd.getEntityName());
        }
    }

    private CommandResult createAttributeGroup(CreateEntity cmd,
        Map<String, Object> properties) {
        AttributeGroup group = new AttributeGroup();
        updateAttributeGroupProperties(group, properties);

        entityManager().persist(group);

        Activity activity = entityManager().find(Activity.class,
            properties.get("activityId"));
        activity.getAttributeGroups().add(group);

        activity.getDatabase().setLastSchemaUpdate(new Date());

        return new CreateResult(group.getId());
    }

    private CommandResult createAttribute(CreateEntity cmd,
        Map<String, Object> properties) {
        Attribute attribute = new Attribute();
        AttributeGroup ag = entityManager().getReference(AttributeGroup.class,
            properties.get("attributeGroupId"));
        attribute.setGroup(ag);

        updateAttributeProperties(properties, attribute);

        Activity activity = ag.getActivities().iterator().next(); // Assume
                                                                  // group has
                                                                  // only one
                                                                  // activity

        entityManager().persist(attribute);
        activity.getDatabase().setLastSchemaUpdate(new Date());

        return new CreateResult(attribute.getId());
    }

    private CommandResult createIndicator(User user, CreateEntity cmd,
        Map<String, Object> properties)
        throws IllegalAccessCommandException {

        Indicator indicator = new Indicator();
        Activity activity = entityManager().getReference(Activity.class,
            properties.get("activityId"));
        indicator.setActivity(activity);

        assertDesignPriviledges(user, indicator.getActivity().getDatabase());

        updateIndicatorProperties(indicator, properties);

        entityManager().persist(indicator);
        activity.getDatabase().setLastSchemaUpdate(new Date());

        return new CreateResult(indicator.getId());
    }
}
