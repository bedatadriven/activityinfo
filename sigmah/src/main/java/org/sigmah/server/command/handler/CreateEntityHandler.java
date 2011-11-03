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
import org.sigmah.server.command.handler.crud.ActivityPolicy;
import org.sigmah.server.command.handler.crud.PropertyMap;
import org.sigmah.server.command.handler.crud.UserDatabasePolicy;
import org.sigmah.server.database.hibernate.entity.Activity;
import org.sigmah.server.database.hibernate.entity.Attribute;
import org.sigmah.server.database.hibernate.entity.AttributeGroup;
import org.sigmah.server.database.hibernate.entity.Indicator;
import org.sigmah.server.database.hibernate.entity.User;
import org.sigmah.shared.command.CreateEntity;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.command.result.CreateResult;
import org.sigmah.shared.exception.CommandException;
import org.sigmah.shared.exception.IllegalAccessCommandException;

import com.google.inject.Inject;
import com.google.inject.Injector;

public class CreateEntityHandler extends BaseEntityHandler implements CommandHandler<CreateEntity> {

    private final Injector injector;

    private static final Log log = LogFactory.getLog(CreateEntityHandler.class);

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
        }else {
            throw new CommandException("Invalid entity class " + cmd.getEntityName());
        }
    }

    private CommandResult createAttributeGroup(CreateEntity cmd, Map<String, Object> properties) {
        AttributeGroup group = new AttributeGroup();
        updateAttributeGroupProperties(group, properties);

        em.persist(group);

        Activity activity = em.find(Activity.class, properties.get("activityId"));
        activity.getAttributeGroups().add(group);

        activity.getDatabase().setLastSchemaUpdate(new Date());

        return new CreateResult(group.getId());
    }
    
    private CommandResult createAttribute(CreateEntity cmd, Map<String, Object> properties) {
        Attribute attribute = new Attribute();
        AttributeGroup ag = em.getReference(AttributeGroup.class, properties.get("attributeGroupId")); 
        attribute.setGroup(ag);

        updateAttributeProperties(properties, attribute);
        
        Activity activity = ag.getActivities().iterator().next(); // Assume group has only one activity

        em.persist(attribute);
        activity.getDatabase().setLastSchemaUpdate(new Date());
        
        return new CreateResult(attribute.getId());
    }

    private CommandResult createIndicator(User user, CreateEntity cmd, Map<String, Object> properties)
            throws IllegalAccessCommandException {

        Indicator indicator = new Indicator();
        Activity activity = em.getReference(Activity.class, properties.get("activityId"));
        indicator.setActivity(activity);

        assertDesignPriviledges(user, indicator.getActivity().getDatabase());

        updateIndicatorProperties(indicator, properties);

        em.persist(indicator);
        activity.getDatabase().setLastSchemaUpdate(new Date());

        return new CreateResult(indicator.getId());
    }
}
