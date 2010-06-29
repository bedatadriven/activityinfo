/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.dao.hibernate;

import org.sigmah.server.dao.DAO;

import javax.persistence.EntityManager;
import java.lang.reflect.ParameterizedType;

/**
 * Generic DAO implementation which provides implementation of boiler-plate methods using
 * generics.
 *
 * @param <T> Entity Type
 * @param <K> Entity Key Type
 *
 * @author Alex Bertram
 */
public abstract class GenericDAO<T, K> implements DAO<T, K> {
    private final Class<T> persistentClass;
    protected final EntityManager em;

    protected GenericDAO(EntityManager em) {
        this.em = em;
        this.persistentClass = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public void persist(T entity) {
        this.em.persist(entity);
    }

    public T findById(K primaryKey) {
        return this.em.find(persistentClass, primaryKey);
    }
}
