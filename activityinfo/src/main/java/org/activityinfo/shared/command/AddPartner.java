package org.activityinfo.shared.command;

import org.activityinfo.shared.command.result.CreateResult;
import org.activityinfo.shared.dto.PartnerModel;

public class AddPartner implements Command<CreateResult> {

    private int databaseId;
    private PartnerModel partner;

    public AddPartner() {

    }

    public AddPartner(int databaseId, PartnerModel partner) {
        this.databaseId = databaseId;
        this.partner = partner;
    }

    public int getDatabaseId() {
        return databaseId;
    }

    public void setDatabaseId(int databaseId) {
        this.databaseId = databaseId;
    }

    public PartnerModel getPartner() {
        return partner;
    }

    public void setPartner(PartnerModel partner) {
        this.partner = partner;
    }
}
