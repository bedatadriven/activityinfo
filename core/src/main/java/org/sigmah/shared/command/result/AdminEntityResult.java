/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.command.result;

import java.util.List;

import org.sigmah.shared.dto.AdminEntityDTO;

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
