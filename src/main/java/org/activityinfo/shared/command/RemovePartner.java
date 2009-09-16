package org.activityinfo.shared.command;

import org.activityinfo.shared.command.result.VoidResult;

public class RemovePartner implements Command<VoidResult> {

    private int databaseId;
    private int partnerId;

    public RemovePartner() {
    }

    public RemovePartner(int databaseId, int partnerId) {
        this.databaseId = databaseId;
        this.partnerId = partnerId;
    }

    public int getDatabaseId() {
        return databaseId;
    }

    public void setDatabaseId(int databaseId) {
        this.databaseId = databaseId;
    }

    public int getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(int partnerId) {
        this.partnerId = partnerId;
    }
}
