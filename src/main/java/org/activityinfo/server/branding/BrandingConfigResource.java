package org.activityinfo.server.branding;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.activityinfo.server.database.hibernate.entity.Domain;

import com.google.common.collect.Maps;
import com.sun.jersey.api.core.InjectParam;
import com.sun.jersey.api.view.Viewable;

@Path("/admin/branding")
public class BrandingConfigResource {

    @GET
    @Produces(MediaType.TEXT_HTML) 
    @Path("{host}")
    public Viewable getPage(@InjectParam EntityManager em, @PathParam("host") String host) {
        
        Domain domain = em.find(Domain.class, host);
        if(domain == null) {
            domain = new Domain();
            domain.setHost(host);
        }
        
        Map<String, Object> model = Maps.newHashMap();
        model.put("customDomain", domain);
        
        return new Viewable("/page/BrandingConfig.ftl", model);
    }
    
    @POST
    @Path("{host}")
    public Response updateConfig(
        @InjectParam EntityManager em, 
        @Context UriInfo uri,
        @PathParam("host") String host,
        @FormParam("title") String updatedTitle,
        @FormParam("scaffolding") String updatedScaffolding,
        @FormParam("homePageBody") String updatedHomePageBody) {
        
        em.getTransaction().begin();
        
        Domain domain = em.find(Domain.class, host);
        if(domain == null) {
            domain = new Domain();
            domain.setHost(host);
        }
        
        domain.setTitle(updatedTitle);
        domain.setScaffolding(updatedScaffolding);
        domain.setHomePageBody(updatedHomePageBody);
        
        em.persist(domain);
        em.getTransaction().commit();
        
        return Response.seeOther(uri.getRequestUri()).build();
    }
    
}
