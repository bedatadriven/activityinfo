package org.activityinfo.server.digest;

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
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.server.database.hibernate.entity.UserDatabase;
import org.activityinfo.server.report.output.StorageProvider;
import org.activityinfo.server.report.output.TempStorage;
import org.activityinfo.server.report.renderer.image.ImageMapRenderer;
import org.activityinfo.server.util.date.DateFormatter;
import org.activityinfo.shared.command.Filter;
import org.activityinfo.shared.command.GenerateElement;
import org.activityinfo.shared.command.GetSchema;
import org.activityinfo.shared.command.GetSites;
import org.activityinfo.shared.command.result.SiteResult;
import org.activityinfo.shared.dto.ActivityDTO;
import org.activityinfo.shared.dto.SchemaDTO;
import org.activityinfo.shared.dto.SiteDTO;
import org.activityinfo.shared.report.content.BubbleMapMarker;
import org.activityinfo.shared.report.content.MapContent;
import org.activityinfo.shared.report.content.MapMarker;
import org.activityinfo.shared.report.model.DimensionType;
import org.activityinfo.shared.report.model.MapReportElement;
import org.activityinfo.shared.report.model.clustering.AutomaticClustering;
import org.activityinfo.shared.report.model.labeling.ArabicNumberSequence;
import org.activityinfo.shared.report.model.layers.BubbleMapLayer;
import org.apache.commons.lang.StringUtils;

import com.google.common.annotations.VisibleForTesting;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class GeoDigestRenderer {
    private static final String BUBBLE_COLOR = "67a639";
    private static final int BUBBLE_SIZE = 20;
    
    private static final Logger LOGGER =
        Logger.getLogger(GeoDigestRenderer.class.getName());
    
    private final Provider<EntityManager> entityManager;
    private final DispatcherSync dispatcher;
    private final ImageMapRenderer imageMapRenderer;
    private final StorageProvider storageProvider;

    private transient Context ctx;

    @Inject
    public GeoDigestRenderer(Provider<EntityManager> entityManager,
        DispatcherSync dispatcher,
        ImageMapRenderer imageMapRenderer,
        StorageProvider storageProvider) {
        this.entityManager = entityManager;
        this.dispatcher = dispatcher;
        this.imageMapRenderer = imageMapRenderer;
        this.storageProvider = storageProvider;
    }

    /**
     * @return a list with geo digests for each database the specified user has access to by ownership or view
     *         permission, starting the specified amount of days before the specified date.
     * @throws IOException
     */
    public List<String> render(User user, Date date, int days) throws IOException {
        this.ctx = new Context(user, date, days);
        
        List<String> items = new ArrayList<String>();

        List<UserDatabase> databases = findDatabases(user);
        LOGGER.finest("found " + databases.size() + " database(s) for user " + user.getId());

        if (!databases.isEmpty()) {
            ctx.setSchemaDTO(dispatcher.execute(new GetSchema()));

            for (UserDatabase database : databases) {
                String item = renderDatabase(database);
                if (StringUtils.isNotBlank(item)) {
                    items.add(item);
                }
            }
        }

        return items;
    }

    private String renderDatabase(UserDatabase database)
        throws IOException {

        List<Integer> siteIds = findSiteIds(database, ctx.getFrom());

        LOGGER.finest("rendering geo digest for user " + ctx.getUser().getId() + " and database " + database.getId()
            + " - found " + siteIds.size() + " site(s) that were edited since "
            + DateFormatter.formatDateTime(ctx.getFrom()));

        if (siteIds.isEmpty()) {
            return null;
        }

        MapReportElement model = new MapReportElement();

        BubbleMapLayer layer = createLayer(siteIds);
        model.setLayers(layer);

        MapContent content = dispatcher.execute(new GenerateElement<MapContent>(model));
        if (content.getMarkers().isEmpty()) {
            return null;
        }

        model.setContent(content);

        TempStorage storage = storageProvider.allocateTemporaryFile("image/png", "map.png");
        imageMapRenderer.render(model, storage.getOutputStream());
        storage.getOutputStream().close();

        return renderHtml(database, content, storage);
    }

    private BubbleMapLayer createLayer(List<Integer> siteIds) {
        Filter filter = new Filter();
        filter.addRestriction(DimensionType.Site, siteIds);

        BubbleMapLayer layer = new BubbleMapLayer(filter);
        layer.setLabelSequence(new ArabicNumberSequence());
        layer.setClustering(new AutomaticClustering());
        layer.setMinRadius(BUBBLE_SIZE);
        layer.setMaxRadius(BUBBLE_SIZE);
        layer.setBubbleColor(BUBBLE_COLOR);
        return layer;
    }

    private String renderHtml(UserDatabase database, MapContent content, TempStorage storage) {

        StringBuilder html = new StringBuilder();
        
        html.append("<span class='geo-header' style='font-weight:bold;'>");
        html.append(database.getName());
        html.append("</span><br>");

        html.append("<img class='geo-graph' width=\"450px\" src=\"");
        html.append(storage.getUrl());
        html.append("\" /><br>");

        for (MapMarker marker : content.getMarkers()) {
            String label = ((BubbleMapMarker) marker).getLabel();
            html.append("<span class='geo-marker-header' style='color: #" + BUBBLE_COLOR + "; font-weight:bold;'>");
            html.append(label);
            html.append(":</span><br>");

            LOGGER.finest(marker.getSiteIds().size() + " sites for marker " + label + ": " + marker.getSiteIds());
            renderSites(html, marker.getSiteIds());
        }

        if (!content.getUnmappedSites().isEmpty()) {
            html.append("<br><span class='geo-unmapped-header' style='color: red; font-weight:bold;'>");
            html.append(I18N.MESSAGES.digestUnmappedSites());
            html.append(":</span><br>");

            LOGGER.finest(content.getUnmappedSites().size() + " unmapped sites");
            renderSites(html, content.getUnmappedSites());
        }
        
        return html.toString();
    }
    
    private void renderSites(StringBuilder html, Collection<Integer> siteIds) {
        if (!siteIds.isEmpty()) {
            for (Integer siteId : siteIds) {
                SiteResult siteResult = dispatcher.execute(GetSites.byId(siteId));
                SiteDTO siteDTO = siteResult.getData().get(0);
                ActivityDTO activityDTO = ctx.getSchemaDTO().getActivityById(siteDTO.getActivityId());

                List<SiteHistory> histories = findSiteHistory(siteId, ctx.getFrom());
                for (SiteHistory history : histories) {
                    html.append("<span class='geo-site' style='margin-left:10px;'>&bull; ");
                    html.append(I18N.MESSAGES.digestSiteMsg(
                        history.getUser().getEmail(), history.getUser().getName(),
                        activityDTO.getName(), siteDTO.getLocationName()));
                    if (DigestDateUtil.isOn(ctx.getDate(), history.getTimeCreated())) {
                        html.append(I18N.MESSAGES.digestSiteMsgDateToday(new Date(history.getTimeCreated())));
                    } else {
                        html.append(I18N.MESSAGES.digestSiteMsgDateOther(new Date(history.getTimeCreated())));
                    }
                    html.append("</span><br>");
                }
            }
        }
    }

    /**
     * @return all UserDatabases for the contextual user where the user is the database owner, or where the database has
     *         a UserPermission for the specified user with allowView set to true. If the user happens to have his
     *         emailnotification preference set to false, an empty list is returned.
     */
    @VisibleForTesting
    @SuppressWarnings("unchecked")
    List<UserDatabase> findDatabases(User user) {
        // sanity check
        if (!user.isEmailNotification()) {
            return new ArrayList<UserDatabase>();
        }

        Query query = entityManager.get().createQuery(
            "select distinct d from UserDatabase d left join d.userPermissions p " +
                "where d.owner = :user or (p.user = :user and p.allowView = true) " +
                "order by d.name"
            );
        query.setParameter("user", user);

        return query.getResultList();
    }

    /**
     * @param database
     *            the database the sites should be linked to (via an activity)
     * @param from
     *            the timestamp (millis) to start searching from for edited sites
     * @return the siteIds linked to the specified database that were edited since the specified timestamp
     */
    @VisibleForTesting
    @SuppressWarnings("unchecked")
    List<Integer> findSiteIds(UserDatabase database, long from) {

        Query query = entityManager.get().createQuery(
            "select distinct s.id from Site s " +
                "where s.activity.database = :database and s.timeEdited >= :from"
            );
        query.setParameter("database", database);
        query.setParameter("from", from);

        return query.getResultList();
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

    public Context getContext() {
        if (ctx == null) {
            throw new IllegalStateException("context not set! call render first");
        }
        return ctx;
    }

    public class Context {
        private final User user;
        private final Date date;
        private final int days;
        private final long from;
        private SchemaDTO schemaDTO;

        Context(User user, Date date, int days) {
            this.user = user;
            this.date = date;
            this.days = days;
            this.from = DigestDateUtil.millisDaysAgo(date, days);
        }

        public User getUser() {
            return user;
        }

        public Date getDate() {
            return date;
        }

        public int getDays() {
            return days;
        }

        public long getFrom() {
            return from;
        }

        public Date getFromDate() {
            return new Date(from);
        }

        private SchemaDTO getSchemaDTO() {
            return schemaDTO;
        }

        private void setSchemaDTO(SchemaDTO schemaDTO) {
            this.schemaDTO = schemaDTO;
        }
    }
}
