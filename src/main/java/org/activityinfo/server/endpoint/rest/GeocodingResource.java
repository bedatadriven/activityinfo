package org.activityinfo.server.endpoint.rest;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.activityinfo.server.database.hibernate.dao.Geocoder;
import org.activityinfo.server.database.hibernate.entity.AdminEntity;

import com.sun.jersey.api.core.InjectParam;

public class GeocodingResource {

    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<AdminEntity> geocode(@InjectParam Geocoder geocoder, 
        @QueryParam("lat") double latitude,
        @QueryParam("lng") double longitude) {
        
        return geocoder.geocode(latitude, longitude);
    }
    
}
