/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.client.dispatch.remote.cache;

import org.activityinfo.client.dispatch.CommandProxy;
import org.activityinfo.client.dispatch.DispatchEventSource;
import org.activityinfo.client.dispatch.DispatchListener;
import org.activityinfo.shared.command.AddPartner;
import org.activityinfo.shared.command.Command;
import org.activityinfo.shared.command.CreateEntity;
import org.activityinfo.shared.command.GetSchema;
import org.activityinfo.shared.command.RemovePartner;
import org.activityinfo.shared.command.UpdateEntity;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.dto.SchemaDTO;

import com.google.inject.Inject;

/**
 * Caches the user's schema in-memory for the duration of the session.

 * TODO: we need to peridiodically check the server for updates. Do we do this here
 * or in a separate class?
 *
 * @author Alex Bertram
 */
public class SchemaCache implements CommandProxy<GetSchema>, DispatchListener {

    private SchemaDTO schema = null;

    @Inject
    public SchemaCache(DispatchEventSource source) {

        source.registerProxy(GetSchema.class, this);
        source.registerListener(GetSchema.class, this);
        source.registerListener(UpdateEntity.class, this);
        source.registerListener(CreateEntity.class, this);
        source.registerListener(AddPartner.class, this);
        source.registerListener(RemovePartner.class, this);
    }

    @Override
    public ProxyResult maybeExecute(GetSchema command) {
        if (schema == null) {
            return ProxyResult.couldNotExecute();
        } else {
            return new ProxyResult(schema);
        }
    }

    @Override
    public void beforeDispatched(Command command) {
        String entityName;
        if (command instanceof UpdateEntity && isSchemaEntity(((UpdateEntity) command).getEntityName())) {
            schema = null;
        } else if (command instanceof CreateEntity && isSchemaEntity(((CreateEntity) command).getEntityName())) {
            schema = null;
        } else if (command instanceof AddPartner ||
                command instanceof RemovePartner) {
            schema = null;
        }
    }

    private boolean isSchemaEntity(String entityName) {
        return ("UserDatabase".equals(entityName) ||
                "Activity".equals(entityName) ||
                "AttributeGroup".equals(entityName) ||
                "Attribute".equals(entityName) ||
                "Indicator".equals(entityName));
    }

    @Override
    public void onSuccess(Command command, CommandResult result) {
        if (command instanceof GetSchema) {
            cache((SchemaDTO) result);
        } else if (schema != null) {
            if (command instanceof AddPartner) {
                AddPartner add = (AddPartner) command;
            }
        }
    }

    /**
     * Caches the schema in-memory following a successful GetSchema
     * call. Subclasses can override this to provide a more permanent
     * cache.
     *
     * @param schema The schema to cache
     */
    protected void cache(SchemaDTO schema) {
        this.schema = schema;
    }


    @Override
    public void onFailure(Command command, Throwable caught) {

    }
}
