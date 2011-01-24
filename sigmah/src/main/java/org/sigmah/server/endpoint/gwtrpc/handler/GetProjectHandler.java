/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.endpoint.gwtrpc.handler;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dozer.Mapper;
import org.sigmah.shared.command.GetProject;
import org.sigmah.shared.command.handler.CommandHandler;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.domain.OrgUnit;
import org.sigmah.shared.domain.Project;
import org.sigmah.shared.domain.User;
import org.sigmah.shared.dto.ProjectDTO;
import org.sigmah.shared.exception.CommandException;

import com.google.inject.Inject;

/**
 * 
 * @author Denis Colliot (dcolliot@ideia.fr)
 */
public class GetProjectHandler implements CommandHandler<GetProject> {

    private final static Log LOG = LogFactory.getLog(GetProjectHandler.class);

    private final EntityManager em;
    private final Mapper mapper;

    @Inject
    public GetProjectHandler(EntityManager em, Mapper mapper) {
        this.em = em;
        this.mapper = mapper;
    }

    /**
     * Gets a project from the database and maps it into a {@link ProjectDTO}
     * object.
     * 
     * @param cmd
     *            command containing the project id
     * @param user
     *            user connected
     * 
     * @return the {@link ProjectDTO} object.
     */
    @Override
    public CommandResult execute(GetProject cmd, User user) throws CommandException {

        if (LOG.isDebugEnabled()) {
            LOG.debug("[execute] Getting projet id#" + cmd.getProjectId() + " from the database.");
        }

        final Project project = em.find(Project.class, cmd.getProjectId());

        if (project == null) {

            if (LOG.isDebugEnabled()) {
                LOG.debug("[execute] Projet id#" + cmd.getProjectId() + " doesn't exist.");
            }

            return null;
        } else {

            if (isProjectVisible(project, user)) {
                return mapper.map(project, ProjectDTO.class);
            }
            // The user cannot see this project.
            else {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("[execute] User cannot see projet id#" + cmd.getProjectId() + ".");
                }

                return null;
            }
        }
    }

    /**
     * Adds recursively all the children of an unit in a collection.
     * 
     * @param root
     *            The root unit from which the hierarchy is traversed.
     * @param units
     *            The current collection in which the units are added.
     * @param addRoot
     *            If the root must be added too.
     */
    public static void crawlUnits(OrgUnit root, Collection<OrgUnit> units, boolean addRoot) {

        if (addRoot) {
            units.add(root);
        }

        final Set<OrgUnit> children = root.getChildren();
        if (children != null) {
            for (OrgUnit child : children) {
                crawlUnits(child, units, true);
            }
        }
    }

    /**
     * Returns if the project is visible for the given user.
     * 
     * @param project
     *            The project.
     * @param user
     *            The user.
     * @return If the project is visible for the user.
     */
    public static boolean isProjectVisible(Project project, User user) {

        // Checks that the user can see this project.
        final HashSet<OrgUnit> units = new HashSet<OrgUnit>();
        GetProjectHandler.crawlUnits(user.getOrgUnitWithProfiles().getOrgUnit(), units, true);

        for (final OrgUnit partner : project.getPartners()) {
            for (final OrgUnit unit : units) {
                if (partner.getId() == unit.getId()) {
                    return true;
                }
            }
        }

        return false;
    }
}
