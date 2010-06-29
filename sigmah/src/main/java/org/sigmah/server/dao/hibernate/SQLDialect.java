/*
 * This file is part of ActivityInfo.
 *
 * ActivityInfo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ActivityInfo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ActivityInfo.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2010 Alex Bertram and contributors.
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
