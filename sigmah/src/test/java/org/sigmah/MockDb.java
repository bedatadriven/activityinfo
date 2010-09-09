/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah;



import javax.persistence.Id;

import org.sigmah.shared.dao.DAO;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;

public class MockDb {

    private List entities = new ArrayList(0);

    public MockDb() {

    }

    public Object findById(Class entityClass, Object primaryKey) {
        if (primaryKey == null) {
            return null;
        }

        for (Object entity : entities) {
            if (entity.getClass().equals(entityClass) && primaryKey.equals(getId(entity))) {
                return entity;
            }
        }
        return null;
    }

    public void persist(Object entity) {
        entities.add(entity);
    }

    private Object getId(Object t) {
        for (Method method : t.getClass().getMethods()) {
            if (method.getAnnotation(Id.class) != null) {
                try {
                    return method.invoke(t);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        throw new RuntimeException("No @Id field!");
    }

    public <S extends DAO> S getDAO(Class<S> daoSubClass) {
        ClassLoader cl = daoSubClass.getClassLoader();
        Type daoClass = daoSubClass.getGenericInterfaces()[0];
        Class persistentClass = (Class)((ParameterizedType) daoClass).getActualTypeArguments()[0];
        return (S) Proxy.newProxyInstance(cl, new Class[]{daoSubClass}, new Handler(persistentClass));
    }


    public class Handler implements InvocationHandler {

        private final Class persistentClass;

        public Handler(Class persistentClass) {
            this.persistentClass = persistentClass;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

            if (method.getName().equals("findById")) {
                return findById(persistentClass, args[0]);
            } else if (method.getName().equals("persist")) {
                persist(args[0]);
            } else if (method.getReturnType().equals(Boolean.TYPE)) {
                return false;
            }
            return null;
        }
    }

}
