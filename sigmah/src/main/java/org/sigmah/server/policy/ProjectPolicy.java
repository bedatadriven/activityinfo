/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.policy;

import java.util.Date;

import javax.persistence.EntityManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sigmah.server.dao.Transactional;
import org.sigmah.shared.domain.Country;
import org.sigmah.shared.domain.Phase;
import org.sigmah.shared.domain.PhaseModel;
import org.sigmah.shared.domain.Project;
import org.sigmah.shared.domain.ProjectModel;
import org.sigmah.shared.domain.User;

import com.google.inject.Inject;
import org.sigmah.shared.domain.calendar.PersonalCalendar;
import org.sigmah.shared.domain.logframe.LogFrame;
import org.sigmah.shared.domain.logframe.LogFrameModel;

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
        return project.getId();
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
        project.setCountry(em.getReference(Country.class, properties.<Integer> get("countryId")));
        project.setOwner(em.getReference(User.class, user.getId()));
        project.setName(properties.<String> get("name"));
        project.setName(properties.<String> get("fullName"));
        project.setLastSchemaUpdate(new Date());
        project.setCalendarId(calendar.getId());

        // Project attributes.
        final ProjectModel model = em.getReference(ProjectModel.class, properties.<Long> get("modelId"));
        project.setProjectModel(model);
        project.setLogFrame(null);

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

        // Creates a new log frame (with a default model)
        final LogFrame logFrame = new LogFrame();
        logFrame.setParentProject(project);
        final LogFrameModel logFrameModel = new LogFrameModel();
        logFrameModel.setName("Default logical framework model");
        logFrame.setLogFrameModel(logFrameModel);

        em.persist(logFrame);

        // Updates the project for the new log frame.
        project.setLogFrame(logFrame);
        project = em.merge(project);

        return project;
    }

    @Override
    public void update(User user, Object entityId, PropertyMap changes) {

    }
}
