package org.sigmah.client.offline.dao;

import javax.persistence.EntityManager;

import org.sigmah.shared.dao.DAO;

import com.google.inject.Inject;

public abstract class OfflineDAO<T, K> implements DAO<T, K> {
    protected final EntityManager em;
    
    @Inject
    protected OfflineDAO(EntityManager em) {
        this.em = em;
    }

    public void persist(T entity) {
        this.em.persist(entity);
    }
}