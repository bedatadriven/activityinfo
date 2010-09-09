/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.test;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Provider;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.sigmah.server.dao.hibernate.HibernateModule;
import org.sigmah.server.endpoint.gwtrpc.handler.GetSitesHandlerHibernate;
import org.sigmah.server.util.DozerMapper;
import org.sigmah.shared.command.handler.GetSitesHandler;
import org.sigmah.shared.dto.DTOMapper;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class MockHibernateModule extends HibernateModule {
    private static EntityManagerFactory emf = null;

    
    @Override
    protected void configureEmf() {
        bind(EntityManagerFactory.class).toProvider(new Provider<EntityManagerFactory>() {
            @Override
            public EntityManagerFactory get() {
                // we are assuming that the tests do not affect the database schema, so there is no
                // need to restart hiberate for each test class, and we save quite a bit of time
                if (emf == null) {
                    emf = Persistence.createEntityManagerFactory(getPersistenceUnitName());
                    System.err.println("GUICE: EntityManagerFACTORY created");
                }
                return emf;
            }
        }).in(Singleton.class);
    }

    private String getPersistenceUnitName() {
        String specifiedUnitName = System.getProperty("activityinfo.pu");
        return specifiedUnitName == null ? "h2-test" : specifiedUnitName;
    }

    @Override
    protected void configureEm() {
        bind(EntityManager.class).toProvider(EntityManagerProvider.class)
                .in(TestScoped.class);
    }
    



}
