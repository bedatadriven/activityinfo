/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.policy;

import com.google.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import org.sigmah.server.dao.Transactional;
import org.sigmah.shared.domain.Amendment;
import org.sigmah.shared.domain.PhaseModel;
import org.sigmah.shared.domain.Project;
import org.sigmah.shared.domain.User;
import org.sigmah.shared.domain.element.FlexibleElement;
import org.sigmah.shared.domain.history.HistoryToken;
import org.sigmah.shared.domain.layout.LayoutConstraint;
import org.sigmah.shared.domain.layout.LayoutGroup;

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
        amendment.setDate(new Date());

        // Running through every flexible element attached to the project [...]
        // and saving the last history token in the values property.
        // @see GetHistoryHandler
        final ArrayList<HistoryToken> historyTokens = new ArrayList<HistoryToken>();

        // Looking for all groups
        final ArrayList<LayoutGroup> groups = new ArrayList<LayoutGroup>();
        for(final PhaseModel phaseModel : project.getProjectModel().getPhases())
            groups.addAll(phaseModel.getLayout().getGroups());
        groups.addAll(project.getProjectModel().getProjectDetails().getLayout().getGroups());

        // Iterating on groups
        for(final LayoutGroup group : groups) {
            for(final LayoutConstraint constraint : group.getConstraints()) {
                final FlexibleElement element = constraint.getElement();
                if(element.isAmendable() != null && element.isAmendable()) {
                    // The value of the current flexible element must be saved.
                    final Query maxDateQuery = em.createQuery("SELECT MAX(h.date) FROM HistoryToken h WHERE h.elementId = :elementId AND h.projectId = :projectId");
                    maxDateQuery.setParameter("projectId", project.getId());
                    maxDateQuery.setParameter("elementId", element.getId());

                    try {
                        final Date maxDate = (Date) maxDateQuery.getSingleResult();

                        final Query query = em.createQuery("SELECT h FROM HistoryToken h WHERE h.elementId = :elementId AND h.projectId = :projectId AND h.date = :maxDate");
                        query.setParameter("projectId", project.getId());
                        query.setParameter("elementId", element.getId());
                        query.setParameter("maxDate", maxDate);

                        final HistoryToken token = (HistoryToken) query.getSingleResult();
                        historyTokens.add(token);

                    } catch(NoResultException e) {
                        // There is no history token for the given element. No action.
                    }
                }
            }
        }

        amendment.setValues(historyTokens);

        return amendment;
    }

    @Override
    public void update(User user, Object entityId, PropertyMap changes) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
