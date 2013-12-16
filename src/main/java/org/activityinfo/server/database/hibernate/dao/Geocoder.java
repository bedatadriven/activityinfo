package org.activityinfo.server.database.hibernate.dao;

import java.util.List;

import org.activityinfo.server.database.hibernate.entity.AdminEntity;
import org.activityinfo.server.util.mapping.JtsUtil;
import org.activityinfo.shared.report.content.AiLatLng;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.spatial.criterion.SpatialRestrictions;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.vividsolutions.jts.geom.Coordinate;
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

    /**
     * Geocode a single point to a list of admin entities
     * 
     * @param latitude
     * @param longitude
     * @return
     */
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
            if(JtsUtil.contains(entity.getGeometry(), point)) {
                contains.add(entity);
            }
        }
        return contains;
    }
    
    public List<List<AdminEntity>> gecodeBatch(List<AiLatLng> points) {
        BatchGeocoder batchGeocoder = new BatchGeocoder(sessionProvider.get());
        for(AiLatLng latLng : points) {
            batchGeocoder.addPoint(latLng.getLng(), latLng.getLat());
        }
        return batchGeocoder.geocode();   
    }

}
