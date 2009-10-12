package org.activityinfo.client.command.cache;

import com.google.inject.Inject;
import org.activityinfo.client.EventBus;
import org.activityinfo.client.command.CommandEventSource;
import org.activityinfo.shared.command.Command;
import org.activityinfo.shared.command.CreateEntity;
import org.activityinfo.shared.command.GetSchema;
import org.activityinfo.shared.command.UpdateEntity;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.dto.Schema;

/**
 *
 * Caches the user's schema in-memory for the duration of the session.
 *
 * TODO: we need to peridiodically check the server for updates. Do we do this here
 * or in a seperate class?
 *
 * @author Alex Bertram
 */
public class SchemaCache implements CommandProxy<GetSchema>, CommandListener {

    protected Schema schema = null;

    @Inject
    public SchemaCache(final EventBus eventBus, CommandEventSource source) {

        source.registerProxy(GetSchema.class, this);
        source.registerListener(GetSchema.class, this);
        source.registerListener(UpdateEntity.class, this);
        source.registerListener(CreateEntity.class, this);
    }
    @Override
    public CommandProxyResult execute(GetSchema command) {
        if(schema == null) {
            return CommandProxyResult.couldNotExecute();
        } else {
            return new CommandProxyResult(schema);
        }
    }

    @Override
    public void beforeCalled(Command command) {
        String entityName;
        if(command instanceof UpdateEntity) {
            entityName = ((UpdateEntity)command).getEntityName();
        } else if(command instanceof CreateEntity) {
            entityName = ((CreateEntity)command).getEntityName();
        } else {
            return;
        }
        if("Activity".equals(entityName) ||
           "AttributeGroup".equals(entityName) ||
           "Attribute".equals(entityName) ||
           "Indicator".equals(entityName)) {

            schema = null;
        }
    }

    @Override
    public void onSuccess(Command command, CommandResult result) {
        if(command instanceof GetSchema) {
            cache((Schema)result);
        }
    }

    /**
     * Caches the schema in-memory following a successful GetSchema
     * call. Subclasses can override this to provide a more permanent
     * cache.
     *
     * @param schema The schema to cache
     */
    protected void cache(Schema schema) {
        this.schema = schema;
    }


    @Override
    public void onFailure(Command command, Throwable caught) {

    }
}
