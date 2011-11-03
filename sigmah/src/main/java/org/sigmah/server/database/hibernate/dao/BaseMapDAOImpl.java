/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.database.hibernate.dao;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;
import org.sigmah.shared.map.TileBaseMap;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * Reads a list of base maps (tile sets) from a path on the local disk
 * Searches the following three paths:
 * <ul>
 * <li>The path specified by the "basemaps.root" property in tomcat/conf/activityinfo.conf</li>
 * <li>e:\tiles</li>
 * <li>c:\tiles</li>
 * </ul>
 * Ultimately needs to be replaced by a database table with URLs to WMS/TMS services.
 *
 * @author Alex Bertram
 */
public class BaseMapDAOImpl implements BaseMapDAO {

    private final static Logger logger = Logger.getLogger(BaseMapDAOImpl.class);
    private final Provider<EntityManager> entityManager;
    
    @Inject
    public BaseMapDAOImpl(Provider<EntityManager> entityManager) {
    	this.entityManager = entityManager;
    }

    public TileBaseMap getBaseMap(String id) {
        return entityManager.get().find(TileBaseMap.class, id);
    }

    public List<TileBaseMap> getBaseMaps() {
    	return entityManager.get().createQuery("select m from BaseMap m").getResultList();
    }
}
