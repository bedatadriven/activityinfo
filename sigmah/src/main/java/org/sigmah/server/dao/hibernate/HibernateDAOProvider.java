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
import com.google.inject.Provider;
import org.sigmah.server.dao.DAO;

import javax.persistence.EntityManager;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;

/**
 * Provider which dynamically implements an a subclass of DAO
 *
 * @param <T> The type of the DAO to provide
 *
 * @author Alex Bertram
 */
public class HibernateDAOProvider<T> implements Provider<T> {

    private Provider<EntityManager> emProvider;

    private final Class<T> daoClass;
    private final Class<T> entityClass;

    public HibernateDAOProvider(Class<T> daoClass) {
        this.daoClass = daoClass;
        this.entityClass = findDAOInterface();
    }

    @Inject
    public void setEntityManagerProvider(Provider<EntityManager> emProvider) {
        this.emProvider = emProvider;
    }

    private Class findDAOInterface() {
        for (Type interfaceType : daoClass.getGenericInterfaces()) {
            ParameterizedType genericType = (ParameterizedType) interfaceType;
            Class interfaceClass = (Class) genericType.getRawType();
            if (interfaceClass.equals(DAO.class)) {
                return (Class) genericType.getActualTypeArguments()[0];
            }
        }
        throw new UnsupportedOperationException("Dao class " + daoClass.getSimpleName()
                + " MUST implement " + DAO.class.getName());
    }

    @Override
    public T get() {
        ClassLoader cl = daoClass.getClassLoader();
        return (T) Proxy.newProxyInstance(cl,
            new Class[]{daoClass},
            new DAOInvocationHandler(
                emProvider.get(), entityClass
        ));
    }


}
