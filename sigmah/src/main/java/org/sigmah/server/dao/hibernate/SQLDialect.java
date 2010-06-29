/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.dao.hibernate;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.hibernate.ejb.HibernateEntityManager;
import org.hibernate.jdbc.Work;

import javax.persistence.EntityManagerFactory;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.MessageFormat;

/**
 * Utility class that detects and caches required properties of the
 * SQL dialect in use.
 *
 * Note: Obviously hibernate manages SQLDialects as well, but I can't figure out
 * how to access the Dialect Object from the JPA runtime, and it doesn't provide
 * info on the quarter() function anyway.
 *
 * @author Alex Bertram
 */
@Singleton
public class SQLDialect {
    private final EntityManagerFactory emf;

    private MessageFormat quarterFunctionFormat;

    @Inject
    public SQLDialect(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public String formatQuarterFunction(String column) {
        if(quarterFunctionFormat == null) {
            HibernateEntityManager hem = (HibernateEntityManager) emf.createEntityManager();
            hem.getSession().doWork(new Work() {
                @Override
                public void execute(Connection connection) throws SQLException {
                    if(isMicrosoftSQL(connection)) {
                        quarterFunctionFormat = new MessageFormat("FLOOR((MONTH({0})-1)/3)+1");
                    } else {
                        quarterFunctionFormat = new MessageFormat("QUARTER({0})");
                    }
                }
            });
            hem.close();
        }
        return quarterFunctionFormat.format(new String[] { column });
    }

    private boolean isMicrosoftSQL(Connection connection) throws SQLException {
        String dbName = connection.getMetaData().getDatabaseProductName();
        return dbName.equals("Microsoft SQL Server");
    }
}
