/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.endpoint.gwtrpc.handler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dozer.Mapper;
import org.sigmah.shared.command.GetProjects;
import org.sigmah.shared.command.handler.CommandHandler;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.command.result.ProjectListResult;
import org.sigmah.shared.domain.Country;
import org.sigmah.shared.domain.Project;
import org.sigmah.shared.domain.ProjectModelType;
import org.sigmah.shared.domain.User;
import org.sigmah.shared.dto.CountryDTO;
import org.sigmah.shared.dto.ProjectDTOLight;
import org.sigmah.shared.exception.CommandException;

import com.google.inject.Inject;

public class GetProjectsHandler implements CommandHandler<GetProjects> {

    private final EntityManager em;
    private final Mapper mapper;

    private final static Log LOG = LogFactory.getLog(GetProjectsHandler.class);

    @Inject
    public GetProjectsHandler(EntityManager em, Mapper mapper) {
        this.em = em;
        this.mapper = mapper;
    }

    /**
     * Gets the projects list from the database.
     * 
     * @param cmd
     * @param user
     * 
     * @return a {@link CommandResult} object containing the
     *         {@link ProjectListResult} object.
     */
    @SuppressWarnings("unchecked")
    @Override
    public CommandResult execute(GetProjects cmd, User user) throws CommandException {

        if (LOG.isDebugEnabled()) {
            LOG.debug("[execute] Gets projects: " + cmd + ".");
        }

        List<Project> projects;
        final List<CountryDTO> countriesDTO = cmd.getCountries();
        final ProjectModelType modelType = cmd.getModelType();

        // Filters by country.
        if (countriesDTO == null) {

            if (LOG.isDebugEnabled()) {
                LOG.debug("[execute] No country specified, gets all projects.");
            }

            projects = em.createQuery("SELECT p FROM Project p ORDER BY p.name").getResultList();
        } else {
            if (countriesDTO.size() > 0) {

                if (LOG.isDebugEnabled()) {
                    LOG.debug("[execute] Gets projects for " + countriesDTO.size() + " countries.");
                }

                final ArrayList<Country> countries = new ArrayList<Country>();

                for (final CountryDTO countryDTO : countriesDTO) {

                    if (LOG.isDebugEnabled()) {
                        LOG.debug("[execute] Map country name " + countryDTO + ".");
                    }

                    final Country country = mapper.map(countryDTO, Country.class);

                    if (LOG.isDebugEnabled()) {
                        LOG.debug("[execute] Gets projects in country " + country + ".");
                    }

                    countries.add(country);
                }

                Query q = em.createQuery("SELECT p FROM Project p WHERE p.country IN (:countryList)");
                q.setParameter("countryList", countries);
                projects = q.getResultList();

                if (LOG.isDebugEnabled()) {
                    LOG.debug("[execute] Executes selection query.");
                }

            } else {
                projects = Collections.emptyList();
            }
        }

        // Filters by model type.
        if (modelType != null) {

            if (LOG.isDebugEnabled()) {
                LOG.debug("[execute] Filter project by model type '" + modelType + "'.");
            }

            for (final ListIterator<Project> it = projects.listIterator(); it.hasNext();) {
                final Project p = it.next();
                if (p.getProjectModel().getVisibility(user.getOrganization()) != modelType) {
                    it.remove();
                }
            }
        } else {

            if (LOG.isDebugEnabled()) {
                LOG.debug("[execute] No model type specified, no filter applied.");
            }
        }

        // Mapping into DTO objects
        final ArrayList<ProjectDTOLight> projectDTOList = new ArrayList<ProjectDTOLight>();
        for (Project project : projects) {
            final ProjectDTOLight p = mapper.map(project, ProjectDTOLight.class);
            projectDTOList.add(p);
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("[execute] Found " + projects.size() + " project(s).");
        }

        return new ProjectListResult(projectDTOList);

    }
}
