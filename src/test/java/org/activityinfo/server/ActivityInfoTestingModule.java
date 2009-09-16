package org.activityinfo.server;

import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.AbstractModule;
import com.google.inject.servlet.RequestScoped;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.EntityManager;
/*
 * @author Alex Bertram
 */

public class ActivityInfoTestingModule extends AbstractModule {


    @Override
    protected void configure() {

    }

    @Provides
    @Singleton
    public EntityManagerFactory provideEntityManagerFactory() {
        return Persistence.createEntityManagerFactory("activityInfo");
    }

    @Provides
    @Singleton
    public EntityManager provideEntityManager(EntityManagerFactory emf) {
        return emf.createEntityManager();
    }
}
