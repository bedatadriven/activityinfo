package org.activityinfo.server.dao.jpa;

import com.google.inject.Inject;
import org.activityinfo.server.dao.ReportDAO;
import org.activityinfo.server.domain.ReportDefinition;
import org.activityinfo.server.domain.UserDatabase;

import javax.persistence.EntityManager;

public class ReportDAOJPA implements ReportDAO {

	private final EntityManager em;
	
	@Inject
	public ReportDAOJPA(EntityManager em) {
		this.em = em;
	}

    /* (non-Javadoc)
      * @see org.activityinfo.server.dao.ReportDAO#getXmlById(int)
      */
	public String getXmlById(int reportId) {
		
		return (String) em.createQuery("select r.xml from org.activityinfo.server.domain.ReportDefinition r where r.id=?1")
				 .setParameter(1, reportId)
				 .getSingleResult();	
	}
	
	/* (non-Javadoc)
	 * @see org.activityinfo.server.dao.ReportDAO#createReport(java.lang.Integer, java.lang.String)
	 */
	public int createReport(Integer databaseId, String reportXml) {
		
		ReportDefinition reportTemplate = new ReportDefinition();
		if(databaseId!=null) {
			reportTemplate.setDatabase( em.getReference(UserDatabase.class, databaseId ));
		}
		reportTemplate.setXml(reportXml);
		
		em.persist(reportTemplate);
		
		return reportTemplate.getId();
	}

	/* (non-Javadoc)
	 * @see org.activityinfo.server.dao.ReportDAO#updateXml(int, java.lang.String)
	 */
	public void updateXml(int id, String newXml) {
		
		ReportDefinition report = em.find(ReportDefinition.class, id);
		report.setXml(newXml);
		
	}
}
