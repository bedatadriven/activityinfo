package org.activityinfo.server.digest.geo;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.ws.rs.Path;

import org.activityinfo.server.digest.DigestResource;

import com.google.inject.Inject;
import com.google.inject.Provider;

@Path(GeoDigestResource.ENDPOINT)
public class GeoDigestResource extends DigestResource {
    public static final String ENDPOINT = "/tasks/geodigests";

    private final Provider<EntityManager> entityManager;

    @Inject
    public GeoDigestResource(Provider<EntityManager> entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public String getUserDigestEndpoint() {
        return GeoUserDigestResource.ENDPOINT;
    }

    /**
     * @return the ids of all users who could possibly be selected to recieve the digest email. Filter on database
     *         ownership and userpermission.allowView to minimize the amount of created userdigest tasks.
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
                        "and p.allowView " +
                ") " +
            ")"
        );
        // @formatter:on
        return query.getResultList();
    }
}
