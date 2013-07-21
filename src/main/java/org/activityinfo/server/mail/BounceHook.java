package org.activityinfo.server.mail;

import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.activityinfo.server.util.config.DeploymentConfiguration;

import com.google.common.base.Strings;
import com.google.inject.Inject;

@Path("/bounceHook")
public class BounceHook {

    private static final Logger LOGGER = Logger.getLogger(BounceHook.class.getName());
    
    private final String bounceHookToken;
    
    @Inject
    public BounceHook(DeploymentConfiguration config) {
        bounceHookToken = config.getProperty("postmark.bouncehook.key");
    }
    
    @POST
    @Path("/{token}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response bounced(@PathParam("token") String token, Bounce bounce) {
        
        if(Strings.isNullOrEmpty(bounceHookToken) || !bounceHookToken.equals(token) ) {
            throw new WebApplicationException(Status.UNAUTHORIZED);
        }
        
        LOGGER.info("Subject = " + bounce.getSubject());
        LOGGER.info("Email = " + bounce.getEmail());
        LOGGER.info("Type = " + bounce.getType());
        LOGGER.info("Token = " + token);
      
        return Response.ok().build(); 
    }
}
