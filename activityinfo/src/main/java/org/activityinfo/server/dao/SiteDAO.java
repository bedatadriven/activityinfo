package org.activityinfo.server.dao;

import com.google.inject.ImplementedBy;
import org.activityinfo.server.dao.hibernate.SiteDAOImpl;
import org.activityinfo.server.domain.Site;

@ImplementedBy(SiteDAOImpl.class)
public interface SiteDAO extends DAO<Site, Integer> {


}
