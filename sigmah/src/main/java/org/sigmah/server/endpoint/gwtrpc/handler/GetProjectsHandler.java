/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.endpoint.gwtrpc.handler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dozer.Mapper;
import org.sigmah.server.domain.Project;
import org.sigmah.shared.command.GetProjects;
import org.sigmah.shared.command.handler.CommandHandler;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.command.result.ProjectListResult;
import org.sigmah.shared.domain.Country;
import org.sigmah.shared.domain.User;
import org.sigmah.shared.dto.CountryDTO;
import org.sigmah.shared.dto.ProjectDTO;
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
        /* Initialization */
        List<ProjectDTO> projectDTOList = new ArrayList<ProjectDTO>();

        /* Recovery of the projects list from the DB */
        List<Project> projects;

        final List<CountryDTO> countriesDTO = cmd.getCountries();

        if (countriesDTO == null) {

            if (LOG.isDebugEnabled()) {
                LOG.debug("[execute] No country specified, gets all projects.");
            }

            projects = em.createQuery("select p from Project p order by p.name").getResultList();
        } else {
            if (countriesDTO.size() > 0) {
                final ArrayList<Country> countries = new ArrayList<Country>();

                for (final CountryDTO countryDTO : countriesDTO) {
                    countries.add(mapper.map(countryDTO, Country.class));

                    if (LOG.isDebugEnabled()) {
                        LOG.debug("[execute] Gets projects in country " + countryDTO.getName() + ".");
                    }
                }

                Query q = em.createQuery("SELECT p FROM Project p WHERE p.country IN (:countryList)");
                q.setParameter("countryList", countries);
                projects = q.getResultList();
            } else {
                projects = Collections.emptyList();
            }
        }

        /* Transformation into DTO objects */
        for (Project project : projects) {
            ProjectDTO p = mapper.map(project, ProjectDTO.class);
            projectDTOList.add(p);
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("[execute] Found " + projects.size() + " project(s).");
        }

        return new ProjectListResult(projectDTOList);

    }
}
