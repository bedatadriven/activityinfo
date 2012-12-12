package org.activityinfo.server.event.sitehistory;

import java.util.Date;
import java.util.Map;
import java.util.logging.Level;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.activityinfo.server.command.DispatcherSync;
import org.activityinfo.server.database.hibernate.entity.Site;
import org.activityinfo.server.database.hibernate.entity.SiteHistory;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.server.event.CommandEvent;
import org.activityinfo.server.event.ServerEventBus;
import org.activityinfo.server.event.sitechange.SiteChangeListener;
import org.activityinfo.shared.command.GetSites;
import org.activityinfo.shared.command.result.SiteResult;
import org.activityinfo.shared.dto.SiteDTO;
import org.activityinfo.shared.util.JsonUtil;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class SiteHistoryListener extends SiteChangeListener {
	
	private Provider<EntityManager> entityManager;
	private DispatcherSync dispatcher;
	
	@Inject 
	public SiteHistoryListener(ServerEventBus serverEventBus, Provider<EntityManager> entityManager, DispatcherSync dispatcher) {
		super(serverEventBus);
		this.entityManager = entityManager;
		this.dispatcher = dispatcher;
	}

	@Override
	protected void onEvent(CommandEvent event, final int userId, final int siteId) {
		LOGGER.fine("persisting site history (site: "+siteId+", user: "+userId+")");
		EntityManager em = entityManager.get();
		
		Site site = em.find(Site.class, siteId);
		User user = em.find(User.class, userId);
		boolean isNew = super.isNew(event);

		if (!isNew) {
			Query q = em.createQuery("select count(*) from SiteHistory where site = :site");
			q.setParameter("site", site);
			Long count = (Long) q.getSingleResult();
			if (count == 0) {
				// update, but first entry -> repair history by adding record with complete site json
				LOGGER.fine("site is not new, but history was empty. Repairing..");
				SiteResult siteResult = dispatcher.execute(GetSites.byId(siteId));
				SiteDTO siteDTO = siteResult.getData().get(0);
				String fulljson = JsonUtil.encodeMap(siteDTO.getProperties()).toString();
			
				SiteHistory repair = new SiteHistory();
				repair.setSite(site);
				repair.setUser(user);
				repair.setJson(fulljson);
				repair.setTimeCreated(new Date().getTime());
				repair.setInitial(false);

				persist(em, repair);
			}
		}

		Map<String, Object> changeMap = event.getRpcMap().getTransientMap();
		if (!changeMap.isEmpty()) {
			String json = JsonUtil.encodeMap(changeMap).toString();
			
			SiteHistory history = new SiteHistory();
			history.setSite(site);
			history.setUser(user);
			history.setJson(json);
			history.setTimeCreated(new Date().getTime());
			history.setInitial(isNew);
	
			persist(em, history);
		}
	}
	
	private void persist(EntityManager em, SiteHistory history) {
		try {
			em.getTransaction().begin();
			
			em.persist(history);
			
			em.getTransaction().commit();
			
		} catch (Exception e) {
			try {
				em.getTransaction().rollback();
			} catch (Exception rollbackException) {
				LOGGER.log(Level.SEVERE, "Exception rolling back failed transaction", rollbackException);
			}
			throw new RuntimeException(e);
		}
	}
}
