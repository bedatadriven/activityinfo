/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.database.hibernate.dao;


import java.lang.reflect.ParameterizedType;

import javax.persistence.EntityManager;


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
    private final EntityManager em;

    protected GenericDAO(EntityManager em) {
        this.em = em;
        this.persistentClass = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }

    protected final EntityManager getEntityManager() {
    	return em;
    }
    
    @Override
	public void persist(T entity) {
        this.em.persist(entity);
    }

    @Override
	public T findById(K primaryKey) {
        return this.em.find(persistentClass, primaryKey);
    }
}
