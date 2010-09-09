/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.dao;

/**
 * Defines common methods of Data Access Objects (DAOs)
 *
 * @param <T> The entity type (should be annotated with @Entity)
 * @param <K> The type of the entity's @Id property
 *
 * @author Alex Bertram
 */
public interface DAO<T, K> {

    /**
     * Persists the entity to the datastore.
     *
     * Same as enitityManager.persist(entity)
     *
     * @param entity
     */
    void persist(T entity);

    /**
     * @param primaryKey
     * @return the entity with the specified key or null if no such entity exists
     */
    T findById(K primaryKey);
}
