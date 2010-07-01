/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.policy;

import com.google.inject.Inject;
import org.sigmah.server.dao.Transactional;
import org.sigmah.server.domain.Country;
import org.sigmah.server.domain.Project;
import org.sigmah.server.domain.User;

import javax.persistence.EntityManager;
import java.util.Date;

public class ProjectPolicy  implements EntityPolicy<Project> {
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
        Project project = new Project();
        project.setName(properties.<String>get("name"));
        project.setCountry(em.getReference(Country.class, properties.<Object>get("countryId")));
        project.setOwner(em.getReference(User.class, user.getId()));

        project.setLastSchemaUpdate(new Date());


        em.persist(project);
        return project;
    }

    @Override
    public void update(User user, Object entityId, PropertyMap changes) {

    }
}
