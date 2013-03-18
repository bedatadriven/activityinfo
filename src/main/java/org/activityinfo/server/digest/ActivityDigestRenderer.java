package org.activityinfo.server.digest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.activityinfo.client.i18n.I18N;
import org.activityinfo.server.database.hibernate.entity.Partner;
import org.activityinfo.server.database.hibernate.entity.SiteHistory;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.server.database.hibernate.entity.UserDatabase;
import org.apache.commons.lang.StringUtils;

import com.google.common.annotations.VisibleForTesting;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class ActivityDigestRenderer {
    private static final Logger LOGGER =
        Logger.getLogger(ActivityDigestRenderer.class.getName());

    private static final String PARTNER_COLOR = "67a639";

    private final Provider<EntityManager> entityManager;

    private transient Context ctx;

    @Inject
    public ActivityDigestRenderer(Provider<EntityManager> entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * @return a list with user activity digests for each database the specified user has access to by ownership or
     *         design permission, with the activity calculated from the specified amount of days before the specified
     *         date
     * @throws IOException
     */
    public List<String> render(User user, Date date, int days) throws IOException {
        this.ctx = new Context(user, date, days);

        List<String> items = new ArrayList<String>();
        List<String> inactiveDatabases = new ArrayList<String>();

        List<UserDatabase> databases = findDatabases(user);
        LOGGER.finest("found " + databases.size() + " database(s) for user " + user.getId());

        if (!databases.isEmpty()) {
            for (UserDatabase database : databases) {
                String renderedDatabase = renderDatabase(database);
                if (StringUtils.isNotBlank(renderedDatabase)) {
                    items.add(renderedDatabase);
                } else {
                    inactiveDatabases.add(database.getName());
                }
            }
        }

        if (!inactiveDatabases.isEmpty()) {
            items.add(renderInactiveDatabasesMessage(inactiveDatabases));
        }

        return items;
    }

    private String renderDatabase(UserDatabase database) throws IOException {

        List<Partner> partners = findPartners(database);
        LOGGER.finest("rendering user activity digest for user " + ctx.getUser().getId() +
            " and database " + database.getId() + " - found " + partners.size() + " partner(s)");
        if (partners.isEmpty()) {
            return null;
        }

        boolean databaseIsActive = false;

        List<SiteHistory> ownerHistories = findSiteHistory(database, database.getOwner());
        ActivityMap ownerActivityMap =
            new ActivityMap(database.getOwner(), ctx.getDate(), ctx.getDays(), ownerHistories);
        databaseIsActive |= ownerActivityMap.hasActivity();

        Map<Partner, List<ActivityMap>> partnerActivityMap = new HashMap<Partner, List<ActivityMap>>();

        for (Partner partner : partners) {
            List<User> partnerUsers = findUsers(database, partner);
            LOGGER.finest("found users " + partnerUsers + " for partner " + partner.getName());
            List<ActivityMap> activityMaps = new ArrayList<ActivityMap>();
            for (User partnerUser : partnerUsers) {
                List<SiteHistory> histories = findSiteHistory(database, partnerUser);
                ActivityMap activityMap = new ActivityMap(partnerUser, ctx.getDate(), ctx.getDays(), histories);
                activityMaps.add(activityMap);
                databaseIsActive |= activityMap.hasActivity();
            }
            partnerActivityMap.put(partner, activityMaps);
        }
        
        if (!databaseIsActive) {
            return null;
        }

        return renderActiveDatabase(database, ownerActivityMap, partnerActivityMap);
    }
    
    private String renderActiveDatabase(UserDatabase database, ActivityMap ownerActivityMap,
        Map<Partner, List<ActivityMap>> partnerActivityMap) {

        StringBuilder html = new StringBuilder();
        
        html.append("<span class='act-header' style='font-weight:bold;'>");
        html.append(database.getName());
        html.append("</span><br>");
        html.append("<div class='act-owner' style='margin-left: 25px;'>");
        html.append("<span class='act-owner-header'>");
        html.append("<a href=\"mailto:");
        html.append(ownerActivityMap.getUser().getEmail());
        html.append("\">");
        html.append(ownerActivityMap.getUser().getName());
        html.append("</a> (owner)</span><br>");
        html.append("<div class='act-owner-graph'>");
        html.append(createGraph(ownerActivityMap.getMap()));
        html.append("</div><br>");
        html.append("</div>");

        for (Map.Entry<Partner, List<ActivityMap>> entry : partnerActivityMap.entrySet()) {
            Partner partner = entry.getKey();
            List<ActivityMap> activityMaps = entry.getValue();

            html.append("<div class='act-partner' style='margin-left: 15px;'>");
            html.append("<span class='act-partner-header' style='color: #");
            html.append(PARTNER_COLOR);
            html.append("'; font-weight:bold;'>");
            html.append(partner.getName());
            html.append("</span><br>");

            html.append("<div class='act-partner-graph' style='margin-left: 10px;'>");
            html.append(createGraph(ActivityMap.getTotalActivityMap(activityMaps, ctx.getDays())));
            html.append("</div><br>");

            html.append("<div class='act-user' style='margin-left: 10px;'>");
            for (ActivityMap activityMap : activityMaps) {
                html.append("<span class='act-user-header'>");
                html.append("<a href=\"mailto:");
                html.append(activityMap.getUser().getEmail());
                html.append("\">");
                html.append(activityMap.getUser().getName());
                html.append("</a></span><br>");

                html.append("<div class='act-user-graph' style='margin-top: 2px;'>");
                html.append(createGraph(activityMap.getMap()));
                html.append("</div><br>");
            }
            html.append("</div>");
            html.append("</div>");
        }

        return html.toString();
    }

    private String createGraph(Map<Integer, Integer> activityMap) {
        List<Integer> list = new ArrayList<Integer>(activityMap.values());
        Collections.reverse(list);
        
        StringBuilder result = new StringBuilder();
        result.append("<table cellspacing='0' cellpadding='0' style='width: ");
        result.append(21 * list.size());
        result.append("px; height: 21px; border: 1px solid black; border-collapse:collapse;'>");
        result.append("<tr style='height: 21px;'>");

        for (int i = 0; i < list.size(); i++) {
            int value = list.get(i);
            result.append("<td style='width: 21px; height: 21px; background-color:#");
            result.append(determineColor(value));
            result.append("; border: 1px solid black; text-align:center; vertical-align:middle' title='");
            result.append(determineTitle(value, i));
            result.append("'>");
            result.append((value != 0) ? value : "&nbsp;");
            result.append("</td>");
        }

        result.append("</tr>");
        result.append("</table>");
        return result.toString();
    }

    private String determineColor(int value) {
        switch (value) {
        case 0:
            return "f0f0f0";
        case 1:
            return "67a639";
        case 2:
            return "67d839";
        default:
            return "67ff39";
        }
    }
    
    private String determineTitle(int updates, int dayIndex) {
        Date date = DigestDateUtil.daysAgo(ctx.getDate(), (ctx.getDays() - dayIndex - 1));
        return I18N.MESSAGES.activityDigestGraphTooltip(updates, date);
    }
    
    private String renderInactiveDatabasesMessage(List<String> inactiveDatabases) {
        Collections.sort(inactiveDatabases);
        StringBuilder sb = new StringBuilder();
        for (String db : inactiveDatabases) {
            sb.append(db);
            sb.append(", ");
        }
        sb.setLength(sb.length() - 2);

        StringBuilder html = new StringBuilder();
        html.append("<div class='act-inactivedatabases'>");
        html.append(I18N.MESSAGES.activityDigestInactiveDatabases(ctx.getDays()));
        html.append("<br>");
        html.append(sb.toString());
        html.append("</div>");
        return html.toString();
    }

    /**
     * @param user
     * @return all UserDatabases for the specified user where the user is the database owner, or where the database has
     *         a UserPermission for the specified user with allowDesign set to true. If the user happens to have his
     *         emailnotification preference set to false, an empty list is returned.
     */
    @VisibleForTesting
    @SuppressWarnings("unchecked")
    List<UserDatabase> findDatabases(User user) {
        // sanity check
        if (!user.isEmailNotification()) {
            return new ArrayList<UserDatabase>();
        }

        Query query = entityManager.get().createQuery(
            "select distinct d from UserDatabase d left join d.userPermissions p " +
                "where d.owner = :user or (p.user = :user and p.allowDesign = true) " +
                "order by d.name"
            );
        query.setParameter("user", user);

        return query.getResultList();
    }

    /**
     * @param database
     * @return the partners linked to the specified database via a userpermission
     */
    @VisibleForTesting
    @SuppressWarnings("unchecked")
    List<Partner> findPartners(UserDatabase database) {

        Query query = entityManager.get().createQuery(
            "select distinct p.partner from UserPermission p " +
                "where p.database = :database " +
                "order by p.partner.name"
            );
        query.setParameter("database", database);

        return query.getResultList();
    }

    /**
     * @param database
     * @param partner
     * @return the users linked to the specified database and partner via a userpermission where allowEdit is set to
     *         true.
     */
    @VisibleForTesting
    @SuppressWarnings("unchecked")
    List<User> findUsers(UserDatabase database, Partner partner) {

        Query query = entityManager.get().createQuery(
            "select distinct p.user from UserPermission p " +
                "where p.database = :database and p.partner = :partner and p.allowEdit = true " +
                "order by p.user.name"
            );
        query.setParameter("database", database);
        query.setParameter("partner", partner);

        return query.getResultList();
    }

    /**
     * @param database
     * @param user
     * @param from
     * @return the sitehistory edited since the specified timestamp (milliseconds) and linked to the specified database
     *         and user.
     */
    @VisibleForTesting
    @SuppressWarnings("unchecked")
    List<SiteHistory> findSiteHistory(UserDatabase database, User siteEditor) {

        Query query = entityManager.get().createQuery(
            "select distinct h from SiteHistory h " +
                "where h.site.activity.database = :database and h.user = :user and h.timeCreated >= :from " +
                "order by h.timeCreated"
            );
        query.setParameter("database", database);
        query.setParameter("user", siteEditor);
        query.setParameter("from", ctx.getFrom());

        return query.getResultList();
    }

    public Context getContext() {
        if (ctx == null) {
            throw new IllegalStateException("context not set! call render first");
        }
        return ctx;
    }

    public class Context {
        private final User user;
        private final Date date;
        private final int days;
        private final long from;

        Context(User user, Date date, int days) {
            this.user = user;
            this.date = date;
            this.days = days;
            this.from = DigestDateUtil.daysAgo(date, days).getTime();
        }

        public User getUser() {
            return user;
        }

        public Date getDate() {
            return date;
        }

        public int getDays() {
            return days;
        }

        public long getFrom() {
            return from;
        }

        public Date getFromDate() {
            return new Date(from);
        }
    }
}
