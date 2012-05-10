package org.activityinfo.server.database.hibernate;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class EntityManagerProvider implements Provider<EntityManager> {
    private EntityManagerFactory emf;

    @Inject
    public EntityManagerProvider(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public EntityManager get() {
        return emf.createEntityManager();
    }
}