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
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import org.activityinfo.server.command.DispatcherSync;
import org.activityinfo.server.database.hibernate.entity.Site;
import org.activityinfo.server.database.hibernate.entity.SiteHistory;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.server.event.sitechange.ChangeType;
import org.activityinfo.shared.command.Command;
import org.activityinfo.shared.command.GetSites;
import org.activityinfo.shared.command.SiteCommand;
import org.activityinfo.shared.command.result.SiteResult;
import org.activityinfo.shared.dto.SiteDTO;
import org.activityinfo.shared.util.JsonUtil;
import org.apache.commons.lang.StringUtils;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class SiteHistoryProcessor {
    private static final Logger LOGGER = Logger.getLogger(SiteHistoryProcessor.class.getName());

    private static final String JSON_DELETE = "{\"_DELETE\":{\"type\":\"Boolean\",\"value\":true}}";

    private final Provider<EntityManager> entityManager;
    private final DispatcherSync dispatcher;

    @Inject
    public SiteHistoryProcessor(Provider<EntityManager> entityManager, DispatcherSync dispatcher) {
        this.entityManager = entityManager;
        this.dispatcher = dispatcher;
    }

    public void process(Command<?> cmd, final int userId, final int siteId) {
        assert(cmd instanceof SiteCommand);
        

        LOGGER.fine("persisting site history (site: " + siteId + ", user: "
            + userId + ")");
        EntityManager em = entityManager.get();
        
        
        // It's important to use getReference() here rather
        // than find() becuase the site might not actually have
        // been sent to the database at this point
        Site site = em.getReference(Site.class, siteId);
        User user = em.getReference(User.class, userId);
        ChangeType type = ChangeType.getType(cmd);

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

                persist(baseline);
            }
        }

        
        String json = null;
        
        if (type.isNewOrUpdate()) {
            Map<String, Object> changeMap = ((SiteCommand) cmd).getProperties().getTransientMap();
            if (!changeMap.isEmpty()) {
                json = JsonUtil.encodeMap(changeMap).toString();
            }
        } else if (type.isDelete()) {
            json = JSON_DELETE;
        }
        
        if (StringUtils.isNotBlank(json)) {
            persistHistory(site, user, type, json);
        }
    }

    public void persistHistory(Site site, User user, ChangeType type, Map<String, Object> changeMap) {
        String json = "{}";
        if (changeMap != null && !changeMap.isEmpty()) {
            json = JsonUtil.encodeMap(changeMap).toString();
        }
        persistHistory(site, user, type, json);
    }

    public void persistHistory(Site site, User user, ChangeType type, String json) {
        SiteHistory history = new SiteHistory();
        history.setSite(site);
        history.setUser(user);
        history.setJson(json);
        history.setTimeCreated(new Date().getTime());
        history.setInitial(type.isNew());

        persist(history);
    }

    private void persist(SiteHistory history) {
        EntityManager em = entityManager.get();
        EntityTransaction tx = em.getTransaction();
        boolean manageManually = !tx.isActive();

        try {
            if (manageManually) {
                tx.begin();
            }

            em.persist(history);

            if (manageManually) {
                tx.commit();
            }

        } catch (Exception e) {
            try {
                if (manageManually) {
                    tx.rollback();
                }
            } catch (Exception rollbackException) {
                LOGGER.log(Level.SEVERE, "Exception rolling back failed transaction", rollbackException);
            }
            throw new RuntimeException(e);
        }
    }
}
