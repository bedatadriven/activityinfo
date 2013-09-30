package org.activityinfo.server.database.hibernate.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.activityinfo.server.database.hibernate.entity.AdminEntity;

import com.google.common.collect.Lists;
import com.sun.jersey.api.core.InjectParam;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;

/**
 * Some how alot of our geometry has been corrupted, and stored
 * as Geometry Collections instead of Polygon / Multipolygon.
 * Fix now.
 */
@Path("/tasks/fixGeometry")
public class FixGeometryTask {

    private GeometryFactory gf = new GeometryFactory();
    
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String correctGeometry(@InjectParam EntityManager em) {
        
        while(true) {
        
            em.getTransaction().begin();
            
            List<AdminEntity> entities = em.createNativeQuery("select * from adminentity where GeometryType(Geometry) = 'GEOMETRYCOLLECTION' limit 50", AdminEntity.class)
                .getResultList();
            
            for(AdminEntity entity : entities) {
                entity.setGeometry(fixGeometry((GeometryCollection) entity.getGeometry()));
            }
                
            em.getTransaction().commit();
        
            if(entities.isEmpty()) {
                break;
            }
        }
        
        return "OK";
    }

    private Geometry fixGeometry(GeometryCollection collection) {
        
        List<Polygon> polygons = Lists.newArrayList();
        findPolygons(collection, polygons);
        
        if(polygons.size() == 0) {
            return null;
        } else if(polygons.size() == 1) {
            return polygons.get(0);
        } else {
            return gf.createMultiPolygon(polygons.toArray(new Polygon[polygons.size()]));
        }   
    }

    private void findPolygons(GeometryCollection collection,
        List<Polygon> polygons) {

        for(int i=0;i!=collection.getNumGeometries();++i) {
            Geometry geometry = collection.getGeometryN(i);
            if(geometry instanceof Polygon) {
                polygons.add((Polygon)geometry);
            } else if(geometry instanceof GeometryCollection) {
                findPolygons((GeometryCollection)geometry, polygons);
            }
        }
    }
    
}
