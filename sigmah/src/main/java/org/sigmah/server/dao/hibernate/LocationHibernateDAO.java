/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.dao.hibernate;

import java.util.ListIterator;

import com.google.inject.Inject;

import org.sigmah.server.dao.LocationDAO;
import org.sigmah.shared.domain.AdminEntity;
import org.sigmah.shared.domain.Location;

import javax.persistence.EntityManager;

/**
 * @author Alex Bertram
 */
public class LocationHibernateDAO extends GenericDAO<Location, Integer> implements LocationDAO {

    @Inject
    public LocationHibernateDAO(EntityManager em) {
        super(em);
    }

    @Override
    public void updateAdminMembership(int locationId, int adminLevelId, int adminEntityId) {
        removeExistingRow(locationId, adminLevelId);
        addRow(locationId, adminEntityId);
    }

    @Override
    public void addAdminMembership(int locationId, int adminEntityId) {
        addRow(locationId, adminEntityId);
    }

    @Override
    public void removeMembership(int locationId, int adminLevelId) {
        removeExistingRow(locationId, adminLevelId);
    }

    private void addRow(int locationId, int adminEntityId) {
        Location location = em.find(Location.class, locationId);
        location.getAdminEntities().add(em.getReference(AdminEntity.class, adminEntityId));
    }

    private void removeExistingRow(int locationId, int adminLevelId) {
        Location location = em.find(Location.class, locationId);
        for(AdminEntity entity : location.getAdminEntities()) {
        	if(entity.getLevel().getId() == adminLevelId) {
        		location.getAdminEntities().remove(entity);
        		return;
        	}
        }
    }
}
