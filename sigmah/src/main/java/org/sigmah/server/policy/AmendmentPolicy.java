/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.policy;

import com.google.inject.Inject;
import javax.persistence.EntityManager;
import org.sigmah.server.dao.Transactional;
import org.sigmah.shared.domain.Amendment;
import org.sigmah.shared.domain.Project;
import org.sigmah.shared.domain.User;

/**
 * Creates and updates project amendments.
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
public class AmendmentPolicy implements EntityPolicy<Amendment> {

    private final EntityManager em;

    @Inject
    public AmendmentPolicy(EntityManager em) {
        this.em = em;
    }

    @Override
    public Object create(User user, PropertyMap properties) {
        return createAmendment(properties).getId();
    }

    public Amendment createAmendment(PropertyMap properties) {
        Integer projectId = (Integer) properties.get("projectId");
        final Project project = em.find(Project.class, projectId);

        return createAmendment(project);
    }

    @Transactional
    public Amendment createAmendment(Project project) {
        final Amendment amendment = new Amendment();

        amendment.setParentProject(project);
        amendment.setLogFrame(project.getLogFrame().copy());
        amendment.setState(project.getAmendmentState());
        amendment.setVersion(project.getAmendmentVersion());
        amendment.setRevision(project.getAmendmentRevision());

        em.merge(amendment);

        // TODO: run through every flexible element attached to the project [...]
        // and saves the last history token in the values property.
        // @see GetHistoryHandler

        // Updating the current amendment version/revision number
        int version  = project.getAmendmentVersion();
        int revision = project.getAmendmentRevision();

        if(project.getAmendmentState() == Amendment.State.ACTIVE) {
            version++;
            revision = 1;
        } else {
            revision++;
        }
        
        // REM: Amendement en cours : toujours ACTIF donc toujours version++
        // Pour révision : 1 par défaut, si amendement précédent est en état REJECTED
        // mettre rev à précédent + 1

        // Updating the project
        project.setAmendmentState(Amendment.State.DRAFT);
        project.setAmendmentVersion(version);
        project.setAmendmentRevision(revision);
        em.merge(project);

        return amendment;
    }



    @Override
    public void update(User user, Object entityId, PropertyMap changes) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
