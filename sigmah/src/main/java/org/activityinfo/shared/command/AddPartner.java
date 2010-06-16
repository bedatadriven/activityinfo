package org.activityinfo.shared.command;

import org.activityinfo.shared.command.result.CreateResult;
import org.activityinfo.shared.dto.PartnerDTO;

/**
 * Adds a {@link org.activityinfo.server.domain.Partner} to the
 * the given {@link org.activityinfo.server.domain.UserDatabase}
 *
 * Returns {@link org.activityinfo.shared.command.result.VoidResult}
 *
 * @author Alex Bertram
 */
public class AddPartner implements Command<CreateResult> {

    private int databaseId;
    private PartnerDTO partner;

    public AddPartner() {

    }

    public AddPartner(int databaseId, PartnerDTO partner) {
        this.databaseId = databaseId;
        this.partner = partner;
    }

    public int getDatabaseId() {
        return databaseId;
    }

    public void setDatabaseId(int databaseId) {
        this.databaseId = databaseId;
    }

    public PartnerDTO getPartner() {
        return partner;
    }

    public void setPartner(PartnerDTO partner) {
        this.partner = partner;
    }
}
