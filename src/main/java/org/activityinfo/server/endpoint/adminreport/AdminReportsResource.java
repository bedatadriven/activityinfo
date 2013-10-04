package org.activityinfo.server.endpoint.adminreport;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.core.InjectParam;
import com.sun.jersey.api.view.Viewable;

@Path("/admin/reports")
public class AdminReportsResource {
    private static String LAST_UPDATE = "date_format(from_unixtime(substring(max(h.timecreated), 1,10)), '%Y-%m-%d')";

    @GET
    @Produces(MediaType.TEXT_HTML)
    public Viewable getReport() throws Exception {
        return new Viewable("/adminreport/AdminReports.ftl");
    }

    @GET
    @Path("users/mostActive")
    @Produces(MediaType.TEXT_HTML)
    public Viewable usersMostActive(@InjectParam AdminReport adminReport) throws Exception {
        return adminReport.setTitle("Most Active Users")

            .setHeaders("Total updates", "Last update", "Name", "Email", "UserID")

            .setQuery("select count(1), " + LAST_UPDATE + ", u.Name, u.Email, u.UserId " +
                "from sitehistory h left join userlogin u on h.userid = u.userid " +
                "group by h.userid " +
                "order by count(1) desc " +
                "limit 100")

            .asViewable();
    }
        
    @GET
    @Path("users/recentlyActive")
    @Produces(MediaType.TEXT_HTML)
    public Viewable usersRecentlyActive(@InjectParam AdminReport adminReport) throws Exception {
        return adminReport.setTitle("Recently Active Users")

            .setHeaders("Last update", "Total updates", "Name", "Email", "UserID")

            .setQuery("select " + LAST_UPDATE + ", count(1), u.Name, u.Email, u.UserId " +
                "from sitehistory h left join userlogin u on h.userid = u.userid " +
                "group by h.userid " +
                "order by max(h.timecreated) desc " +
                "limit 100")

            .asViewable();
    }

    @GET
    @Path("activities/mostActive")
    @Produces(MediaType.TEXT_HTML)
    public Viewable activitiesMostActive(@InjectParam AdminReport adminReport) throws Exception {
        return adminReport.setTitle("Most Active Activities")

            .setHeaders("Total updates", "Last update", "Activity", "ActivityID",
                "Database", "DatabaseID", "Owner", "Email", "OwnerID")

            .setQuery("select count(h.id), " + LAST_UPDATE + ", a.name, a.activityid, " +
                "(select ud.name from userdatabase ud where ud.databaseid = d.databaseid), d.databaseid, " +
                "(select u.name from userlogin u where u.userid = o.userid), o.Email, o.UserId " +
                "from sitehistory h " +
                "left join site s on h.siteid = s.siteid " +
                "left join activity a on s.activityid = a.activityid " +
                "left join userdatabase d on a.databaseid = d.databaseid " +
                "left join userlogin o on d.owneruserid = o.userid " +
                "group by a.activityid " +
                "order by count(h.id) desc " +
                "limit 100")

            .asViewable();
    }

    @GET
    @Path("activities/recentlyActive")
    @Produces(MediaType.TEXT_HTML)
    public Viewable activitiesRecentlyActive(@InjectParam AdminReport adminReport) throws Exception {
        return adminReport.setTitle("Recently Active Activities")

            .setHeaders("Last update", "Total updates", "Activity", "ActivityID",
                "Database", "DatabaseID", "Owner", "Email", "OwnerID")

            .setQuery("select " + LAST_UPDATE + ", count(h.id), a.name, a.activityid, " +
                "(select ud.name from userdatabase ud where ud.databaseid = d.databaseid), d.databaseid, " +
                "(select u.name from userlogin u where u.userid = o.userid), o.Email, o.UserId " +
                "from sitehistory h " +
                "left join site s on h.siteid = s.siteid " +
                "left join activity a on s.activityid = a.activityid " +
                "left join userdatabase d on a.databaseid = d.databaseid " +
                "left join userlogin o on d.owneruserid = o.userid " +
                "group by a.activityid " +
                "order by max(h.timecreated) desc " +
                "limit 100")

            .asViewable();
    }

    @GET
    @Path("databases/mostActive")
    @Produces(MediaType.TEXT_HTML)
    public Viewable databasesMostActive(@InjectParam AdminReport adminReport) throws Exception {
        return adminReport.setTitle("Most Active Databases")

            .setHeaders("Total updates", "Last update", "Database", "DatabaseID", "Owner", "Email", "OwnerID")

            .setQuery("select count(h.id), " + LAST_UPDATE + ", d.name, d.databaseid, " +
                "(select u.name from userlogin u where u.userid = o.userid), o.Email, o.UserId " +
                "from sitehistory h " +
                "left join site s on h.siteid = s.siteid " +
                "left join activity a on s.activityid = a.activityid " +
                "left join userdatabase d on a.databaseid = d.databaseid " +
                "left join userlogin o on d.owneruserid = o.userid " +
                "group by d.databaseid " +
                "order by count(h.id) desc " +
                "limit 100")

            .asViewable();
    }

    @GET
    @Path("databases/recentlyActive")
    @Produces(MediaType.TEXT_HTML)
    public Viewable databasesRecentlyActive(@InjectParam AdminReport adminReport) throws Exception {
        return adminReport.setTitle("Recently Active Databases")

            .setHeaders("Last update", "Total updates", "Database", "DatabaseID", "Owner", "Email", "OwnerID")
            .setQuery("select " + LAST_UPDATE + ", count(h.id), d.name, d.databaseid, " +
                "(select u.name from userlogin u where u.userid = o.userid), o.Email, o.UserId " +
                "from sitehistory h " +
                "left join site s on h.siteid = s.siteid " +
                "left join activity a on s.activityid = a.activityid " +
                "left join userdatabase d on a.databaseid = d.databaseid " +
                "left join userlogin o on d.owneruserid = o.userid " +
                "group by d.databaseid " +
                "order by max(h.timecreated) desc " +
                "limit 100")

            .asViewable();
    }
}
