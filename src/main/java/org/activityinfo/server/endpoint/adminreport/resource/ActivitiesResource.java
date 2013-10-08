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

public class ActivitiesResource extends GridResource {

    @GET
    @Path("mostActive")
    @Produces(MediaType.TEXT_HTML)
    public Viewable mostActive(
        @QueryParam(PARAM_LIMIT) @DefaultValue(PARAM_LIMIT_DEFAULT) int limit) throws Exception {

        return gridProvider.get()
            .setTitle("Most active activities")

            .setHeaders("Total updates", "Last update", "Activity", "Database", "Country", "Owner", "Email")

            .setQuery("select " +
                "count(h.id), " + SQL_LAST_UPDATE + ", " +
                nameId("a.name", "a.activityid") +
                nameId("(select ud.name from userdatabase ud where ud.databaseid = d.databaseid)", "d.databaseid") +
                nameId("(select c.name from country c where c.countryid = d.countryid)", "d.countryid") +
                nameId("(select u.name from userlogin u where u.userid = o.userid)", "o.UserId") + "o.Email " +
                "from sitehistory h " +
                "left join site s on h.siteid = s.siteid " +
                "left join activity a on s.activityid = a.activityid " +
                "left join userdatabase d on a.databaseid = d.databaseid " +
                "left join userlogin o on d.owneruserid = o.userid " +
                "where a.datedeleted is null " +
                "group by a.activityid " +
                "order by count(h.id) desc, d.countryid, d.databaseid, a.activityid " +
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
            .setTitle("Least active activities")
            .setHint("Only activities that were updated after " + HISTORY_AVAILABLE_FROM + " are shown")

            .setHeaders("Total updates", "Last update", "Activity", "Database", "Country", "Owner", "Email")

            .setQuery("select " +
                "count(h.id), " + SQL_LAST_UPDATE + ", " +
                nameId("a.name", "a.activityid") +
                nameId("(select ud.name from userdatabase ud where ud.databaseid = d.databaseid)", "d.databaseid") +
                nameId("(select c.name from country c where c.countryid = d.countryid)", "d.countryid") +
                nameId("(select u.name from userlogin u where u.userid = o.userid)", "o.UserId") + "o.Email " +
                "from sitehistory h " +
                "left join site s on h.siteid = s.siteid " +
                "left join activity a on s.activityid = a.activityid " +
                "left join userdatabase d on a.databaseid = d.databaseid " +
                "left join userlogin o on d.owneruserid = o.userid " +
                "where a.datedeleted is null " +
                "group by a.activityid " +
                "order by count(h.id) asc, d.countryid, d.databaseid, a.activityid " +
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
            .setTitle("Recently active activities")

            .setHeaders("Last update", "Total updates", "Activity", "Database", "Country", "Owner", "Email")

            .setQuery("select " +
                SQL_LAST_UPDATE + ", count(h.id), " +
                nameId("a.name", "a.activityid") +
                nameId("(select ud.name from userdatabase ud where ud.databaseid = d.databaseid)", "d.databaseid") +
                nameId("(select c.name from country c where c.countryid = d.countryid)", "d.countryid") +
                nameId("(select u.name from userlogin u where u.userid = o.userid)", "o.UserId") + "o.Email " +
                "from sitehistory h " +
                "left join site s on h.siteid = s.siteid " +
                "left join activity a on s.activityid = a.activityid " +
                "left join userdatabase d on a.databaseid = d.databaseid " +
                "left join userlogin o on d.owneruserid = o.userid " +
                "where a.datedeleted is null " +
                "group by a.activityid " +
                "order by max(h.timecreated) desc, d.countryid, d.databaseid, a.activityid " +
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
            .setTitle("Least recently active activities")
            .setHint("Only activities that were updated after " + HISTORY_AVAILABLE_FROM + " are shown")

            .setHeaders("Last update", "Total updates", "Activity", "Database", "Country", "Owner", "Email")

            .setQuery("select " +
                SQL_LAST_UPDATE + ", count(h.id), " +
                nameId("a.name", "a.activityid") +
                nameId("(select ud.name from userdatabase ud where ud.databaseid = d.databaseid)", "d.databaseid") +
                nameId("(select c.name from country c where c.countryid = d.countryid)", "d.countryid") +
                nameId("(select u.name from userlogin u where u.userid = o.userid)", "o.UserId") + "o.Email " +
                "from sitehistory h " +
                "left join site s on h.siteid = s.siteid " +
                "left join activity a on s.activityid = a.activityid " +
                "left join userdatabase d on a.databaseid = d.databaseid " +
                "left join userlogin o on d.owneruserid = o.userid " +
                "where a.datedeleted is null " +
                "group by a.activityid " +
                "order by max(h.timecreated) asc, d.countryid, d.databaseid, a.activityid " +
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
            .setTitle("Activities without known history")

            .setHeaders("Activity", "Database", "Country", "Owner", "Email")

            .setQuery("select " +
                nameId("a.name", "a.activityid") +
                nameId("(select ud.name from userdatabase ud where ud.databaseid = d.databaseid)", "d.databaseid") +
                nameId("(select c.name from country c where c.countryid = d.countryid)", "d.countryid") +
                nameId("(select u.name from userlogin u where u.userid = o.userid)", "o.UserId") + "o.Email " +
                "from activity a " +
                "left join userdatabase d on a.databaseid = d.databaseid " +
                "left join userlogin o on d.owneruserid = o.userid " +
                "where a.datedeleted is null " +
                "and not exists ( " +
                "select 1 from sitehistory h " +
                "left join site s2 on h.siteid = s2.siteid " +
                "where s2.activityid = a.activityid " +
                ") " +
                "order by d.countryid, d.databaseid, a.activityid " +
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
            .setTitle("Most active activities since " + format(since))

            .setHeaders("Total updates", "Last update", "Activity", "Database", "Country", "Owner", "Email")

            .setQuery("select " +
                "count(h.id) total, " + SQL_LAST_UPDATE + ", " +
                nameId("a.name", "a.activityid") +
                nameId("(select ud.name from userdatabase ud where ud.databaseid = d.databaseid)", "d.databaseid") +
                nameId("(select c.name from country c where c.countryid = d.countryid)", "d.countryid") +
                nameId("(select u.name from userlogin u where u.userid = o.userid)", "o.UserId") + "o.Email " +
                "from sitehistory h " +
                "left join site s on h.siteid = s.siteid " +
                "left join activity a on s.activityid = a.activityid " +
                "left join userdatabase d on a.databaseid = d.databaseid " +
                "left join userlogin o on d.owneruserid = o.userid " +
                "where h.timeCreated >= ? " +
                "and a.datedeleted is null " +
                "group by a.activityid " +
                "order by total desc, lastupdate desc, d.countryid, d.databaseid, a.activityid " +
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
            .setTitle("Least active activities since " + format(since))
            .setHint("Only activities that were updated after " + HISTORY_AVAILABLE_FROM + " are shown")

            .setHeaders("Total updates", "Last update", "Activity", "Database", "Country", "Owner", "Email")

            .setQuery("select " +
            
                "(select count(h.id) " +
                "from sitehistory h " +
                "left join site s on h.siteid = s.siteid " +
                "where s.activityid = a.activityid and h.timeCreated >= ?) total, " +
                
                "(select " + SQL_LAST_UPDATE + " " +
                "from sitehistory h " +
                "left join site s on h.siteid = s.siteid " +
                "where s.activityid = a.activityid) lastupdate, " +
                
                nameId("a.name", "a.activityid") +
                nameId("(select ud.name from userdatabase ud where ud.databaseid = d.databaseid)", "d.databaseid") +
                nameId("(select c.name from country c where c.countryid = d.countryid)", "d.countryid") +
                nameId("(select u.name from userlogin u where u.userid = o.userid)", "o.UserId") + "o.Email " +
                
                "from activity a " +
                "left join userdatabase d on a.databaseid = d.databaseid " +
                "left join userlogin o on d.owneruserid = o.userid " +

                "where a.datedeleted is null " +
                "and exists ( select 1 from sitehistory h left join site s on h.siteid = s.siteid where s.activityid = a.activityid )" +
                "order by total asc, lastupdate asc, d.countryid, d.databaseid, a.activityid " +
                "limit ?")
            .setParams(since.getTime(), limit)

            .asViewable();
    }
}
