package org.activityinfo.server.database.hibernate.dao;

import java.util.ArrayList;
import java.util.List;

import org.activityinfo.server.database.hibernate.entity.AdminEntity;
import org.activityinfo.server.util.mapping.JtsUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.spatial.criterion.SpatialRestrictions;

import com.google.common.collect.Lists;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.index.sweepline.SweepLineIndex;
import com.vividsolutions.jts.index.sweepline.SweepLineInterval;
import com.vividsolutions.jts.index.sweepline.SweepLineOverlapAction;

/**
 * Matches a large set of points to their respective Administrative 
 * Entities to which they belong using a SweepLineIndex
 * 
 */
public class BatchGeocoder {

    private final Session session;
    private final GeometryFactory gf = new GeometryFactory();
    private final List<Point> points = Lists.newArrayList();
    private final SweepLineIndex index = new SweepLineIndex();
    private List<AdminEntity> entities;
    private List<List<AdminEntity>> results = Lists.newArrayList();
    
    public BatchGeocoder(Session session) {
        super();
        this.session = session;
    }
    
    public void addPoint(double x, double y) {

        int pointIndex = points.size();

        // add the point to the list
        points.add(gf.createPoint(new Coordinate(x, y)));
       
        // index the point
        index.add(new SweepLineInterval(x, x, pointIndex));
        
        // add an empty result
        results.add(new ArrayList<AdminEntity>());
    }
    
    public List<List<AdminEntity>> geocode() {
        entities = queryEntities();
        
        // add the entities to the sweep line index
        indexEntities();
        
        index.computeOverlaps(new SweepLineOverlapAction() {
            
            @Override
            public void overlap(SweepLineInterval s0, SweepLineInterval s1) {
                // is this an overlap between a point and and entity?
                if(s0.getItem() instanceof Integer && s1.getItem() instanceof AdminEntity) {
                    checkContains((Integer)s0.getItem(), (AdminEntity)s1.getItem());
                } else if(s1.getItem() instanceof Integer && s0.getItem() instanceof AdminEntity) {
                    checkContains((Integer)s1.getItem(), (AdminEntity)s0.getItem());
                }
            }
        });
        
        return results;
    }

    private List<AdminEntity> queryEntities() {
        // to do the job efficiently on the set of points, we'll use a SweepLine algorithm.
        // but first we need to compute the bounds of the bounds so we know what to fetch
        
        Envelope pointsMbr = new Envelope();
        for(Point point : points) {
            pointsMbr.expandToInclude(point.getCoordinate());
        }
        
        // now query the x/y ranges of all admin entities that
        // might intersect with the ranges
        
        Criteria criteria = session.createCriteria(AdminEntity.class);
        criteria.add(SpatialRestrictions.intersects("geometry", gf.toGeometry(pointsMbr)));
        
        List<AdminEntity> entities = criteria.list();
        return entities;
    }

    private void indexEntities() {
        for(AdminEntity entity : entities) {
            index.add(new SweepLineInterval(entity.getBounds().getX1(), entity.getBounds().getX2(), entity));
        }
    }

    private void checkContains(Integer pointIndex, AdminEntity entity) {
        Point point = points.get(pointIndex);
        if(JtsUtil.contains(entity.getGeometry(), point)) {
            results.get(pointIndex).add(entity);
        }
    }
}
