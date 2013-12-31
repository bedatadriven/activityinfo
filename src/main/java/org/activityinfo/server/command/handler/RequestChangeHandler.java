package org.activityinfo.server.command.handler;

import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.server.entity.change.ChangeHandler;
import org.activityinfo.server.entity.change.ChangeRequestBuilder;
import org.activityinfo.server.entity.change.ChangeType;
import org.activityinfo.shared.command.RequestChange;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.command.result.VoidResult;
import org.activityinfo.shared.exception.CommandException;

import com.google.inject.Inject;

public class RequestChangeHandler implements CommandHandler<RequestChange> {

    private final ChangeHandler changeHandler;

    @Inject
    public RequestChangeHandler(ChangeHandler changeHandler) {
        super();
        this.changeHandler = changeHandler;
    }

    @Override
    public CommandResult execute(RequestChange cmd, User user) throws CommandException {
       
        ChangeRequestBuilder request = new ChangeRequestBuilder()
            .setChangeType(ChangeType.valueOf(cmd.getChangeType()))
            .setEntityId(cmd.getEntityId())
            .setEntityType(cmd.getEntityType())
            .setUser(user);
        
        if(cmd.getPropertyMap() != null) {
            request.setProperties(cmd.getPropertyMap().getTransientMap());
        }
        
        changeHandler.execute(request);
        
        return new VoidResult();
    }
}
