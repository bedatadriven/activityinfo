/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.database.hibernate.dao;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.createStrictMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertNotNull;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

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
