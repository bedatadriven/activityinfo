package org.activityinfo.client.offline;

import com.google.gwt.core.client.GWT;
import com.google.gwt.gears.client.database.Database;
import com.google.gwt.gears.client.database.DatabaseException;
import com.google.inject.Inject;
import org.activityinfo.client.EventBus;
import org.activityinfo.client.dispatch.DispatchEventSource;
import org.activityinfo.client.dispatch.remote.cache.CommandProxyResult;
import org.activityinfo.client.dispatch.remote.cache.SchemaCache;
import org.activityinfo.client.offline.dao.SchemaDAO;
import org.activityinfo.shared.command.GetSchema;
import org.activityinfo.shared.dto.Schema;

/**
 * Extends the default in-memory cache to persist the schema
 * across sessions in the Gears database.
 *
 * @author Alex Bertram
 */
public class OfflineSchemaCache extends SchemaCache {

    private SchemaDAO schemaDAO;

    @Inject
    public OfflineSchemaCache(EventBus eventBus, DispatchEventSource source, Database database) {
        super(source);

        try {
            schemaDAO = new SchemaDAO(database);
        } catch (DatabaseException e) {
            GWT.log("OfflineSchemaCache: Creation of SchemaDAO failed", e);
            schemaDAO = null;
        }
    }

    @Override
    protected void cache(Schema schema) {
        super.cache(schema);
        if (schema != null) {

            try {
                schemaDAO.save(schema);
            } catch (DatabaseException e) {
                GWT.log("OfflineSchemaCache: Saving of schema failed", e);
            }
        }
    }

    @Override
    public CommandProxyResult execute(GetSchema command) {
        // try first from memory, this will be a lot faster
        CommandProxyResult cpr = super.execute(command);
        if (cpr.couldExecute)
            return cpr;

        // otherwise try to load from local DB
        try {
            schema = schemaDAO.load();
            if (schema == null) {
                return CommandProxyResult.couldNotExecute();
            } else {
                return new CommandProxyResult(schema);
            }
        } catch (DatabaseException e) {
            GWT.log("OfflineSchemaCache: failed to load schema from database", e);
            return CommandProxyResult.couldNotExecute();
        }
    }

}
