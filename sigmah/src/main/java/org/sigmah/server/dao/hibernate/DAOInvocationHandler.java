/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.dao.hibernate;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Invocation Handler for {@link org.sigmah.server.dao.DAO} interfaces.
 * 
 * This InvocationHandler provides implementations for
 * {@link org.sigmah.server.dao.DAO#findById(Object) findById} and
 * {@link org.sigmah.server.dao.DAO#persist(Object) persist}. Any other methods defined in the interface
 * are matched to named JPA queries, and their parameters as provided to the query as <em>positional</em>
 * parameters.
 *
 * For example, given the declaration:
 *
 * <pre>
 * &#64;Entity
 * &#64;NamedQuery(name="queryAllCountriesAlphabetically",
 *             query="select c from Country c order by c.name")
 * public class Country implements Serializable, SchemaElement {  }
 * </pre>
 *
 * and
 *
 * <pre>
 * public interface CountryDAO extends DAO<Country, Integer> {
 *      List<Country> queryAllCountriesAlphabetically();
 * }
 * </pre>
 *
 * a call to queryAllCountriesAlphabetically will invoke the named query.
 *
 * Other notes:
 * If the method returns a {@link java.util.List List}, {@link javax.persistence.Query#getResultList()} will be
 * invoked, otherwise {@link javax.persistence.Query#getSingleResult()}
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
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
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
            throw new UnsupportedOperationException("The hibernate DAO proxy does not know how to handle the method " +
                    method.getName());
        }
    }

    private void applyPositionalParameters(Object[] args, Query query) {
        if(args != null) {
            for (int i = 0; i != args.length; ++i) {
                query.setParameter(i + 1, args[i]);
            }
        }
    }
}
