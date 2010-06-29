/*
 * This file is part of ActivityInfo.
 *
 * ActivityInfo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ActivityInfo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ActivityInfo.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2009 Alex Bertram and contributors.
 */

package org.sigmah.server.endpoint.gwtrpc.handler;

import com.google.inject.Inject;
import com.google.inject.Injector;
import org.sigmah.server.domain.*;
import org.sigmah.server.policy.ActivityPolicy;
import org.sigmah.server.policy.PropertyMap;
import org.sigmah.server.policy.SitePolicy;
import org.sigmah.server.policy.UserDatabasePolicy;
import org.sigmah.shared.command.CreateEntity;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.command.result.CreateResult;
import org.sigmah.shared.exception.CommandException;
import org.sigmah.shared.exception.IllegalAccessCommandException;

import javax.persistence.EntityManager;
import java.util.Map;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 * @see org.sigmah.shared.command.CreateEntity
 */
public class CreateEntityHandler extends BaseEntityHandler implements CommandHandler<CreateEntity> {

    private final Injector injector;

    @Inject
    public CreateEntityHandler(EntityManager em, Injector injector) {
        super(em);
        this.injector = injector;
    }

    @Override
    public CommandResult execute(CreateEntity cmd, User user) throws CommandException {

        Map<String, Object> properties = cmd.getProperties().getTransientMap();
        PropertyMap propertyMap = new PropertyMap(cmd.getProperties().getTransientMap());


        if ("UserDatabase".equals(cmd.getEntityName())) {
            UserDatabasePolicy policy = injector.getInstance(UserDatabasePolicy.class);
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
        } else if ("Site".equals(cmd.getEntityName())) {
            SitePolicy policy = injector.getInstance(SitePolicy.class);
            return new CreateResult((Integer)policy.create(user, propertyMap));
        } else {
            throw new CommandException("Invalid entity class " + cmd.getEntityName());
        }
    }


    private CommandResult createAttributeGroup(CreateEntity cmd, Map<String, Object> properties) {

        AttributeGroup group = new AttributeGroup();
        updateAttributeGroupProperties(group, properties);

        em.persist(group);

        Activity activity = em.find(Activity.class, properties.get("activityId"));
        activity.getAttributeGroups().add(group);

        return new CreateResult(group.getId());
    }


    private CommandResult createAttribute(CreateEntity cmd, Map<String, Object> properties) {

        Attribute attribute = new Attribute();
        attribute.setGroup(em.getReference(AttributeGroup.class, properties.get("attributeGroupId")));

        updateAttributeProperties(properties, attribute);

        em.persist(attribute);

        return new CreateResult(attribute.getId());
    }

    private CommandResult createIndicator(User user, CreateEntity cmd, Map<String, Object> properties) throws IllegalAccessCommandException {

        Indicator indicator = new Indicator();
        indicator.setActivity(em.getReference(Activity.class, properties.get("activityId")));

        assertDesignPriviledges(user, indicator.getActivity().getDatabase());

        updateIndicatorProperties(indicator, properties);

        em.persist(indicator);

        return new CreateResult(indicator.getId());

    }

}
