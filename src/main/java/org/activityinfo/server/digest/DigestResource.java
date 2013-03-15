package org.activityinfo.server.digest;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withUrl;

import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.appengine.api.taskqueue.TaskOptions.Method;
import com.google.common.annotations.VisibleForTesting;
import com.google.inject.Inject;
import com.google.inject.Provider;

@Path(DigestResource.ENDPOINT)
public class DigestResource {
    public static final String ENDPOINT = "/tasks/digests";
    public static final String USERDIGEST_QUEUE = "userdigest";

    private static final Logger LOGGER =
        Logger.getLogger(DigestResource.class.getName());

    private final Provider<EntityManager> entityManager;

    @Inject
    public DigestResource(Provider<EntityManager> entityManager) {
        this.entityManager = entityManager;
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String createDigests() throws Exception {

        List<Integer> userIds = selectUsers();

        String msg = "creating digests for " + userIds.size() + " users";
        LOGGER.info(msg);

        Queue queue = QueueFactory.getQueue(USERDIGEST_QUEUE);

        for (Integer userId : userIds) {
            TaskOptions taskoptions = withUrl(UserDigestResource.ENDPOINT)
                .param(UserDigestResource.PARAM_USER, String.valueOf(userId))
                .method(Method.GET);

            LOGGER.finest("creating userdigest task with options: "
                + taskoptions.toString());

            queue.add(taskoptions);
        }
        return msg;
    }

    /**
     * @return the ids of all users who could possibly be selected to recieve
     *         the digest email. Filter on database ownership,
     *         userpermission.allowView or userpermission.allowDesign to
     *         minimize the amount of created userdigest tasks.
     */
    @VisibleForTesting
    @SuppressWarnings("unchecked")
    List<Integer> selectUsers() {
        // @formatter:off
        Query query = entityManager.get().createNativeQuery(
            "select u.userid from userlogin u " +
            "where u.emailnotification and ( " +
                "exists ( " +
                    "select 1 from userdatabase d " +
                    "where d.owneruserid = u.userid " +
                ") " +
                "or " +
                "exists ( " +
                    "select 1 from userpermission p " +
                    "where p.userId = u.userid " +
                    "and (p.allowView or p.allowDesign) " +
                ") " +
            ")"
        );
        // @formatter:on
        return query.getResultList();
    }
}
