package org.activityinfo.server.digest.geo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.activityinfo.client.i18n.I18N;
import org.activityinfo.server.command.DispatcherSync;
import org.activityinfo.server.database.hibernate.entity.SiteHistory;
import org.activityinfo.server.digest.DigestDateUtil;
import org.activityinfo.server.digest.DigestModel;
import org.activityinfo.server.digest.DigestRenderer;
import org.activityinfo.server.digest.geo.GeoDigestModel.DatabaseModel;
import org.activityinfo.shared.command.GetSites;
import org.activityinfo.shared.command.result.SiteResult;
import org.activityinfo.shared.dto.ActivityDTO;
import org.activityinfo.shared.dto.SiteDTO;
import org.activityinfo.shared.report.content.BubbleMapMarker;
import org.activityinfo.shared.report.content.MapMarker;

import com.google.common.annotations.VisibleForTesting;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class GeoDigestRenderer implements DigestRenderer {
    private static final String BUBBLE_COLOR = "67a639";
    
    private static final Logger LOGGER =
        Logger.getLogger(GeoDigestRenderer.class.getName());
    
    private final Provider<EntityManager> entityManager;
    private final DispatcherSync dispatcher;

    @Inject
    public GeoDigestRenderer(Provider<EntityManager> entityManager, DispatcherSync dispatcher) {
        this.entityManager = entityManager;
        this.dispatcher = dispatcher;
    }

    @Override
    public String renderHtml(DigestModel model) throws IOException {
        assert (model instanceof GeoDigestModel);

        StringBuilder html = new StringBuilder();
        html.append("<div id='geo-digest' style='margin-top:20px'>");

        renderHeader(html, (GeoDigestModel) model);

        renderDatabases(html, (GeoDigestModel) model);

        html.append("</div>");
        return html.toString();
    }

    private void renderHeader(StringBuilder html, GeoDigestModel model) {
        html.append("<div class='geo-header'>");
        html.append(I18N.MESSAGES.geoDigestIntro(model.getDays() * 24));
        html.append("</div>");
    }

    private void renderDatabases(StringBuilder html, GeoDigestModel model) throws IOException {
        html.append("<div class='geo-data' style='margin-top:20px'>");
        
        Collection<DatabaseModel> databases = model.getDatabases();
        for (DatabaseModel database : databases) {
            if (database.isRenderable()) {
                renderDatabase(html, database);
            }
        }

        html.append("</div>");
    }
    
    private void renderDatabase(StringBuilder html, DatabaseModel databaseModel) throws IOException {
        html.append("<div class='geo-database' style='margin-top:20px'>");
        html.append("<span class='geo-header' style='font-weight:bold; color: #" + BUBBLE_COLOR + ";'>");
        html.append(databaseModel.getName());
        html.append("</span><br>");

        html.append("<img class='geo-graph' width=\"450px\" src=\"");
        html.append(databaseModel.getUrl());
        html.append("\" /><br><br>");

        for (MapMarker marker : databaseModel.getContent().getMarkers()) {
            String label = ((BubbleMapMarker) marker).getLabel();
            html.append("<span class='geo-marker-header' style='color: #" + BUBBLE_COLOR + "; font-weight:bold;'>");
            html.append(label);
            html.append(":</span><br>");

            LOGGER.finest(marker.getSiteIds().size() + " sites for marker " + label + ": " + marker.getSiteIds());
            renderSites(html, databaseModel, marker.getSiteIds());
        }

        if (!databaseModel.getContent().getUnmappedSites().isEmpty()) {
            html.append("<br><span class='geo-unmapped-header' style='color:black; font-weight:bold;'>");
            html.append(I18N.MESSAGES.geoDigestUnmappedSites());
            html.append(":</span><br>");

            LOGGER.finest(databaseModel.getContent().getUnmappedSites().size() + " unmapped sites");
            renderSites(html, databaseModel, databaseModel.getContent().getUnmappedSites());
        }
        html.append("</div>");
    }
    
    private void renderSites(StringBuilder html, DatabaseModel databaseModel, Collection<Integer> siteIds) {
        if (!siteIds.isEmpty()) {
            for (Integer siteId : siteIds) {
                SiteResult siteResult = dispatcher.execute(GetSites.byId(siteId));
                SiteDTO siteDTO = siteResult.getData().get(0);
                ActivityDTO activityDTO = databaseModel.getModel().getSchemaDTO()
                    .getActivityById(siteDTO.getActivityId());

                List<SiteHistory> histories = findSiteHistory(siteId, databaseModel.getModel().getFrom());
                for (SiteHistory history : histories) {
                    html.append("<span class='geo-site' style='margin-left:10px;'>&bull; ");
                    html.append(I18N.MESSAGES.geoDigestSiteMsg(
                        history.getUser().getEmail(), history.getUser().getName(),
                        activityDTO.getName(), siteDTO.getLocationName()));

                    Date targetDate = databaseModel.getModel().getDate();
                    Date creationDate = new Date(history.getTimeCreated());
                    if (DigestDateUtil.isOnToday(targetDate, creationDate)) {
                        html.append(I18N.MESSAGES.geoDigestSiteMsgDateToday(creationDate));
                    } else if (DigestDateUtil.isOnYesterday(targetDate, creationDate)) {
                        html.append(I18N.MESSAGES.geoDigestSiteMsgDateYesterday(creationDate));
                    } else {
                        html.append(I18N.MESSAGES.geoDigestSiteMsgDateOther(creationDate));
                    }

                    html.append("</span><br>");
                }
            }
        }
    }

    /**
     * @param database
     * @param user
     * @param from
     * @return the sitehistory edited since the specified timestamp (milliseconds) and linked to the specified database
     *         and user. The resulting list is grouped by user, keeping the last created sitehistory entry per user.
     */
    @VisibleForTesting
    @SuppressWarnings("unchecked")
    List<SiteHistory> findSiteHistory(Integer siteId, long from) {

        Query query = entityManager.get().createQuery(
            "select distinct h from SiteHistory h " +
                "where h.site.id = :siteId and h.timeCreated >= :from " +
                "order by h.timeCreated"
            );
        query.setParameter("siteId", siteId);
        query.setParameter("from", from);

        List<SiteHistory> list = query.getResultList();

        if (list.isEmpty()) {
            return list;
        }

        Map<Integer, SiteHistory> map = new HashMap<Integer, SiteHistory>();
        for (SiteHistory siteHistory : list) {
            SiteHistory old = map.get(siteHistory.getUser().getId());
            if (old == null || old.getTimeCreated() <= siteHistory.getTimeCreated()) {
                map.put(siteHistory.getUser().getId(), siteHistory);
            }
        }
        return new ArrayList<SiteHistory>(map.values());
    }
}
