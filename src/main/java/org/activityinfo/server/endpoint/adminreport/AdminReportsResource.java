package org.activityinfo.server.endpoint.adminreport;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.activityinfo.server.endpoint.adminreport.resource.ActivitiesResource;
import org.activityinfo.server.endpoint.adminreport.resource.DatabasesResource;
import org.activityinfo.server.endpoint.adminreport.resource.GridResource;
import org.activityinfo.server.endpoint.adminreport.resource.UsersResource;

import com.sun.jersey.api.core.InjectParam;
import com.sun.jersey.api.view.Viewable;

@Path("/admin/reports")
public class AdminReportsResource extends GridResource {

    @GET
    @Produces(MediaType.TEXT_HTML)
    public Viewable getReport() throws Exception {
        return new Viewable("/adminreport/AdminReports.ftl");
    }

    @Path("users")
    public UsersResource getUsersResource(@InjectParam UsersResource usersResource) {
        return usersResource;
    }

    @Path("activities")
    public ActivitiesResource getActivitiesResource(@InjectParam ActivitiesResource activitiesResource) {
        return activitiesResource;
    }

    @Path("databases")
    public DatabasesResource getDatabasesResource(@InjectParam DatabasesResource databasesResource) {
        return databasesResource;
    }

    @GET
    @Path("recentUpdates")
    @Produces(MediaType.TEXT_HTML)
    public Viewable recentUpdates(
        @QueryParam(PARAM_LIMIT) @DefaultValue(PARAM_LIMIT_DEFAULT) int limit) throws Exception {

        return gridProvider.get()
            .setTitle("Recent updates")

            .setHeaders("Date", "Editor", "Editor Email", "Activity", "Database", "Country", "Owner", "Owner Email")

            .setQuery("select " +
                "date_format( from_unixtime( substring(h.timecreated, 1,10) ), " + SQL_DATETIME_FORMAT + " ), " +
                nameId("(select u.name from userlogin u where u.userid = e.userid)", "e.UserId") +
                "(select u.Email from userlogin u where u.userid = e.userid), " +
                nameId("a.name", "a.activityid") +
                nameId("(select ud.name from userdatabase ud where ud.databaseid = d.databaseid)", "d.databaseid") +
                nameId("(select c.name from country c where c.countryid = d.countryid)", "d.countryid") +
                nameId("(select u.name from userlogin u where u.userid = o.userid)", "o.UserId") + "o.Email " +
                "from sitehistory h " +
                "left join userlogin e on h.userid = e.userid " +
                "left join site s on h.siteid = s.siteid " +
                "left join activity a on s.activityid = a.activityid " +
                "left join userdatabase d on a.databaseid = d.databaseid " +
                "left join userlogin o on d.owneruserid = o.userid " +
                "order by h.timecreated " +
                "limit " + limit)

            .asViewable();
    }
}
