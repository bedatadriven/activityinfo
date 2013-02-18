/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.shared.command.result;

import java.util.List;

import org.activityinfo.shared.dto.AdminEntityDTO;

/**
 * Result of a <code>GetAdminEntities</code> command.
 *
 * @see org.activityinfo.shared.command.GetAdminEntities
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
