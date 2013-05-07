package org.activityinfo.server.digest.geo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.activityinfo.server.command.DispatcherSync;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.server.database.hibernate.entity.UserDatabase;
import org.activityinfo.server.digest.DigestModelBuilder;
import org.activityinfo.server.digest.geo.GeoDigestModel.DatabaseModel;
import org.activityinfo.server.report.output.StorageProvider;
import org.activityinfo.server.report.output.TempStorage;
import org.activityinfo.server.report.renderer.image.ImageMapRenderer;
import org.activityinfo.server.util.date.DateFormatter;
import org.activityinfo.shared.command.Filter;
import org.activityinfo.shared.command.GenerateElement;
import org.activityinfo.shared.command.GetSchema;
import org.activityinfo.shared.report.content.MapContent;
import org.activityinfo.shared.report.model.DimensionType;
import org.activityinfo.shared.report.model.MapReportElement;
import org.activityinfo.shared.report.model.clustering.AutomaticClustering;
import org.activityinfo.shared.report.model.labeling.ArabicNumberSequence;
import org.activityinfo.shared.report.model.layers.BubbleMapLayer;

import com.google.common.annotations.VisibleForTesting;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class GeoDigestModelBuilder implements DigestModelBuilder {
    private static final String BUBBLE_COLOR = "67a639";
    private static final int BUBBLE_SIZE = 20;
    
    private static final Logger LOGGER =
        Logger.getLogger(GeoDigestModelBuilder.class.getName());
    
    private final Provider<EntityManager> entityManager;
    private final DispatcherSync dispatcher;
    private final ImageMapRenderer imageMapRenderer;
    private final StorageProvider storageProvider;

    @Inject
    public GeoDigestModelBuilder(Provider<EntityManager> entityManager,
        DispatcherSync dispatcher,
        ImageMapRenderer imageMapRenderer,
        StorageProvider storageProvider) {
        this.entityManager = entityManager;
        this.dispatcher = dispatcher;
        this.imageMapRenderer = imageMapRenderer;
        this.storageProvider = storageProvider;
    }

    @Override
    public GeoDigestModel createModel(User user, Date date, int days) throws IOException {
        GeoDigestModel model = new GeoDigestModel(user, date, days);

        List<UserDatabase> databases = findDatabases(user);
        LOGGER.finest("found " + databases.size() + " database(s) for user " + user.getId());

        if (!databases.isEmpty()) {
            model.setSchemaDTO(dispatcher.execute(new GetSchema()));

            for (UserDatabase database : databases) {
                createDatabaseModel(model, database);
            }
        }

        return model;
    }

    private void createDatabaseModel(GeoDigestModel model, UserDatabase database) throws IOException {

        DatabaseModel databaseModel = new DatabaseModel(model, database);

        List<Integer> siteIds = findSiteIds(database, model.getFrom());

        LOGGER.finest("rendering geo digest for user " + model.getUser().getId() + " and database " + database.getId()
            + " - found " + siteIds.size() + " site(s) that were edited since "
            + DateFormatter.formatDateTime(model.getFrom()));

        if (!siteIds.isEmpty()) {
            MapReportElement reportModel = new MapReportElement();
            reportModel.setMaximumZoomLevel(9);
            
            BubbleMapLayer layer = createLayer(siteIds);
            reportModel.setLayers(layer);

            MapContent content = dispatcher.execute(new GenerateElement<MapContent>(reportModel));
            databaseModel.setContent(content);
            
            if (!content.getMarkers().isEmpty()) {
                reportModel.setContent(content);
    
                TempStorage storage = storageProvider.allocateTemporaryFile("image/png", "map.png");
                imageMapRenderer.render(reportModel, storage.getOutputStream());
                storage.getOutputStream().close();

                databaseModel.setUrl(storage.getUrl());
            }
        }
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
                "where (d.owner = :user or (p.user = :user and p.allowView = true)) " +
                "and d.dateDeleted is null " +
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
                "join s.siteHistories h " +
                "where s.activity.database = :database " +
                "and h.timeCreated >= :from"
            );
        query.setParameter("database", database);
        query.setParameter("from", from);

        return query.getResultList();
    }
}
