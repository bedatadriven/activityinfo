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

package org.activityinfo;

import org.activityinfo.server.dao.DAO;

import javax.persistence.Id;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

public class MockDAO<T, K> implements DAO<T, K> {

    private List<T> entities = new ArrayList<T>(0);
    private Class<T> persistentClass;

    public MockDAO() {
        this.persistentClass = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Override
    public T findById(K primaryKey) {
        if (primaryKey == null)
            return null;

        for (T entity : entities) {
            if (primaryKey.equals(getId(entity)))
                return entity;
        }
        return null;
    }


    @Override
    public void persist(T entity) {
        entities.add(entity);
    }

    private K getId(T t) {
        for (Method method : persistentClass.getMethods()) {
            if (method.getAnnotation(Id.class) != null) {
                try {
                    return (K) method.invoke(t);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        throw new RuntimeException("No @Id field!");
    }

    public <S extends DAO<T, K>> S cast(Class<S> daoType) {
        ClassLoader cl = daoType.getClassLoader();
        return (S) Proxy.newProxyInstance(cl, new Class[]{daoType}, new Handler());
    }

    public class Handler implements InvocationHandler {

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

            if (method.getName().equals("findById")) {
                return findById((K) args[0]);
            } else if (method.getName().equals("persist")) {
                persist((T) args[0]);
            }
            return null;
        }
    }

}
