package org.activityinfo.server.event.sitechange;

import org.activityinfo.server.event.CommandEvent;
import org.activityinfo.shared.command.Command;
import org.activityinfo.shared.command.CreateSite;
import org.activityinfo.shared.command.DeleteSite;
import org.activityinfo.shared.command.UpdateSite;

public enum ChangeType {
    CREATE,
    UPDATE,
    DELETE,
    UNKNOWN;

    @SuppressWarnings("rawtypes")
    public static ChangeType getType(CommandEvent event) {
        Command cmd = event.getCommand();
        if (cmd instanceof CreateSite) {
            return CREATE;
        }
        else if (cmd instanceof UpdateSite) {
            return UPDATE;
        }
        else if (cmd instanceof DeleteSite) {
            return DELETE;
        } else {
            return UNKNOWN;
        }
    }

    public boolean isKnown() {
        return (this != UNKNOWN);
    }

    public boolean isNew() {
        return (this == CREATE);
    }

    public boolean isNewOrUpdate() {
        return (this == CREATE || this == UPDATE);
    }

    public boolean isDelete() {
        return (this == DELETE);
    }
}
