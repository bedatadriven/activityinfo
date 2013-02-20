package org.activityinfo.server.endpoint.hxl;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.jaxrs.listing.ApiListing;

@Path("/api/docs")
@Api("/api/docs")
@Produces(MediaType.APPLICATION_JSON)
public class ApiListingResource extends ApiListing {

}
