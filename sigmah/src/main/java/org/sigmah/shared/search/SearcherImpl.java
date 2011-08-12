package org.sigmah.shared.search;

import javax.persistence.Query;

import org.hibernate.ejb.HibernateEntityManager;
import org.sigmah.shared.dao.Filter;
import org.sigmah.shared.report.model.DimensionType;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

public class SearcherImpl implements Searcher {
	HibernateEntityManager em;
	
	@Inject
	public SearcherImpl(HibernateEntityManager em) {
		this.em = em;
	}


	@Override
	public void search(String testQuery, AsyncCallback<Filter> callback) {
		Filter resultFilter = new Filter();
		
		Query q = em.createNativeQuery("select adminentityid from adminentity where name like ?");
		q.setParameter(1, "%" + testQuery + "%");
		for (Object result : q.getResultList()) {
			resultFilter.addRestriction(DimensionType.AdminLevel, (Integer)result); 
		}
		
		//return resultFilter;
	}

}
