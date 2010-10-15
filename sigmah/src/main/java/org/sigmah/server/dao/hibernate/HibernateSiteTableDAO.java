/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.dao.hibernate;

import com.google.inject.Inject;
import org.hibernate.ejb.HibernateEntityManager;
import org.hibernate.jdbc.Work;
import org.sigmah.shared.dao.*;
import org.sigmah.shared.domain.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Hibernate-friendly wrapper for the {@link org.sigmah.shared.dao.SqlSiteTableDAO}
 * that assures that connections are used and cleaned up appropriately.
 */
public class HibernateSiteTableDAO implements SiteTableDAO {

    private HibernateEntityManager entityManager;
    private SQLDialect dialect;


    @Inject
    public HibernateSiteTableDAO(HibernateEntityManager em, SQLDialect dialect) {
        this.entityManager = em;
        this.dialect = dialect;
    }

    @Override
    public <RowT> List<RowT> query(final User user, final Filter filter, final List<SiteOrder> orderings, final SiteProjectionBinder<RowT> binder, final int retrieve, final int offset, final int limit) {
        final List<RowT> list = new ArrayList<RowT>();
        entityManager.getSession().doWork(new Work() {
            @Override
            public void execute(Connection connection) throws SQLException {
                SqlSiteTableDAO dao = new SqlSiteTableDAO(connection, dialect);
                list.addAll(dao.query(user, filter, orderings, binder, retrieve, offset, limit));
            }
        });
        return list;
    }

    @Override
    public int queryCount(final User user, final Filter filter) {
        final int result[] = new int[1];
        entityManager.getSession().doWork(new Work() {
            @Override
            public void execute(Connection connection) throws SQLException {
                SqlSiteTableDAO dao = new SqlSiteTableDAO(connection, dialect);
                result[0] = dao.queryCount(user, filter);
            }
        });
        return result[0];
    }

    @Override
    public int queryPageNumber(final User user, final Filter filter, final List<SiteOrder> orderings, final int pageSize, final int siteId) {
        final int result[] = new int[1];
        entityManager.getSession().doWork(new Work() {
            @Override
            public void execute(Connection connection) throws SQLException {
                SqlSiteTableDAO dao = new SqlSiteTableDAO(connection, dialect);
                result[0] = dao.queryPageNumber(user, filter, orderings, pageSize, siteId);
            }
        });
        return result[0];
    }
}
