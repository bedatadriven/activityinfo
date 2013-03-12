package org.activityinfo.server.digest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.activityinfo.server.command.DispatcherSync;
import org.activityinfo.server.database.hibernate.entity.Partner;
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
     * @param from
     * @return a list with user activity digests for each database for the specified user, with the activity calculated
     *         from the specified timestamp (milliseconds).
     * @throws IOException
     */
    public List<String> render(User user, long from) throws IOException {
        List<String> items = new ArrayList<String>();

        List<UserDatabase> databases = findDatabases(user);
        LOGGER.finest("found " + databases.size() + " database(s) for user " + user.getId());

        if (!databases.isEmpty()) {
            // SchemaDTO schemaDTO = dispatcher.execute(new GetSchema());

            for (UserDatabase database : databases) {
                String item = renderDatabase(user, database, from);
                if (StringUtils.isNotBlank(item)) {
                    items.add(item);
                }
            }
        }

        return items;
    }

    /**
     * @param user
     * @param database
     * @param from
     * @return the user activity digist for the specified user and database, with the activity calculated from the
     *         specified timestamp (milliseconds).
     * @throws IOException
     */
    public String renderDatabase(User user, UserDatabase database, long from)
        throws IOException {
        List<Partner> partners = findPartners(database);

        LOGGER.finest("rendering user activity digest for user " + user.getId() + " and database " + database.getId() +
            " - found " + partners.size() + " partner(s)");

        if (partners.isEmpty()) {
            return null;
        }

        return null;
    }

    /**
     * @param user
     *            the user to find the databases for
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
     *            the database the partners should be linked to
     * @return the partners linked to the specified database via a userpermission
     */
    @VisibleForTesting
    @SuppressWarnings("unchecked")
    List<Partner> findPartners(UserDatabase database) {

        Query query = entityManager.get().createQuery(
            "select distinct p.partner from UserPermission p where p.database = :database"
            );
        query.setParameter("database", database);

        return query.getResultList();
    }

    // for each database get partners
    // query: from userpermission where database = ::database
    //
    // for each partner get users & their activities from last 7 days
    // query: select u,
    // ( select count from sitehistory
    // where sitehistory.user = u & site.activity.database = ::database
    // and sitehistory.dateCreated >= ::date - 7 days
    // and sitehistory.dateCreated <= ::date - 6 days
    // ) as first,
    // ( etc )
    // from user u, userpermission up
    // where u.id = up.userid and up.database = ::database
    // and up.allowEdit
    //
    // or group by user, date?
    //
    // (this might not be the most efficient query, possibly select all history
    // from ::date - 7 and sort & count in code)
    //
    // for each partner create activityGraph:
    // @see google charts tools? (other options: html table, concat 7 images,
    // etc)
    // @see github-profile example
    // graphUrl = generate activity graph (total user activity), return url
    //
    // for each user create activityGraph:
    // userGraphUrl = generate activity graph, return url
    //
    // generate html content:
    // print database & partner header (assuming there's one partner)
    // for each user:
    // print user info
    // print activityGraph using graphUrl
    //
    //

}
