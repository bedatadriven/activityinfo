package org.activityinfo.server.event.sitehistory;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

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
import org.activityinfo.server.event.sitechange.ChangeType;
import org.activityinfo.server.event.sitechange.SiteChangeListener;
import org.activityinfo.shared.command.GetSites;
import org.activityinfo.shared.command.result.SiteResult;
import org.activityinfo.shared.dto.SiteDTO;
import org.activityinfo.shared.util.JsonUtil;
import org.apache.commons.lang.StringUtils;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class SiteHistoryListener extends SiteChangeListener {

    private static final String JSON_DELETE = "{\"_DELETE\":{\"type\":\"Boolean\",\"value\":true}}";

    private final Provider<EntityManager> entityManager;
    private final DispatcherSync dispatcher;

    @Inject
    public SiteHistoryListener(ServerEventBus serverEventBus,
        Provider<EntityManager> entityManager, DispatcherSync dispatcher) {
        super(serverEventBus);
        this.entityManager = entityManager;
        this.dispatcher = dispatcher;
    }

    @Override
    protected void onEvent(CommandEvent event, final int userId,
        final int siteId) {
        LOGGER.fine("persisting site history (site: " + siteId + ", user: "
            + userId + ")");
        EntityManager em = entityManager.get();

        Site site = em.find(Site.class, siteId);
        User user = em.find(User.class, userId);
        ChangeType type = getType(event);

        if (!type.isNew()) {
            Query q = em
                .createQuery("select count(*) from SiteHistory where site = :site");
            q.setParameter("site", site);
            Long count = (Long) q.getSingleResult();
            if (count == 0) {
                // update, but first entry -> repair history by adding baseline
                // record with complete site json
                LOGGER
                    .fine("site is not new, but history was empty. Adding baseline record..");
                SiteResult siteResult = dispatcher.execute(GetSites
                    .byId(siteId));
                SiteDTO siteDTO = siteResult.getData().get(0);
                String fulljson = JsonUtil.encodeMap(siteDTO.getProperties())
                    .toString();

                SiteHistory baseline = new SiteHistory();
                baseline.setSite(site);
                baseline.setUser(user);
                baseline.setJson(fulljson);
                baseline.setTimeCreated(new Date().getTime());
                baseline.setInitial(false);

                persist(em, baseline);
            }
        }

        
        String json = null;
        
        if (type.isNewOrUpdate()) {
            Map<String, Object> changeMap = event.getRpcMap().getTransientMap();
            if (!changeMap.isEmpty()) {
                json = JsonUtil.encodeMap(changeMap).toString();
            }
        } else if (type.isDelete()) {
            json = JSON_DELETE;
        }
        
        if (StringUtils.isNotBlank(json)) {
            persistHistory(em, site, user, type, json);
        }
    }

    private void persistHistory(EntityManager em, Site site, User user, ChangeType type, String json) {
        SiteHistory history = new SiteHistory();
        history.setSite(site);
        history.setUser(user);
        history.setJson(json);
        history.setTimeCreated(new Date().getTime());
        history.setInitial(type.isNew());

        persist(em, history);
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
                LOGGER.log(Level.SEVERE,
                    "Exception rolling back failed transaction",
                    rollbackException);
            }
            throw new RuntimeException(e);
        }
    }
}
