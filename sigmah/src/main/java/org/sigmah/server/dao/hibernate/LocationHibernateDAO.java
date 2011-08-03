/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.dao.hibernate;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.sigmah.server.dao.LocationDAO;
import org.sigmah.shared.domain.AdminEntity;
import org.sigmah.shared.domain.Location;

import com.google.inject.Inject;

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
        em.createNativeQuery("delete from LocationAdminLink where " +
                "LocationId = ?1 and " +
                "AdminEntityId in " +
                "(select e.AdminEntityId from AdminEntity e where e.AdminLevelId = ?2)")
                .setParameter(1, locationId)
                .setParameter(2, adminLevelId)
                .executeUpdate();
    }
    
	@Override
	public List<Location> allWithoutCoordinates() {
		Query query = em.createNativeQuery(
				"SELECT * FROM location l where x is null and y is null", Location.class);
		
		// Convert to generically typed list
		List<Location> result = new ArrayList<Location>();
		for (Object o: query.getResultList()) {
			result.add((Location)o);
		}
		
		return result;
	}
}
