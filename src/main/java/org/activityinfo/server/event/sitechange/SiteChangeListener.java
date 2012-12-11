package org.activityinfo.server.event.sitechange;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withUrl;

import org.activityinfo.server.event.CommandEvent;
import org.activityinfo.server.event.CommandEventListener;
import org.activityinfo.server.event.ServerEventBus;
import org.activityinfo.shared.command.CreateSite;
import org.activityinfo.shared.command.UpdateSite;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.inject.Inject;

public class SiteChangeListener extends CommandEventListener {
	
	@Inject 
	@SuppressWarnings("unchecked")
	public SiteChangeListener(ServerEventBus serverEventBus) {
		super(serverEventBus, CreateSite.class, UpdateSite.class);
	}

	@Override
	public void onEvent(CommandEvent event) {
		Integer userId = event.getUserId();
		Integer siteId = event.getSiteId();
		
		if (siteId != null && userId != null) {
			onEvent(event, userId, siteId);
		} else {
			LOGGER.warning("event fired without site and/or user!");
		}
	}
	
	protected void onEvent(CommandEvent event, int userId, int siteId) {
		boolean isNew = isNew(event);
		
		Queue queue = QueueFactory.getQueue("commandevent");
	    queue.add(withUrl(SiteChangeServlet.ENDPOINT)
	    			.param(SiteChangeServlet.PARAM_SITE, String.valueOf(siteId))
	    			.param(SiteChangeServlet.PARAM_USER, String.valueOf(userId))
	    			.param(SiteChangeServlet.PARAM_NEW, String.valueOf(isNew)));
	}
	
	protected boolean isNew(CommandEvent event) {
		return (event.getCommand() instanceof CreateSite);
	}
}
