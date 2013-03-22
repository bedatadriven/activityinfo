package org.activityinfo.server.digest.activity;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.ws.rs.Path;

import org.activityinfo.server.digest.DigestResource;

import com.google.inject.Inject;
import com.google.inject.Provider;

@Path(ActivityDigestResource.ENDPOINT)
public class ActivityDigestResource extends DigestResource {
    public static final String ENDPOINT = "/tasks/activitydigests";

    private final Provider<EntityManager> entityManager;

    @Inject
    public ActivityDigestResource(Provider<EntityManager> entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public String getUserDigestEndpoint() {
        return ActivityUserDigestResource.ENDPOINT;
    }

    /**
     * @return the ids of all users who could possibly be selected to recieve the digest email. Filter on database
     *         ownership and userpermission.allowDesign to minimize the amount of created userdigest tasks.
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<Integer> selectUsers() {
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
                        "and p.allowDesign " +
                ") " +
            ")"
        );
        // @formatter:on
        return query.getResultList();
    }
}
