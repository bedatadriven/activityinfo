/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.command;

import org.sigmah.shared.command.result.CreateResult;
import org.sigmah.shared.dto.PartnerDTO;

/**
 * Adds a {@link org.sigmah.server.domain.OrgUnit} to the
 * the given {@link org.sigmah.server.domain.UserDatabase}
 *
 * Returns {@link org.sigmah.shared.command.result.VoidResult}
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
