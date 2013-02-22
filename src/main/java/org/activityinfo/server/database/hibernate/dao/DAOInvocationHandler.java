package org.activityinfo.server.database.hibernate.dao;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * Invocation Handler for
 * {@link org.activityinfo.server.database.hibernate.dao.DAO} interfaces.
 * 
 * This InvocationHandler provides implementations for
 * {@link org.activityinfo.server.database.hibernate.dao.DAO#findById(Object)
 * findById} and
 * {@link org.activityinfo.server.database.hibernate.dao.DAO#persist(Object)
 * persist}. Any other methods defined in the interface are matched to named JPA
 * queries, and their parameters as provided to the query as <em>positional</em>
 * parameters.
 * 
 * For example, given the declaration:
 * 
 * <pre>
 * &#064;Entity
 * &#064;NamedQuery(name = &quot;queryAllCountriesAlphabetically&quot;,
 *     query = &quot;select c from Country c order by c.name&quot;)
 * public class Country implements Serializable, SchemaElement {
 * }
 * </pre>
 * 
 * and
 * 
 * <pre>
 * public interface CountryDAO extends DAO&lt;Country, Integer&gt; {
 *     List&lt;Country&gt; queryAllCountriesAlphabetically();
 * }
 * </pre>
 * 
 * a call to queryAllCountriesAlphabetically will invoke the named query.
 * 
 * Other notes: If the method returns a {@link java.util.List List},
 * {@link javax.persistence.Query#getResultList()} will be invoked, otherwise
 * {@link javax.persistence.Query#getSingleResult()}
 * 
 * @author Alex Bertram
 */
public class DAOInvocationHandler implements InvocationHandler {

    private final EntityManager em;
    private final Class entityClass;

    DAOInvocationHandler(EntityManager em, Class entityClass) {
        this.em = em;
        this.entityClass = entityClass;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args)
        throws Throwable {
        if (method.getName().equals("persist")) {
            return invokePersist(args[0]);
        } else if (method.getName().equals("findById")) {
            return invokeFindById(args[0]);
        } else {
            return invokeNamedQuery(method.getName(), method, args);
        }
    }

    private Object invokeFindById(Object arg) {
        return em.find(entityClass, arg);
    }

    private Void invokePersist(Object arg) {
        em.persist(arg);
        return null;
    }

    private Object invokeNamedQuery(String name, Method method, Object[] args) {
        Query query = tryCreatingNamedQuery(name, method);
        applyPositionalParameters(args, query);

        if (method.getReturnType().equals(List.class)) {
            return query.getResultList();
        } else {
            return query.getSingleResult();
        }
    }

    private Query tryCreatingNamedQuery(String name, Method method) {
        try {
            return em.createNamedQuery(name);
        } catch (IllegalArgumentException e) {
            throw new UnsupportedOperationException(
                "The hibernate DAO proxy does not know how to handle the method "
                    +
                    method.getName());
        }
    }

    private void applyPositionalParameters(Object[] args, Query query) {
        if (args != null) {
            for (int i = 0; i != args.length; ++i) {
                query.setParameter(i + 1, args[i]);
            }
        }
    }
}
