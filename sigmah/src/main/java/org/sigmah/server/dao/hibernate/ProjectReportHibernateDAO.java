/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.dao.hibernate;

import com.google.inject.Inject;
import javax.persistence.EntityManager;
import org.sigmah.server.dao.ProjectReportDAO;
import org.sigmah.shared.domain.report.ProjectReport;
import org.sigmah.shared.domain.report.ProjectReportModel;
import org.sigmah.shared.domain.report.RichTextElement;

/**
 *
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
public class ProjectReportHibernateDAO implements ProjectReportDAO {
    protected final EntityManager em;
    
    @Inject
    public ProjectReportHibernateDAO(EntityManager em) {
        this.em = em;
    }

    @Override
    public void merge(RichTextElement element) {
        em.merge(element);
    }

    @Override
    public ProjectReportModel findModelById(Integer id) {
        return em.find(ProjectReportModel.class, id);
    }

    @Override
    public RichTextElement findRichTextElementById(Integer id) {
        return em.find(RichTextElement.class, id);
    }

    @Override
    public void persist(ProjectReport report) {
        em.persist(report);
    }

    @Override
    public ProjectReport findReportById(Integer id) {
        return em.find(ProjectReport.class, id);
    }

    @Override
    public void merge(ProjectReport report) {
        em.merge(report);
    }
}
