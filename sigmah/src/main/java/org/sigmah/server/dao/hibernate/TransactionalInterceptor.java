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
import com.google.inject.Injector;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

/**
 * MethodInterceptor that implements declarative transaction management.
 * See {@link org.sigmah.server.dao.Transactional}
 *
 * @author Alex Bertram
 */
public class TransactionalInterceptor implements MethodInterceptor {

    private Injector injector;

    @Inject
    public void setInjector(Injector injector) {
        this.injector = injector;
    }

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        EntityManager em = injector.getInstance(EntityManager.class);
        EntityTransaction tx = em.getTransaction();

        //allow joining of transactions if there is an enclosing @Transactional method
        if (tx.isActive()) {
            return methodInvocation.proceed();
        }

        tx.begin();
        Object result = attemptInvocation(methodInvocation, tx);

        // everything was normal so commit the txn (do not move into try block as it interferes
        // with the advised method's throwing semantics)
        tx.commit();
        return result;
    }

    private Object attemptInvocation(MethodInvocation methodInvocation, EntityTransaction tx) throws Throwable {
        try {
            return methodInvocation.proceed();
        } catch (Exception e) {
            // rollback database to original state
            tx.rollback();

            //propagate whatever exception is thrown anyway
            throw e;
        }
    }
}
