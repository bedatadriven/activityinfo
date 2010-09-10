/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */
package org.sigmah.client.offline.dao;

import javax.persistence.EntityManager;

import org.sigmah.shared.dao.DAO;

import com.google.inject.Inject;

/**
 * A base class for off-line DAOs.
 * 
 * @author jon
 *
 * @param <T>
 * @param <K>
 */
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