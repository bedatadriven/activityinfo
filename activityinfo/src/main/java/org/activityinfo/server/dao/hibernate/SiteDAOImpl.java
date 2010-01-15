package org.activityinfo.server.dao.hibernate;

import com.google.inject.Inject;
import org.activityinfo.server.dao.SiteDAO;
import org.activityinfo.server.domain.Site;

import javax.persistence.EntityManager;

public class SiteDAOImpl extends AbstractDAO<Site, Integer>
        implements SiteDAO {

    @Inject
    public SiteDAOImpl(EntityManager em) {
        super(em);
    }
}
