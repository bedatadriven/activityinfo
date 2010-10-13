/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.test;

import com.google.inject.Provider;
import com.google.inject.Singleton;
import org.hibernate.ejb.Ejb3Configuration;
import org.sigmah.server.dao.hibernate.HibernateModule;
import org.sigmah.server.domain.PersistentClasses;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class MockHibernateModule extends HibernateModule {
    private static EntityManagerFactory emf = null;


    @Override
    protected void configureEmf() {
        bind(EntityManagerFactory.class).toProvider(new Provider<EntityManagerFactory>() {
            @Override
            public EntityManagerFactory get() {
                // we are assuming that the tests do not affect the database schema, so there is no
                // need to restart hibernate for each test class, and we save quite a bit of time
                if (emf == null) {
                    // we want to avoid a full scan of WEB-INF/classes during hibernate
                    // startup for tests. So we avoid the normal persistence.xml config
                    // and build the configuration manually.
                    Ejb3Configuration cfg = new Ejb3Configuration();
                    for(Class entityClass : PersistentClasses.LIST) {
                        cfg.addAnnotatedClass(entityClass);
                    }
                    emf = cfg.configure(getConfigurationFilePath()) //add a regular hibernate.cfg.xml
                            .buildEntityManagerFactory(); //Create the entity manager factory
                    System.err.println("GUICE: EntityManagerFACTORY created");
                }
                return emf;
            }
        }).in(Singleton.class);
    }

    private String getConfigurationFilePath() {
        String db = "h2";
        if(System.getProperty("testDatabase") != null) {
            db = System.getProperty("testDatabase");
        }
        String cfgFile = "/hibernate-tests-" + db + ".cfg.xml";
        if( getClass().getResourceAsStream(cfgFile) == null ) {
            throw new Error("Cannot find hibernate cfg file for testing: " + cfgFile);
        }
        return cfgFile;
    }

    @Override
    protected void configureEm() {
        bind(EntityManager.class).toProvider(EntityManagerProvider.class)
                .in(TestScoped.class);
    }
}
