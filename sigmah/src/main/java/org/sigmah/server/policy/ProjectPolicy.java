/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.policy;

import java.util.ArrayList;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sigmah.client.page.project.logframe.ProjectLogFramePresenter;
import org.sigmah.server.dao.Transactional;
import org.sigmah.shared.domain.Country;
import org.sigmah.shared.domain.OrgUnit;
import org.sigmah.shared.domain.Phase;
import org.sigmah.shared.domain.PhaseModel;
import org.sigmah.shared.domain.Project;
import org.sigmah.shared.domain.ProjectModel;
import org.sigmah.shared.domain.User;
import org.sigmah.shared.domain.calendar.PersonalCalendar;
import org.sigmah.shared.domain.logframe.LogFrame;
import org.sigmah.shared.domain.logframe.LogFrameGroup;
import org.sigmah.shared.domain.logframe.LogFrameGroupType;
import org.sigmah.shared.domain.logframe.LogFrameModel;
import org.sigmah.shared.domain.reminder.MonitoredPointList;
import org.sigmah.shared.domain.reminder.ReminderList;

import com.google.inject.Inject;

public class ProjectPolicy implements EntityPolicy<Project> {

    private static final Log log = LogFactory.getLog(ProjectPolicy.class);

    private final EntityManager em;

    @Inject
    public ProjectPolicy(EntityManager em) {
        this.em = em;
    }

    @Override
    public Object create(User user, PropertyMap properties) {
        Project project = createProject(properties, user);
        return project;
    }

    @Transactional
    protected Project createProject(PropertyMap properties, User user) {

        if (log.isDebugEnabled()) {
            log.debug("[createProject] Starting project creation.");
        }

        // Creates a new calendar
        PersonalCalendar calendar = new PersonalCalendar();
        calendar.setName(properties.<String> get("calendarName"));
        em.persist(calendar);

        // Creates the new project
        Project project = new Project();

        // Userdatabase attributes.
        project.setStartDate(new Date());
        project.setOwner(em.getReference(User.class, user.getId()));

        // Monitored points.
        project.setPointsList(new MonitoredPointList());

        // Reminders.
        project.setRemindersList(new ReminderList());

        // Org unit.
        final OrgUnit orgunit = em.find(OrgUnit.class, properties.<Integer> get("orgUnitId"));
        project.getPartners().add(orgunit);

        // Country
        final Country country = getProjectCountry(orgunit);
        project.setCountry(country);

        if (log.isDebugEnabled()) {
            log.debug("[createProject] Selected country: " + country.getName() + ".");
        }

        // Considers name length constraints.
        final String name = properties.<String> get("name");
        if (name != null) {
            if (name.length() > 16) {
                project.setName(name.substring(0, 16));
            } else {
                project.setName(name);
            }
        } else {
            project.setName("noname");
        }

        // Considers name length constraints.
        final String fullName = properties.<String> get("fullName");
        if (fullName != null) {
            if (fullName.length() > 50) {
                project.setFullName(fullName.substring(0, 50));
            } else {
                project.setFullName(fullName);
            }
        } else {
            project.setFullName("");
        }

        project.setLastSchemaUpdate(new Date());
        project.setCalendarId(calendar.getId());

        // Project attributes.
        final ProjectModel model = em.getReference(ProjectModel.class, properties.<Long> get("modelId"));
        project.setProjectModel(model);
        project.setLogFrame(null);
        project.setPlannedBudget(properties.<Double> get("budget"));

        // Creates and adds phases.
        for (final PhaseModel phaseModel : model.getPhases()) {

            final Phase phase = new Phase();
            phase.setModel(phaseModel);

            project.addPhase(phase);

            if (log.isDebugEnabled()) {
                log.debug("[createProject] Creates and adds phase instance for model: " + phaseModel.getName() + ".");
            }

            // Searches the root phase.
            if (model.getRootPhase() != null && phaseModel.getId() == model.getRootPhase().getId()) {

                // Sets it.
                phase.setStartDate(new Date());
                project.setCurrentPhase(phase);

                if (log.isDebugEnabled()) {
                    log.debug("[createProject] Sets the first phase: " + phaseModel.getName() + ".");
                }
            }
        }

        // The model doesn't define a root phase, select the first declared
        // phase as the first one.
        if (model.getRootPhase() == null) {

            if (log.isDebugEnabled()) {
                log.debug("[createProject] No root phase defined for this model, active the first phase by default.");
            }

            // Selects the first phase by default.
            final Phase phase = project.getPhases().get(0);

            // Sets it.
            phase.setStartDate(new Date());
            project.setCurrentPhase(phase);

            if (log.isDebugEnabled()) {
                log.debug("[createProject] Sets the first phase: " + phase.getModel().getName() + ".");
            }
        }

        em.persist(project);

        if (log.isDebugEnabled()) {
            log.debug("[createProject] Project successfully created.");
        }

        // Updates the project with a default log frame.
        final LogFrame logFrame = createDefaultLogFrame(project);
        project.setLogFrame(logFrame);
        project = em.merge(project);

        return project;
    }

    /**
     * Creates a well-configured default log frame for the new project.
     * 
     * @param project
     *            The project.
     * @return The log frame.
     */
    private LogFrame createDefaultLogFrame(Project project) {

        // Creates a new log frame (with a default model)
        final LogFrame logFrame = new LogFrame();
        logFrame.setParentProject(project);

        // Default groups.
        final ArrayList<LogFrameGroup> groups = new ArrayList<LogFrameGroup>();

        LogFrameGroup group = new LogFrameGroup();
        group.setType(LogFrameGroupType.SPECIFIC_OBJECTIVE);
        group.setParentLogFrame(logFrame);
        group.setLabel(ProjectLogFramePresenter.DEFAULT_GROUP_LABEL);
        groups.add(group);

        group = new LogFrameGroup();
        group.setType(LogFrameGroupType.EXPECTED_RESULT);
        group.setParentLogFrame(logFrame);
        group.setLabel(ProjectLogFramePresenter.DEFAULT_GROUP_LABEL);
        groups.add(group);

        group = new LogFrameGroup();
        group.setType(LogFrameGroupType.ACTIVITY);
        group.setParentLogFrame(logFrame);
        group.setLabel(ProjectLogFramePresenter.DEFAULT_GROUP_LABEL);
        groups.add(group);

        group = new LogFrameGroup();
        group.setType(LogFrameGroupType.PREREQUISITE);
        group.setParentLogFrame(logFrame);
        group.setLabel(ProjectLogFramePresenter.DEFAULT_GROUP_LABEL);
        groups.add(group);

        logFrame.setGroups(groups);

        // Links to the log frame model.
        LogFrameModel model = project.getProjectModel().getLogFrameModel();

        if (model == null) {
            model = new LogFrameModel();
            model.setName("Auto-created default model at " + new Date());
            logFrame.setLogFrameModel(model);
        }

        logFrame.setLogFrameModel(model);

        em.persist(logFrame);

        return logFrame;
    }

    /**
     * Searches the country for the given org unit.
     * 
     * @param orgUnit
     *            The org unit.
     * @return The country
     */
    private Country getProjectCountry(OrgUnit orgUnit) {

        if (orgUnit == null) {
            return getDefaultCountry();
        }

        Country country = null;
        OrgUnit current = orgUnit;

        while (country == null || current != null) {

            if ((country = current.getOfficeLocationCountry()) != null) {
                return country;
            } else {
                current = current.getParent();
            }

            // Root reached
            if (current == null) {
                break;
            }
        }

        return getDefaultCountry();
    }

    /**
     * Gets the default country for all the application.
     * 
     * @return The default country.
     */
    private Country getDefaultCountry() {

        final Query q = em.createQuery("SELECT c FROM Country c WHERE c.name = :defaultName");
        // FIXME France by default
        q.setParameter("defaultName", "France");

        try {
            return (Country) q.getSingleResult();
        } catch (NoResultException e) {

            try {
                return (Country) em.createQuery("SELECT c FROM Country c").getResultList().get(0);
            } catch (Throwable e2) {
                throw new IllegalStateException("There is no country in database, unable to create a project.", e2);
            }
        }
    }

    @Override
    public void update(User user, Object entityId, PropertyMap changes) {
    }
}
