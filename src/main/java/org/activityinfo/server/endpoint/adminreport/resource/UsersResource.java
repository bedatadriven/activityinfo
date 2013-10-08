package org.activityinfo.server.endpoint.adminreport.resource;

import java.util.Date;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.activityinfo.server.util.date.DateCalc;

import com.sun.jersey.api.view.Viewable;

public class UsersResource extends GridResource {

    @GET
    @Path("mostActive")
    @Produces(MediaType.TEXT_HTML)
    public Viewable mostActive(
        @QueryParam(PARAM_LIMIT) @DefaultValue(PARAM_LIMIT_DEFAULT) int limit) throws Exception {

        return gridProvider.get()
            .setTitle("Most active users")

            .setHeaders("Total updates", "Last update", "User", "Email")

            .setQuery("select " +
                "count(1) total, " + SQL_LAST_UPDATE + ", " +
                nameId("u.Name", "u.UserId") + "u.Email " +
                "from userlogin u left join sitehistory h on h.userid = u.userid " +
                "where h.id is not null " +
                "group by u.userid " +
                "order by total desc, lastupdate desc, u.userid desc " +
                "limit ?")
            .setParams(limit)

            .asViewable();
    }

    @GET
    @Path("leastActive")
    @Produces(MediaType.TEXT_HTML)
    public Viewable leastActive(
        @QueryParam(PARAM_LIMIT) @DefaultValue(PARAM_LIMIT_DEFAULT) int limit) throws Exception {

        return gridProvider.get()
            .setTitle("Least active users")
            .setHint("Only users that have made changes to the system after " + HISTORY_AVAILABLE_FROM + " are shown")

            .setHeaders("Total updates", "Last update", "User", "Email")

            .setQuery("select " +
                "count(1) total, " + SQL_LAST_UPDATE + ", " +
                nameId("u.Name", "u.UserId") + "u.Email " +
                "from userlogin u left join sitehistory h on h.userid = u.userid " +
                "where h.id is not null " +
                "group by u.userid " +
                "order by total asc, lastupdate asc, u.userid asc " +
                "limit ?")
            .setParams(limit)

            .asViewable();
    }

    @GET
    @Path("recentlyActive")
    @Produces(MediaType.TEXT_HTML)
    public Viewable recentlyActive(
        @QueryParam(PARAM_LIMIT) @DefaultValue(PARAM_LIMIT_DEFAULT) int limit) throws Exception {

        return gridProvider.get()
            .setTitle("Recently active users")

            .setHeaders("Last update", "Total updates", "User", "Email")

            .setQuery("select " +
                SQL_LAST_UPDATE + ", count(1) total, " +
                nameId("u.Name", "u.UserId") + "u.Email " +
                "from userlogin u left join sitehistory h on h.userid = u.userid " +
                "where h.id is not null " +
                "group by u.userid " +
                "order by lastupdate desc, u.userid desc " +
                "limit ?")
            .setParams(limit)

            .asViewable();
    }

    @GET
    @Path("leastRecentlyActive")
    @Produces(MediaType.TEXT_HTML)
    public Viewable leastRecentlyActive(
        @QueryParam(PARAM_LIMIT) @DefaultValue(PARAM_LIMIT_DEFAULT) int limit) throws Exception {

        return gridProvider.get()
            .setTitle("Least recently active users")
            .setHint("Only users that have made changes to the system after " + HISTORY_AVAILABLE_FROM + " are shown")

            .setHeaders("Last update", "Total updates", "User", "Email")

            .setQuery("select " +
                SQL_LAST_UPDATE + ", count(1) total, " +
                nameId("u.Name", "u.UserId") + "u.Email " +
                "from userlogin u left join sitehistory h on h.userid = u.userid " +
                "where h.id is not null " +
                "group by u.userid " +
                "order by lastupdate asc, u.userid asc " +
                "limit ?")
            .setParams(limit)

            .asViewable();
    }

    @GET
    @Path("withoutHistory")
    @Produces(MediaType.TEXT_HTML)
    public Viewable withoutHistory(
        @QueryParam(PARAM_LIMIT) @DefaultValue(PARAM_LIMIT_DEFAULT) int limit) throws Exception {

        return gridProvider.get()
            .setTitle("Users without known history")

            .setHeaders("User", "Email")

            .setQuery("select " +
                nameId("u.Name", "u.UserId") + "u.Email " +
                "from userlogin u left join sitehistory h on h.userid = u.userid " +
                "where h.id is null " +
                "group by u.userid " +
                "order by u.userid asc " +
                "limit ?")
            .setParams(limit)

            .asViewable();
    }

    @GET
    @Path("mostActiveSince")
    @Produces(MediaType.TEXT_HTML)
    public Viewable mostActiveSince(
        @QueryParam(PARAM_LIMIT) @DefaultValue(PARAM_LIMIT_DEFAULT) int limit,
        @QueryParam(PARAM_DAYS) @DefaultValue(PARAM_DAYS_DEFAULT) int days) throws Exception {

        Date since = DateCalc.daysAgo(new Date(), days);
        
        return mostActiveSince(since, limit);
    }

    @GET
    @Path("mostActiveThisMonth")
    @Produces(MediaType.TEXT_HTML)
    public Viewable mostActiveThisMonth(
        @QueryParam(PARAM_LIMIT) @DefaultValue(PARAM_LIMIT_DEFAULT) int limit) throws Exception {
        return mostActiveSince(DateCalc.getMonthStart(new Date()).getTime(), limit);
    }

    @GET
    @Path("mostActiveThisQuarter")
    @Produces(MediaType.TEXT_HTML)
    public Viewable mostActiveThisQuarter(
        @QueryParam(PARAM_LIMIT) @DefaultValue(PARAM_LIMIT_DEFAULT) int limit) throws Exception {
        return mostActiveSince(DateCalc.getQuarterStart(new Date()).getTime(), limit);
    }

    @GET
    @Path("mostActiveThisYear")
    @Produces(MediaType.TEXT_HTML)
    public Viewable mostActiveThisYear(
        @QueryParam(PARAM_LIMIT) @DefaultValue(PARAM_LIMIT_DEFAULT) int limit) throws Exception {
        return mostActiveSince(DateCalc.getYearStart(new Date()).getTime(), limit);
    }

    private Viewable mostActiveSince(Date since, int limit) throws Exception {
        return gridProvider.get()
            .setTitle("Most active users since " + format(since))

            .setHeaders("Total updates", "Last update", "User", "Email")

            .setQuery("select " +
                "count(h.id) total, " + SQL_LAST_UPDATE + ", " +
                nameId("u.Name", "u.UserId") + "u.Email " +
                "from sitehistory h left join userlogin u on h.userid = u.userid " +
                "where h.timeCreated >= ? " +
                "group by u.userid " +
                "order by total desc, lastupdate desc, u.userid desc " +
                "limit ?")
            .setParams(since.getTime(), limit)

            .asViewable();
    }

    @GET
    @Path("leastActiveSince")
    @Produces(MediaType.TEXT_HTML)
    public Viewable leastActiveSince(
        @QueryParam(PARAM_LIMIT) @DefaultValue(PARAM_LIMIT_DEFAULT) int limit,
        @QueryParam(PARAM_DAYS) @DefaultValue(PARAM_DAYS_DEFAULT) int days) throws Exception {

        Date since = DateCalc.daysAgo(new Date(), days);

        return leastActiveSince(since, limit);
    }

    @GET
    @Path("leastActiveThisMonth")
    @Produces(MediaType.TEXT_HTML)
    public Viewable leastActiveThisMonth(
        @QueryParam(PARAM_LIMIT) @DefaultValue(PARAM_LIMIT_DEFAULT) int limit) throws Exception {
        return leastActiveSince(DateCalc.getMonthStart(new Date()).getTime(), limit);
    }

    @GET
    @Path("leastActiveThisQuarter")
    @Produces(MediaType.TEXT_HTML)
    public Viewable leastActiveThisQuarter(
        @QueryParam(PARAM_LIMIT) @DefaultValue(PARAM_LIMIT_DEFAULT) int limit) throws Exception {
        return leastActiveSince(DateCalc.getQuarterStart(new Date()).getTime(), limit);
    }

    @GET
    @Path("leastActiveThisYear")
    @Produces(MediaType.TEXT_HTML)
    public Viewable leastActiveThisYear(
        @QueryParam(PARAM_LIMIT) @DefaultValue(PARAM_LIMIT_DEFAULT) int limit) throws Exception {
        return leastActiveSince(DateCalc.getYearStart(new Date()).getTime(), limit);
    }

    private Viewable leastActiveSince(Date since, int limit) throws Exception {
        return gridProvider.get()
            .setTitle("Least active users since " + format(since))
            .setHint("Only users that have made changes to the system after " + HISTORY_AVAILABLE_FROM + " are shown")

            .setHeaders("Total updates", "Last update", "User", "Email")

            .setQuery("select " +

                "(select count(h.id) " +
                "from sitehistory h " +
                "where h.userid = u.userid and h.timeCreated >= ?) total, " +

                "(select " + SQL_LAST_UPDATE + " " +
                "from sitehistory h " +
                "where h.userid = u.userid) lastupdate, " +

                nameId("u.Name", "u.UserId") + "u.Email " +

                "from userlogin u " +

                "where exists ( select 1 from sitehistory h where h.userid = u.userid ) " +
                "order by total asc, lastupdate asc, u.userid asc " +
                "limit ?")
            .setParams(since.getTime(), limit)

            .asViewable();
    }
}
