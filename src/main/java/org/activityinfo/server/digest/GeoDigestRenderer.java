package org.activityinfo.server.digest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.activityinfo.client.i18n.I18N;
import org.activityinfo.server.command.DispatcherSync;
import org.activityinfo.server.database.hibernate.entity.Site;
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
    private final Provider<EntityManager> entityManager;
    private final DispatcherSync dispatcher;
    private final ImageMapRenderer imageMapRenderer;
    private final StorageProvider storageProvider;

    private static final Logger LOGGER =
        Logger.getLogger(GeoDigestRenderer.class.getName());

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
     * @param user
     * @param from
     * @return a list with geo digests for each database for the specified user, starting from the specified timestamp
     *         (milliseconds).
     * @throws IOException
     */
    public List<String> render(User user, long from) throws IOException {
        List<String> items = new ArrayList<String>();

        List<UserDatabase> databases = findDatabases(user);
        LOGGER.finest("found " + databases.size() + " database(s) for user " + user.getId());

        if (!databases.isEmpty()) {
            SchemaDTO schemaDTO = dispatcher.execute(new GetSchema());

            for (UserDatabase database : databases) {
                String item = renderDatabase(schemaDTO, user, database, from);
                if (StringUtils.isNotBlank(item)) {
                    items.add(item);
                }
            }
        }

        return items;
    }

    /**
     * @param user
     * @param database
     * @param from
     * @return the geo digest for the specified user and database, starting from the specified timestamp (milliseconds).
     * @throws IOException
     */
    public String renderDatabase(SchemaDTO schemaDTO, User user, UserDatabase database, long from)
        throws IOException {

        List<Integer> siteIds = findSiteIds(database, from);

        LOGGER.finest("rendering geo digest for user " + user.getId() + " and database " + database.getId()
            + " - found " + siteIds.size() + " site(s) that were edited since " + DateFormatter.formatDateTime(from));

        if (siteIds.isEmpty()) {
            return null;
        }

        MapReportElement model = new MapReportElement();

        Filter filter = new Filter();
        filter.addRestriction(DimensionType.Site, siteIds);

        BubbleMapLayer layer = new BubbleMapLayer();
        layer.setFilter(filter);
        layer.setLabelSequence(new ArabicNumberSequence());
        layer.setMinRadius(20);
        layer.setClustering(new AutomaticClustering());

        model.setLayers(layer);

        MapContent content = dispatcher.execute(new GenerateElement<MapContent>(model));
        model.setContent(content);

        TempStorage storage = storageProvider.allocateTemporaryFile("image/png", "geo");
        imageMapRenderer.render(model, storage.getOutputStream());
        storage.getOutputStream().close();

        return renderHtml(schemaDTO, user, database, content, storage);
    }

    private String renderHtml(SchemaDTO schemaDTO, User user, UserDatabase database,
        MapContent content, TempStorage storage) {

        StringBuilder html = new StringBuilder();
        
        html.append("<b>" + database.getName() + "</b><br>");
        html.append("<img width=\"450px\" src=\"" + storage.getUrl() + "\" /><br>");

        for (MapMarker marker : content.getMarkers()) {
            String label = ((BubbleMapMarker) marker).getLabel();
            html.append("<span style='color: red; font-weight:bold;'>" + label + ":</span><br>");
            LOGGER.finest(marker.getSiteIds().size() + " sites for marker " + label + ": " + marker.getSiteIds());
            renderSiteMsgs(html, schemaDTO, user, marker.getSiteIds());
        }

        if (!content.getUnmappedSites().isEmpty()) {
            LOGGER.finest(content.getUnmappedSites().size() + " unmapped sites");
            html.append("<br><span style='color: red; font-weight:bold;'>" +
                I18N.MESSAGES.digestUnmappedSites() + ":</span><br>");
            renderSiteMsgs(html, schemaDTO, user, content.getUnmappedSites());
        }
        
        return html.toString();
    }
    
    private void renderSiteMsgs(StringBuilder html, SchemaDTO schemaDTO, User user, Collection<Integer> siteIds) {
        if (!siteIds.isEmpty()) {
            for (Integer siteId : siteIds) {
                html.append("<span style='margin-left:10px;'><b>&bull;</b> ");
                SiteResult siteResult = dispatcher.execute(GetSites.byId(siteId));
                SiteDTO siteDTO = siteResult.getData().get(0);
                ActivityDTO activityDTO = schemaDTO.getActivityById(siteDTO.getActivityId());

                String siteMsg =
                    I18N.MESSAGES.digestSiteMsg(activityDTO.getName(), siteDTO.getLocationName(),
                        user.getName(), user.getEmail(), siteDTO.getDateEdited());
                html.append(siteMsg);
                html.append("</span><br>");
            }
        }
    }

    /**
     * @param user
     *            the user to find the databases for
     * @return all UserDatabases for the specified user where the user is the database owner, or where the database has
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
            "select distinct s.id from Site s where s.activity.database = :database and s.timeEdited > :from"
            );
        query.setParameter("database", database);
        query.setParameter("from", from);

        return query.getResultList();
    }

    private Map<Integer, Site> toMap(List<Site> sites) {
        Map<Integer, Site> siteMap = new HashMap<Integer, Site>();
        for (Site s : sites) {
            siteMap.put(s.getId(), s);
        }
        return siteMap;
    }
}
