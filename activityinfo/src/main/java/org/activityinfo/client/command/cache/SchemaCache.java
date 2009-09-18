package org.activityinfo.client.command.cache;

import org.activityinfo.client.AppEvents;
import org.activityinfo.client.EventBus;
import org.activityinfo.client.command.*;
import org.activityinfo.client.event.AuthenticationEvent;
import org.activityinfo.shared.command.Command;
import org.activityinfo.shared.command.CreateEntity;
import org.activityinfo.shared.command.GetSchema;
import org.activityinfo.shared.command.UpdateEntity;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.dto.Schema;

import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.google.inject.Inject;

public class SchemaCache implements CommandProxy<GetSchema>, CommandListener {

    protected final EventBus eventBus;

    protected Schema schema = null;

    @Inject
    public SchemaCache(final EventBus eventBus, CommandEventSource source) {

        this.eventBus = eventBus;

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
            schema = (Schema)result;
            cache(schema);
        }
    }

    protected void cache(Schema schema) {

    }


    @Override
    public void onFailure(Command command, Throwable caught) {

    }
}
