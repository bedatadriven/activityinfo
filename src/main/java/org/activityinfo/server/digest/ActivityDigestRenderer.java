package org.activityinfo.server.digest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;

import org.activityinfo.server.command.DispatcherSync;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.server.report.output.StorageProvider;
import org.activityinfo.server.report.renderer.image.ImageMapRenderer;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class ActivityDigestRenderer {
    private final Provider<EntityManager> entityManager;
    private final DispatcherSync dispatcher;
    private final ImageMapRenderer imageMapRenderer;
    private final StorageProvider storageProvider;

    private static final Logger LOGGER =
        Logger.getLogger(ActivityDigestRenderer.class.getName());

    @Inject
    public ActivityDigestRenderer(Provider<EntityManager> entityManager,
        DispatcherSync dispatcher,
        ImageMapRenderer imageMapRenderer,
        StorageProvider storageProvider) {
        this.entityManager = entityManager;
        this.dispatcher = dispatcher;
        this.imageMapRenderer = imageMapRenderer;
        this.storageProvider = storageProvider;
    }

    public List<String> render(User user, long from) throws IOException {
        List<String> items = new ArrayList<String>();

        return items;
    }

}
