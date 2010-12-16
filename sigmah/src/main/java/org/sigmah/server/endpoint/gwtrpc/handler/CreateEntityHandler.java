/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.endpoint.gwtrpc.handler;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dozer.Mapper;
import org.sigmah.server.policy.ActivityPolicy;
import org.sigmah.server.policy.PersonalEventPolicy;
import org.sigmah.server.policy.ProjectPolicy;
import org.sigmah.server.policy.ProjectReportPolicy;
import org.sigmah.server.policy.PropertyMap;
import org.sigmah.server.policy.SitePolicy;
import org.sigmah.server.policy.UserDatabasePolicy;
import org.sigmah.shared.command.CreateEntity;
import org.sigmah.shared.command.handler.CommandHandler;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.command.result.CreateResult;
import org.sigmah.shared.domain.Activity;
import org.sigmah.shared.domain.Attribute;
import org.sigmah.shared.domain.AttributeGroup;
import org.sigmah.shared.domain.Indicator;
import org.sigmah.shared.domain.Project;
import org.sigmah.shared.domain.ProjectFunding;
import org.sigmah.shared.domain.User;
import org.sigmah.shared.domain.reminder.MonitoredPoint;
import org.sigmah.shared.domain.reminder.MonitoredPointList;
import org.sigmah.shared.domain.reminder.Reminder;
import org.sigmah.shared.domain.reminder.ReminderList;
import org.sigmah.shared.dto.ProjectDTOLight;
import org.sigmah.shared.dto.ProjectFundingDTO;
import org.sigmah.shared.dto.reminder.MonitoredPointDTO;
import org.sigmah.shared.dto.reminder.ReminderDTO;
import org.sigmah.shared.exception.CommandException;
import org.sigmah.shared.exception.IllegalAccessCommandException;

import com.google.inject.Inject;
import com.google.inject.Injector;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 * @see org.sigmah.shared.command.CreateEntity
 */
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
        } else if ("Project".equals(cmd.getEntityName())) {
            final ProjectPolicy policy = injector.getInstance(ProjectPolicy.class);
            final Project createdProject = (Project) policy.create(user, propertyMap);
            final ProjectDTOLight mappedProject = GetProjectsHandler.mapProject(em, injector.getInstance(Mapper.class),
                    user, createdProject, false);
            return new CreateResult(mappedProject);
        } else if ("Site".equals(cmd.getEntityName())) {
            SitePolicy policy = injector.getInstance(SitePolicy.class);
            return new CreateResult((Integer) policy.create(user, propertyMap));
        } else if ("PersonalEvent".equals(cmd.getEntityName())) {
            PersonalEventPolicy policy = injector.getInstance(PersonalEventPolicy.class);
            return new CreateResult((Integer) policy.create(user, propertyMap));
        } else if ("ProjectFunding".equals(cmd.getEntityName())) {
            return createFunding(properties);
        } else if ("ProjectReport".equals(cmd.getEntityName())) {
            ProjectReportPolicy policy = injector.getInstance(ProjectReportPolicy.class);
            return new CreateResult((Integer) policy.create(user, propertyMap));
        } else if ("MonitoredPoint".equals(cmd.getEntityName())) {
            return createMonitoredPoint(properties);
        } else if ("Reminder".equals(cmd.getEntityName())) {
            return createReminder(properties);
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

    private CommandResult createIndicator(User user, CreateEntity cmd, Map<String, Object> properties)
            throws IllegalAccessCommandException {

        Indicator indicator = new Indicator();
        indicator.setActivity(em.getReference(Activity.class, properties.get("activityId")));

        assertDesignPriviledges(user, indicator.getActivity().getDatabase());

        updateIndicatorProperties(indicator, properties);

        em.persist(indicator);

        return new CreateResult(indicator.getId());

    }

    private CommandResult createFunding(Map<String, Object> properties) {

        // Retrieves parameters.
        Object fundingId = properties.get("fundingId");
        Object fundedId = properties.get("fundedId");
        Object percentage = properties.get("percentage");

        // Retrieves projects.
        final Project fundingProject = em.find(Project.class, fundingId);
        final Project fundedProject = em.find(Project.class, fundedId);

        // Retrieves the eventual already existing link.
        final Query query = em.createQuery("SELECT f FROM ProjectFunding f WHERE f.funding = :p1 AND f.funded = :p2");
        query.setParameter("p1", fundingProject);
        query.setParameter("p2", fundedProject);

        ProjectFunding funding;

        // Updates or creates the link.
        boolean create = false;
        try {
            funding = (ProjectFunding) query.getSingleResult();
        } catch (NoResultException e) {
            funding = new ProjectFunding();
            funding.setFunding(fundingProject);
            funding.setFunded(fundedProject);
            create = true;
        }

        funding.setPercentage((Double) percentage);

        // Saves.
        em.persist(funding);

        final CreateResult result = new CreateResult(injector.getInstance(Mapper.class).map(funding,
                ProjectFundingDTO.class));

        // Sets update or create to inform the client-side.
        result.setNewId(create ? -1 : 1);

        return result;
    }

    private CommandResult createMonitoredPoint(Map<String, Object> properties) {

        if (log.isDebugEnabled()) {
            log.debug("[createMonitoredPoint] Starts monitored point creation.");
        }

        // Retrieves parameters.
        Object expectedDate = properties.get("expectedDate");
        Object label = properties.get("label");
        Object projectId = properties.get("projectId");

        // Retrieves project.
        final Project project = em.find(Project.class, projectId);

        if (log.isDebugEnabled()) {
            log.debug("[createMonitoredPoint] Retrieves the project #" + project.getId() + ".");
        }

        // Retrieves list.
        MonitoredPointList list = project.getPointsList();

        // Creates the list if needed.
        if (list == null) {

            if (log.isDebugEnabled()) {
                log.debug("[createMonitoredPoint] The project #" + project.getId()
                        + " doesn't have a points list. Creates it.");
            }

            list = new MonitoredPointList();
            list.setPoints(new ArrayList<MonitoredPoint>());
            project.setPointsList(list);
        }

        // Creates point.
        final MonitoredPoint point = new MonitoredPoint();
        point.setLabel((String) label);
        point.setExpectedDate(new Date((Long) expectedDate));
        point.setCompletionDate(null);
        point.setFile(null);

        // Adds it to the list.
        list.addMonitoredPoint(point);

        // Saves it.
        em.persist(project);

        if (log.isDebugEnabled()) {
            log.debug("[createMonitoredPoint] Ends monitored point creation #" + point.getId() + " in list #"
                    + list.getId() + ".");
        }

        return new CreateResult(injector.getInstance(Mapper.class).map(point, MonitoredPointDTO.class));
    }

    private CommandResult createReminder(Map<String, Object> properties) {

        if (log.isDebugEnabled()) {
            log.debug("[createReminder] Starts reminder creation.");
        }

        // Retrieves parameters.
        Object expectedDate = properties.get("expectedDate");
        Object label = properties.get("label");
        Object projectId = properties.get("projectId");

        // Retrieves project.
        final Project project = em.find(Project.class, projectId);

        if (log.isDebugEnabled()) {
            log.debug("[createReminder] Retrieves the project #" + project.getId() + ".");
        }

        // Retrieves list.
        ReminderList list = project.getRemindersList();

        // Creates the list if needed.
        if (list == null) {

            if (log.isDebugEnabled()) {
                log.debug("[createReminder] The project #" + project.getId()
                        + " doesn't have a reminders list. Creates it.");
            }

            list = new ReminderList();
            list.setReminders(new ArrayList<Reminder>());
            project.setRemindersList(list);
        }

        // Creates point.
        final Reminder reminder = new Reminder();
        reminder.setLabel((String) label);
        reminder.setExpectedDate(new Date((Long) expectedDate));
        reminder.setCompletionDate(null);

        // Adds it to the list.
        list.addReminder(reminder);

        // Saves it.
        em.persist(project);

        if (log.isDebugEnabled()) {
            log.debug("[createReminder] Ends reminder creation #" + reminder.getId() + " in list #" + list.getId()
                    + ".");
        }

        return new CreateResult(injector.getInstance(Mapper.class).map(reminder, ReminderDTO.class));
    }
}
