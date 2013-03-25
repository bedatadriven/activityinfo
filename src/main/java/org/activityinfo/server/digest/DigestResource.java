package org.activityinfo.server.digest;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withUrl;

import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.appengine.api.taskqueue.TaskOptions.Method;

public abstract class DigestResource {
    public static final String USERDIGEST_QUEUE = "userdigest";

    private static final Logger LOGGER =
        Logger.getLogger(DigestResource.class.getName());

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String createDigests() throws Exception {

        List<Integer> userIds = selectUsers();

        String msg = "creating digests for " + userIds.size() + " users";
        LOGGER.info(msg);

        Queue queue = QueueFactory.getQueue(USERDIGEST_QUEUE);

        for (Integer userId : userIds) {
            TaskOptions taskoptions = withUrl(getUserDigestEndpoint())
                .param(UserDigestResource.PARAM_USER, String.valueOf(userId))
                .method(Method.GET);
            queue.add(taskoptions);
        }
        return msg;
    }

    public abstract List<Integer> selectUsers();

    public abstract String getUserDigestEndpoint();

}
