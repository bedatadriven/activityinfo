

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

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.createStrictMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertNotNull;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.activityinfo.server.database.hibernate.dao.TransactionModule;
import org.activityinfo.server.database.hibernate.dao.Transactional;
import org.junit.Test;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class TransactionalInterceptorTest {

    @Test
    public void testSuccessfulCase() {
        EntityTransaction tx = createMock(EntityTransaction.class);
        expect(tx.isActive()).andReturn(false);
        tx.begin();
        tx.commit();
        replay(tx);

        MockClass mock = getMockInstance(tx);

        mock.succeedsWithoutException();

        verify(tx);
    }


    @Test
    public void testFailedCase() {
        EntityTransaction tx = createMock(EntityTransaction.class);
        expect(tx.isActive()).andReturn(false);
        tx.begin();
        tx.rollback();
        replay(tx);

        MockClass mock = getMockInstance(tx);

        RuntimeException rte = null;
        try {
            mock.throwsRuntimeException();
        } catch (RuntimeException e) {
            rte = e;
        }

        assertNotNull("exception is propagated", rte);

        verify(tx);
    }


    @Test
    public void testNestedSuccessful() {
        EntityTransaction tx = createStrictMock(EntityTransaction.class);
        expect(tx.isActive()).andReturn(false);
        tx.begin();
        expect(tx.isActive()).andReturn(true);
        tx.commit();
        replay(tx);

        MockClass mock = getMockInstance(tx);

        mock.successfulNestedTransactions();

        verify(tx);
    }

    @Test
    public void testNestedUnsuccessful() {
        EntityTransaction tx = createStrictMock(EntityTransaction.class);
        expect(tx.isActive()).andReturn(false);
        tx.begin();
        expect(tx.isActive()).andReturn(true);
        tx.rollback();
        replay(tx);

        MockClass mock = getMockInstance(tx);

        RuntimeException rte = null;
        try {
            mock.nestedTransactionWithFailureOnSecondLevel();
        } catch (RuntimeException e) {
            rte = e;
        }
        assertNotNull("exception is propagated", rte);


        verify(tx);
    }

    private MockClass getMockInstance(EntityTransaction tx) {
        Injector injector = Guice.createInjector(new MockEntityManagerModule(tx));
        MockClass mock = injector.getInstance(MockClass.class);
        return mock;
    }


    public static class MockClass {

        @Transactional
        public void succeedsWithoutException() {
        }

        @Transactional
        public void throwsRuntimeException() {
            throw new RuntimeException();
        }

        @Transactional
        public void successfulNestedTransactions() {
            succeedsWithoutException();
        }

        @Transactional
        public void nestedTransactionWithFailureOnSecondLevel() {
            throwsRuntimeException();
        }
    }

    private static class MockEntityManagerModule extends AbstractModule {
        private EntityManager em;

        public MockEntityManagerModule(EntityTransaction tx) {
            em = createMock(EntityManager.class);
            expect(em.getTransaction()).andReturn(tx).anyTimes();
            replay(em);
        }

		@Override
		protected void configure() {
			bind(EntityManager.class).toInstance(em);	
			install(new TransactionModule());
		}
    }
}
