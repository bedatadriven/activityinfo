package org.activityinfo.server.event.sitechange;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withUrl;

import java.util.Arrays;
import java.util.List;

import org.activityinfo.server.event.CommandEvent;
import org.activityinfo.server.event.CommandEventListener;
import org.activityinfo.server.event.ServerEventBus;
import org.activityinfo.shared.auth.AuthenticatedUser;
import org.activityinfo.shared.command.Command;
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
		LOGGER.info("initializing EventListener for commands CreateSite, UpdateSite");
	}

	@Override
	public void onEvent(CommandEvent event) {
		String siteId = getSiteId(event);
		String userId = getUserId(event);
		
		if (siteId != null && userId != null) {
			Queue queue = QueueFactory.getQueue("commandevent");
		    queue.add(withUrl(SiteChangeServlet.ENDPOINT)
		    			.param(SiteChangeServlet.PARAM_SITE, siteId)
		    			.param(SiteChangeServlet.PARAM_USER, userId));
		} else {
			LOGGER.warning("sitechange event fired without site and/or user!");
		}
	}
	
	public String getSiteId(CommandEvent event) {
		String siteId = null;
		Command cmd = event.getCommand();
		if (cmd instanceof CreateSite) {
			siteId = String.valueOf(((CreateSite)cmd).getSiteId());
		} else if (cmd instanceof UpdateSite) {
			siteId = String.valueOf(((UpdateSite)cmd).getSiteId());
		}
		return siteId;
	}
	
	public String getUserId(CommandEvent event) {
		String userId = null;
		AuthenticatedUser au = event.getContext().getUser();
		if (au != null) {
			userId = String.valueOf(au.getUserId());
		}
		return userId;
	}
}
