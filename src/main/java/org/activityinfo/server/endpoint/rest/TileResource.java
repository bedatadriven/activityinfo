package org.activityinfo.server.endpoint.rest;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.activityinfo.server.util.config.DeploymentConfiguration;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.common.base.Objects;
import com.google.inject.Inject;

public class TileResource {

    private static final String IMAGE_PROPERTY = "i";
    
    private final String authToken;
    private final DatastoreService datastore;
    
    @Inject
    public TileResource(DeploymentConfiguration config) {
        authToken = config.getProperty("tile.update.key");
        datastore = DatastoreServiceFactory.getDatastoreService();
    }
    
    @PUT
    @Path("{layer}/{z}/{x}/{y}.png")
    public Response putTile(
        @HeaderParam("X-Update-Key") String authToken,
        @PathParam("layer") String layer, 
        @PathParam("z") int zoom, 
        @PathParam("x") int x, 
        @PathParam("y") int y, 
        byte[] image) {
    
        if(!Objects.equal(this.authToken, authToken)) {
            throw new WebApplicationException(Status.UNAUTHORIZED);
        }
         
        Entity entity = new Entity(getKey(layer, zoom, x, y));
        entity.setProperty("i", new Blob(image));
        
        datastore.put(entity);
        
        return Response.ok().build();
    }
    
    @GET
    @Path("{layer}/{z}/{x}/{y}.png")
    @Produces("image/png")
    public Response getTile(  
        @PathParam("layer") String layer, 
        @PathParam("z") int zoom, 
        @PathParam("x") int x, 
        @PathParam("y") int y) {
        
        Entity entity;
        try {
            entity = datastore.get(getKey(layer, zoom, x, y));
        } catch (EntityNotFoundException e) {
            throw new WebApplicationException(Status.NOT_FOUND);
        }
        Blob blob = (Blob)entity.getProperty(IMAGE_PROPERTY);
        return Response.ok(blob.getBytes()).build();
    }

    
    private Key getKey(String layer, int z, int x, int y) {
        return KeyFactory.createKey("Tile", layer + "_" + z + "_" + x + "_" + y);
    }
    
}
