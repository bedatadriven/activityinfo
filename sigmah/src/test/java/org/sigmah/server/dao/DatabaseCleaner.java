/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.dao;

import com.google.inject.Inject;
import org.hibernate.ejb.HibernateEntityManager;
import org.hibernate.jdbc.Work;
import org.sigmah.server.domain.PersistentClasses;
import org.sigmah.shared.dao.SQLDialect;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Table;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseCleaner {

    private final EntityManager em;
    private final SQLDialect dialect;

    @Inject
    public DatabaseCleaner(EntityManager em, SQLDialect dialect) {
        this.em = em;
        this.dialect = dialect;
    }

    public void clean() {
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        HibernateEntityManager hem = (HibernateEntityManager) em;
        hem.getSession().doWork(new Work() {
            @Override
            public void execute(Connection connection) throws SQLException {
                Statement stmt = connection.createStatement();

                if(dialect.isPossibleToDisableReferentialIntegrity()) {
                    stmt.execute(dialect.disableReferentialIntegrityStatement(true));
                }

                executeSafe(stmt, removeAllRows("AttributeGroupInActivity"));
                executeSafe(stmt, removeAllRows("PartnerInDatabase"));
                executeSafe(stmt, removeAllRows("LocationAdminLink"));
                executeSafe(stmt, removeAllRows("phase_model_sucessors"));

                for(Class<?> entityClass : PersistentClasses.LIST) {
                    executeSafe(stmt, removeAllRows(tableName(entityClass)));
                }

                if(dialect.isPossibleToDisableReferentialIntegrity()) {
                    stmt.execute(dialect.disableReferentialIntegrityStatement(false));
                }
            }
        });

        tx.commit();
    }

    private void executeSafe(Statement stmt, String sql) throws SQLException {
        try {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("DatabaseCleaner: SQL failed: " + sql + ", " + e.getMessage());
        }
    }

    private String removeAllRows(String tableName) {
        return "delete from " + tableName;
    }

    private String tableName(Class<?> entityClass) {
        Table annotation = entityClass.getAnnotation(Table.class);
        if(annotation == null || annotation.name().length() == 0) {
            return entityClass.getSimpleName();
        } else {
            return annotation.name();
        }
    }

}

