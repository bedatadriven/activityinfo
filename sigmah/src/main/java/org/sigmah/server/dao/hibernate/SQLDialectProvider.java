/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.dao.hibernate;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import org.hibernate.ejb.HibernateEntityManager;
import org.hibernate.jdbc.Work;
import org.sigmah.shared.dao.SQLDialect;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.sql.Connection;
import java.sql.SQLException;

@Singleton
public class SQLDialectProvider implements Provider<SQLDialect> {

    private SQLDialect dialect;

    @Inject
    public SQLDialectProvider(EntityManagerFactory emf)  {
        HibernateEntityManager hem = (HibernateEntityManager) emf.createEntityManager();
        init(hem);
        hem.close();        
    }

    private SQLDialectProvider() {

    }

    public static SQLDialect from(EntityManager entityManager) {
        SQLDialectProvider provider = new SQLDialectProvider();
        provider.init((HibernateEntityManager) entityManager);
        return provider.get();
    }

    private void init(HibernateEntityManager hem) {
        hem.getSession().doWork(new Work() {
            @Override
            public void execute(Connection connection) throws SQLException {
                String dbName = connection.getMetaData().getDatabaseProductName();
                if(dbName.equals(MSSQLDialect.PRODUCT_NAME)) {
                    dialect = new MSSQLDialect();
                } else if(dbName.equals(PostgresDialect.PRODUCT_NAME)) {
                    dialect = new PostgresDialect();
                } else if(dbName.equals(H2Dialect.PRODUCT_NAME)) {
                    dialect = new H2Dialect();
                } else if(dbName.equals(MySQLDialect.PRODUCT_NAME)) {
                    dialect = new MySQLDialect();
                } else {
                    dialect = new DefaultDialect();
                }
            }
        });
    }


    @Override
    public SQLDialect get() {
        return dialect;
    }

    private static class DefaultDialect implements SQLDialect {
        @Override
        public String yearFunction(String column) {
            return "YEAR(" + column + ")";
        }

        @Override
        public String monthFunction(String column) {
            return "MONTH(" + column + ")";
        }

        @Override
        public String quarterFunction(String column) {
            return "QUARTER(" + column + ")";
        }

        @Override
        public boolean isPossibleToDisableReferentialIntegrity() {
            return false;
        }

        @Override
        public String disableReferentialIntegrityStatement(boolean disabled) {
            throw new UnsupportedOperationException();
        }
    }

    private static class MSSQLDialect extends DefaultDialect {

        public static final String PRODUCT_NAME = "Microsoft SQL Server";

        @Override
        public String quarterFunction(String column) {
            return "FLOOR((MONTH(" + column + ")-1)/3)+1";
        }
    }

    private static class MySQLDialect extends DefaultDialect {

        public static final String PRODUCT_NAME = "MySQL";

        @Override
        public boolean isPossibleToDisableReferentialIntegrity() {
            return true;
        }

        @Override
        public String disableReferentialIntegrityStatement(boolean disabled) {
            return "SET foreign_key_checks = " + (disabled ? "0" : "1");
        }
    }

    private static class PostgresDialect extends DefaultDialect {

        public static final String PRODUCT_NAME = "PostgreSQL";

        @Override
        public String yearFunction(String column) {
            return "EXTRACT(year FROM (" + column + "))";
        }

        @Override
        public String monthFunction(String column) {
            return "EXTRACT(month FROM (" + column + "))";
        }

        @Override
        public String quarterFunction(String column) {
            return "EXTRACT(quarter FROM (" + column + "))";
        }
    }

    private static class H2Dialect extends DefaultDialect {

        public static final String PRODUCT_NAME = "H2";

        @Override
        public boolean isPossibleToDisableReferentialIntegrity() {
            return true;
        }

        @Override
        public String disableReferentialIntegrityStatement(boolean disabled) {
            return "SET REFERENTIAL_INTEGRITY " + (disabled ? "FALSE" : "TRUE");
        }
    }


}
