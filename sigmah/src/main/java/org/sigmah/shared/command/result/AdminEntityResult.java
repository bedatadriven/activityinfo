/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.command.result;

import org.sigmah.shared.dto.AdminEntityDTO;

import java.util.List;

/**
 * Result of a <code>GetAdminEntities</code> command.
 *
 * @see org.sigmah.shared.command.GetAdminEntities
 *
 * @author Alex Bertram
 */
public class AdminEntityResult extends ListResult<AdminEntityDTO> {

    public AdminEntityResult() {
        super();
    }

    public AdminEntityResult(List<AdminEntityDTO> data) {
        super(data);
    }

    public AdminEntityResult(AdminEntityResult result) {
        super(result);
    }
}
