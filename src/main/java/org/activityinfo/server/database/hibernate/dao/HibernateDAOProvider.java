/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.database.hibernate.dao;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;

import javax.persistence.EntityManager;


import com.google.inject.Inject;
import com.google.inject.Provider;

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
            @SuppressWarnings("rawtypes")
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
        return makeImplementation(daoClass, entityClass, emProvider.get());
    }

	public static <T> T makeImplementation(Class<T> daoClass, Class entityClass, EntityManager entityManager) {
		ClassLoader cl = daoClass.getClassLoader();
        return (T) Proxy.newProxyInstance(cl,
            new Class[]{daoClass},
            new DAOInvocationHandler(
                entityManager, entityClass
        ));
	}


}
