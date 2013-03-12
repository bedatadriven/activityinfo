package org.activityinfo.server.digest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.activityinfo.server.command.DispatcherSync;
import org.activityinfo.server.database.hibernate.entity.Partner;
import org.activityinfo.server.database.hibernate.entity.SiteHistory;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.server.database.hibernate.entity.UserDatabase;
import org.apache.commons.lang.StringUtils;

import com.google.common.annotations.VisibleForTesting;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class ActivityDigestRenderer {
    private final Provider<EntityManager> entityManager;
    private final DispatcherSync dispatcher;

    private static final Logger LOGGER =
        Logger.getLogger(ActivityDigestRenderer.class.getName());

    @Inject
    public ActivityDigestRenderer(Provider<EntityManager> entityManager,
        DispatcherSync dispatcher) {
        this.entityManager = entityManager;
        this.dispatcher = dispatcher;
    }

    /**
     * @param user
     * @param date
     * @param days
     * @return a list with user activity digests for each database the specified user has access to by ownership or
     *         design permission, with the activity calculated from the specified amount of days before the specified
     *         date
     * @throws IOException
     */
    public List<String> render(User user, Date date, int days) throws IOException {
        List<String> items = new ArrayList<String>();

        List<UserDatabase> databases = findDatabases(user);
        LOGGER.finest("found " + databases.size() + " database(s) for user " + user.getId());

        if (!databases.isEmpty()) {
            for (UserDatabase database : databases) {
                String item = renderDatabase(user, database, date, days);
                if (StringUtils.isNotBlank(item)) {
                    items.add(item);
                }
            }
        }

        return items;
    }

    private String renderDatabase(User user, UserDatabase database, Date date, int days)
        throws IOException {

        List<Partner> partners = findPartners(database);
        LOGGER.finest("rendering user activity digest for user " + user.getId() +
            " and database " + database.getId() + " - found " + partners.size() + " partner(s)");
        if (partners.isEmpty()) {
            return null;
        }

        StringBuilder html = new StringBuilder();
        html.append("<span class='act-header' style='font-weight:bold;'>");
        html.append(database.getName());
        html.append("</span><br>");

        long from = DigestDateUtil.midnightMillisDaysAgo(date, days);

        List<SiteHistory> ownerHistories = findSiteHistory(database, database.getOwner(), from);
        ActivityMap ownerActivityMap = new ActivityMap(database.getOwner(), date, days, ownerHistories);
        html.append(ownerActivityMap.getUser().getName());
        html.append(" (");
        html.append(ownerActivityMap.getUser().getEmail());
        html.append(")<br>");
        html.append(createGraph(ownerActivityMap.getMap()));

        for (Partner partner : partners) {
            List<User> partnerUsers = findUsers(database, partner);
            LOGGER.finest("found users " + partnerUsers + " for partner " + partner.getName());
            List<ActivityMap> activityMaps = new ArrayList<ActivityMap>();
            for (User partnerUser : partnerUsers) {
                List<SiteHistory> histories = findSiteHistory(database, partnerUser, from);
                ActivityMap activityMap = new ActivityMap(partnerUser, date, days, histories);
                activityMaps.add(activityMap);
            }

            html.append("<div class='act-partner' style='margin-top:15px;'>");
            html.append("<span class='act-partner-header' style='margin-left:10px; font-weight:bold;'> ");
            html.append(partner.getName());
            html.append("</span><br>");

            html.append("<span class='act-partner-graph' style='margin-left:12px;'> ");
            html.append(createGraph(ActivityMap.getTotalActivityMap(activityMaps, days + 1)));
            html.append("</span><br>");

            html.append("<div class='act-user' style='margin-top:10px;'>");
            for (ActivityMap activityMap : activityMaps) {
                html.append("<span class='act-user-header' style='margin-left:20px;'>");
                html.append(activityMap.getUser().getName());
                html.append(" (");
                html.append(activityMap.getUser().getEmail());
                html.append(")</span><br>");

                html.append("<span class='act-user-graph' style='margin-left:22px;'> ");
                html.append(createGraph(activityMap.getMap()));
                html.append("</span><br>");
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
        for (Integer value : list) {
            result.append(value);
            result.append(", ");
        }
        result.setLength(result.length() - 2);
        return result.toString();
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
    List<SiteHistory> findSiteHistory(UserDatabase database, User user, long from) {

        Query query = entityManager.get().createQuery(
            "select distinct h from SiteHistory h " +
                "where h.site.activity.database = :database and h.user = :user and h.timeCreated >= :from " +
                "order by h.timeCreated"
            );
        query.setParameter("database", database);
        query.setParameter("user", user);
        query.setParameter("from", from);

        return query.getResultList();
    }
}
