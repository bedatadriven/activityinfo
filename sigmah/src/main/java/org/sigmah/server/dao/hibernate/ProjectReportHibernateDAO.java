/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.dao.hibernate;

import com.google.inject.Inject;
import javax.persistence.EntityManager;
import org.sigmah.server.dao.ProjectReportDAO;
import org.sigmah.shared.domain.report.ProjectReport;

/**
 *
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
public class ProjectReportHibernateDAO extends GenericDAO<ProjectReport, Integer> implements ProjectReportDAO {
    @Inject
    public ProjectReportHibernateDAO(EntityManager em) {
        super(em);
    }
}
