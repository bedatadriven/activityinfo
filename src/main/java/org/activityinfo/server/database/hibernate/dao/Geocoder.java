package org.activityinfo.server.database.hibernate.dao;

import java.util.List;

import org.activityinfo.server.database.hibernate.entity.AdminEntity;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernatespatial.criterion.SpatialRestrictions;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

/**
 * Translates a lat/lng coordinate into a list of administrative entities
 *
 */
public class Geocoder {
    
    private final Provider<Session> sessionProvider;
    private final GeometryFactory gf = new GeometryFactory();

    @Inject
    public Geocoder(Provider<Session> sessionProvider) {
        this.sessionProvider = sessionProvider;
    }

    public List<AdminEntity> geocode(double latitude, double longitude) {

        Point point = gf.createPoint(new Coordinate(longitude, latitude));

        Session session = sessionProvider.get();
        Criteria criteria = session.createCriteria(AdminEntity.class);
        criteria.add(SpatialRestrictions.contains("geometry", point));
        
        // mysql seems to check only the MBRs. We need to verify
        // that the point is actually contained by the geometry
                
        List<AdminEntity> containedByMbr = criteria.list();
        List<AdminEntity> contains = Lists.newArrayList();
        for(AdminEntity entity : containedByMbr) {
            if(contains(entity.getGeometry(), point)) {
                contains.add(entity);
            }
        }
        return contains;
    }

    private boolean contains(Geometry geometry, Point point) {
        // MySQL seems to store all of our multipolygons as GeometryCollections
        
        if(geometry instanceof GeometryCollection) {
            GeometryCollection collection = (GeometryCollection) geometry;
            for(int i=0;i!=collection.getNumGeometries();++i) {
                if(contains(collection.getGeometryN(i), point)) {
                    return true;
                }
            }
            return false;
        } else {
            return geometry.contains(point);
        }
    }

}
