package org.activityinfo.server;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.activityinfo.server.report.generator.MapIconPath;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * @author Alex Bertram
 */
public class ActivityInfoTestingModule extends AbstractModule {


    @Override
    protected void configure() {
        bind(String.class).annotatedWith(MapIconPath.class)
                        .toInstance("war/mapicons/");
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
