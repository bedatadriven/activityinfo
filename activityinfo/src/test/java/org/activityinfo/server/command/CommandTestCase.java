package org.activityinfo.server.command;


import com.google.inject.*;
import org.activityinfo.server.ActivityInfoModule;
import org.activityinfo.server.DbUnitTestCase;
import org.activityinfo.server.command.handler.CommandHandler;
import org.activityinfo.server.command.handler.HandlerUtil;
import org.activityinfo.server.dao.hibernate.DataModule;
import org.activityinfo.server.domain.DomainFilters;
import org.activityinfo.server.domain.User;
import org.activityinfo.shared.command.Command;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.exception.CommandException;
import org.junit.AfterClass;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public abstract class CommandTestCase extends DbUnitTestCase {

    protected class TestModule extends AbstractModule {

        @Override
        protected void configure() {
//
//			bind(SchemaDAO.class).to(SchemaDAOJPA.class);
//            bind(SiteDAO.class).to(SiteDAOJPA.class);
//            bind(AdminDAO.class).to(AdminDAOJPA.class);
        }

        @Provides
        @Singleton
        EntityManagerFactory provideEmf() {
            return DbUnitTestCase.emf;
        }

        @Provides
        @Singleton
        EntityManager provideEm(EntityManagerFactory emf) {
            return emf.createEntityManager();
        }
    }

    protected Injector injector;

    private int currentUserId = 0;


    protected void setUser(int userId) {
        currentUserId = userId;
    }

    protected <T extends CommandResult> T execute(Command<T> command) throws CommandException {

        injector = Guice.createInjector(new TestModule(), new ActivityInfoModule(), new DataModule());


        EntityManager em = injector.getInstance(EntityManager.class);
        em.getTransaction().begin();

        User user = em.find(User.class, currentUserId);
        assert user != null;

        DomainFilters.applyUserFilter(user, em);

        Class<? extends CommandHandler> executorClass = HandlerUtil.executorForCommand(command);
        CommandHandler<Command<T>> handler = (CommandHandler<Command<T>>)
                injector.getInstance(executorClass);


        T result = (T) handler.execute(command, user);

        em.getTransaction().commit();
        em.close();


        return result;

    }

    @AfterClass
    public static void killEmf() {
        emf.close();
    }


}
