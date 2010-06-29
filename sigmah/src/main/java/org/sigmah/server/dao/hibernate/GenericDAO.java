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
